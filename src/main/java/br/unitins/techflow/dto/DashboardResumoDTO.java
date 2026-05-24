package br.unitins.techflow.dto;

public record DashboardResumoDTO(
        long totalCategorias,
        long totalTecnicos,
        long totalProblemas,
        long totalSolucoes,
        long totalChamados,
        long chamadosAbertos,
        long chamadosEmAndamento,
        long chamadosFechados
) {
}