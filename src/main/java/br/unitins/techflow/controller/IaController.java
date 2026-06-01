package br.unitins.techflow.controller;

import br.unitins.techflow.dto.AnaliseProblemaRequestDTO;
import br.unitins.techflow.dto.AnaliseProblemaResponseDTO;
import br.unitins.techflow.service.GeminiService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/ia")
public class IaController {

    private final GeminiService geminiService;

    public IaController(GeminiService geminiService) {
        this.geminiService = geminiService;
    }

    @PostMapping("/analisar-problema")
    public ResponseEntity<AnaliseProblemaResponseDTO> analisarProblema(
            @Valid @RequestBody AnaliseProblemaRequestDTO request
    ) {
        return ResponseEntity.ok(geminiService.analisarProblema(request.descricaoProblema()));
    }

    @PostMapping("/resumir-solucao")
    public ResponseEntity<ResumoSolucaoResponse> resumirSolucao(
            @RequestBody ResumoSolucaoRequest request
    ) {
        String resumo = geminiService.resumirSolucao(
                request.problemaTitulo(),
                request.descricaoSolucao()
        );

        return ResponseEntity.ok(new ResumoSolucaoResponse(resumo));
    }

    public record ResumoSolucaoRequest(
            String problemaTitulo,
            String descricaoSolucao
    ) {
    }

    public record ResumoSolucaoResponse(
            String resumo
    ) {
    }
}