package br.unitins.techflow.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public record AnaliseProblemaResponseDTO(
        @JsonProperty("categoria_sugerida")
        String categoriaSugerida,

        @JsonProperty("prioridade_sugerida")
        String prioridadeSugerida,

        @JsonProperty("palavras_chave")
        List<String> palavrasChave,

        @JsonProperty("resumo_problema")
        String resumoProblema,

        @JsonProperty("solucao_sugerida")
        String solucaoSugerida
) {
}