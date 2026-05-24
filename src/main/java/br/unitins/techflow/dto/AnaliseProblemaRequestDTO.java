package br.unitins.techflow.dto;

import jakarta.validation.constraints.NotBlank;

public record AnaliseProblemaRequestDTO(
        @NotBlank(message = "A descrição do problema é obrigatória")
        String descricaoProblema
) {
}