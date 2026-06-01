package br.unitins.techflow.service;

import br.unitins.techflow.dto.AnaliseProblemaResponseDTO;
import br.unitins.techflow.model.Categoria;
import br.unitins.techflow.model.Solucao;
import br.unitins.techflow.repository.CategoriaRepository;
import br.unitins.techflow.repository.SolucaoRepository;
import br.unitins.techflow.controller.IaController;
import tools.jackson.databind.JsonNode;
import tools.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class GeminiService {

    private final RestClient restClient;
    private final ObjectMapper objectMapper;
    private final CategoriaRepository categoriaRepository;
    private final SolucaoRepository solucaoRepository;

    @Value("${gemini.api.key}")
    private String apiKey;

    @Value("${gemini.api.url}")
    private String apiUrl;

    public GeminiService(
            RestClient.Builder restClientBuilder,
            ObjectMapper objectMapper,
            CategoriaRepository categoriaRepository,
            SolucaoRepository solucaoRepository
    ) {
        this.restClient = restClientBuilder.build();
        this.objectMapper = objectMapper;
        this.categoriaRepository = categoriaRepository;
        this.solucaoRepository = solucaoRepository;
    }

    public AnaliseProblemaResponseDTO analisarProblema(String descricaoProblema) {
        if (apiKey == null || apiKey.isBlank()) {
            throw new RuntimeException("Chave da Gemini API não configurada");
        }

        String prompt = montarPrompt(descricaoProblema);

        Map<String, Object> body = Map.of(
                "contents", List.of(
                        Map.of(
                                "parts", List.of(
                                        Map.of("text", prompt)
                                )
                        )
                )
        );

        String respostaGemini = restClient.post()
                .uri(apiUrl)
                .header("x-goog-api-key", apiKey)
                .contentType(MediaType.APPLICATION_JSON)
                .body(body)
                .retrieve()
                .body(String.class);

        return converterResposta(respostaGemini);
    }

    public String resumirSolucao(String problemaTitulo, String descricaoSolucao) {
        if (apiKey == null || apiKey.isBlank()) {
            throw new RuntimeException("Chave da Gemini API não configurada");
        }

        String prompt = """
            Você é um analista de suporte de TI.

            Resuma a solução técnica abaixo em no máximo 3 linhas.
            O resumo deve ser em português, objetivo, sem lista, sem markdown e sem rótulos.
            Não comece com "A solução foi" ou "Resumo:".

            Problema:
            %s

            Solução aplicada:
            %s

            Responda apenas com o resumo final.
            """.formatted(problemaTitulo, descricaoSolucao);

        Map<String, Object> body = Map.of(
                "contents", List.of(
                        Map.of(
                                "parts", List.of(
                                        Map.of("text", prompt)
                                )
                        )
                )
        );

        String respostaGemini = restClient.post()
                .uri(apiUrl)
                .header("x-goog-api-key", apiKey)
                .contentType(MediaType.APPLICATION_JSON)
                .body(body)
                .retrieve()
                .body(String.class);

        return extrairTextoSimples(respostaGemini);
    }

    public List<IaController.ResultadoBuscaBaseResponse> buscarBaseConhecimento(
            String pergunta,
            List<Map<String, Object>> entradas
    ) {
        if (apiKey == null || apiKey.isBlank()) {
            throw new RuntimeException("Chave da Gemini API não configurada");
        }

        String baseConhecimento = entradas.stream()
                .map(item -> "ID " + item.get("id")
                        + " | Título: " + item.get("titulo")
                        + " | Categoria: " + item.get("categoria")
                        + " | Solução: " + item.get("solucao"))
                .collect(Collectors.joining("\n"));

        if (baseConhecimento.isBlank()) {
            baseConhecimento = "Nenhuma solução cadastrada.";
        }

        String prompt = """
            Você é a busca inteligente de uma base de conhecimento de suporte de TI.

            O usuário descreveu um problema. Analise a base abaixo e selecione os itens mais relevantes.
            Retorne no máximo 4 resultados, ordenados do mais relevante para o menos relevante.

            BASE DE CONHECIMENTO:
            %s

            PROBLEMA DO USUÁRIO:
            %s

            Responda SOMENTE com JSON válido, sem markdown, sem explicações e sem texto fora do JSON.

            Use exatamente este formato:
            [
              {
                "id": 1,
                "motivo": "Essa solução é relevante porque trata de problema semelhante."
              }
            ]
            """.formatted(baseConhecimento, pergunta);

        Map<String, Object> body = Map.of(
                "contents", List.of(
                        Map.of(
                                "parts", List.of(
                                        Map.of("text", prompt)
                                )
                        )
                )
        );

        String respostaGemini = restClient.post()
                .uri(apiUrl)
                .header("x-goog-api-key", apiKey)
                .contentType(MediaType.APPLICATION_JSON)
                .body(body)
                .retrieve()
                .body(String.class);

        return converterRespostaBuscaBase(respostaGemini);
    }

    private List<IaController.ResultadoBuscaBaseResponse> converterRespostaBuscaBase(String respostaGemini) {
        try {
            JsonNode root = objectMapper.readTree(respostaGemini);

            String texto = root
                    .path("candidates")
                    .path(0)
                    .path("content")
                    .path("parts")
                    .path(0)
                    .path("text")
                    .asText();

            String jsonLimpo = limparJson(texto);

            JsonNode lista = objectMapper.readTree(jsonLimpo);

            if (!lista.isArray()) {
                return List.of();
            }

            java.util.ArrayList<IaController.ResultadoBuscaBaseResponse> resultados = new java.util.ArrayList<>();

            for (JsonNode item : lista) {
                resultados.add(new IaController.ResultadoBuscaBaseResponse(
                        item.path("id").asLong(),
                        item.path("motivo").asText()
                ));
            }

            return resultados;

        } catch (Exception e) {
            throw new RuntimeException("Erro ao processar busca na base de conhecimento: " + e.getMessage(), e);
        }
    }

    private String extrairTextoSimples(String respostaGemini) {
        try {
            JsonNode root = objectMapper.readTree(respostaGemini);

            String texto = root
                    .path("candidates")
                    .path(0)
                    .path("content")
                    .path("parts")
                    .path(0)
                    .path("text")
                    .asText();

            return texto
                    .replace("Resumo:", "")
                    .replace("resumo:", "")
                    .trim();

        } catch (Exception e) {
            throw new RuntimeException("Erro ao processar resposta da Gemini API: " + e.getMessage(), e);
        }
    }

    private String montarPrompt(String descricaoProblema) {
        String categorias = categoriaRepository.findAll()
                .stream()
                .map(this::formatarCategoria)
                .collect(Collectors.joining("\n"));

        String solucoesAnteriores = solucaoRepository.findAll()
                .stream()
                .limit(10)
                .map(this::formatarSolucao)
                .collect(Collectors.joining("\n"));

        if (categorias.isBlank()) {
            categorias = "Nenhuma categoria cadastrada.";
        }

        if (solucoesAnteriores.isBlank()) {
            solucoesAnteriores = "Nenhuma solução anterior cadastrada.";
        }

        return """
                Você é uma IA de apoio a uma equipe de suporte de TI.

                Sua tarefa é analisar um problema técnico informado por um usuário e sugerir:
                - categoria mais adequada;
                - prioridade;
                - palavras-chave;
                - resumo do problema;
                - possível solução.

                Categorias existentes:
                %s

                Soluções anteriores cadastradas:
                %s

                Problema informado:
                %s

                Responda SOMENTE com JSON válido, sem markdown, sem explicações e sem texto fora do JSON.

                Use exatamente este formato:

                {
                  "categoria_sugerida": "Rede",
                  "prioridade_sugerida": "Média",
                  "palavras_chave": ["internet", "Wi-Fi", "DNS"],
                  "resumo_problema": "Resumo curto do problema.",
                  "solucao_sugerida": "Solução sugerida de forma objetiva."
                }
                """.formatted(categorias, solucoesAnteriores, descricaoProblema);
    }

    private String formatarCategoria(Categoria categoria) {
        return "- " + categoria.getNome() + ": " + categoria.getDescricao();
    }

    private String formatarSolucao(Solucao solucao) {
        return "- Problema: " + solucao.getProblema().getTitulo()
                + " | Solução: " + solucao.getDescricaoSolucao();
    }

    private AnaliseProblemaResponseDTO converterResposta(String respostaGemini) {
        try {
            JsonNode root = objectMapper.readTree(respostaGemini);

            String texto = root
                    .path("candidates")
                    .path(0)
                    .path("content")
                    .path("parts")
                    .path(0)
                    .path("text")
                    .asText();

            String jsonLimpo = limparJson(texto);

            return objectMapper.readValue(jsonLimpo, AnaliseProblemaResponseDTO.class);
        } catch (Exception e) {
            throw new RuntimeException("Erro ao processar resposta da Gemini API: " + e.getMessage(), e);
        }
    }

    private String limparJson(String texto) {
        return texto
                .replace("```json", "")
                .replace("```", "")
                .trim();
    }
}