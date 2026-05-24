package br.unitins.techflow.service;

import br.unitins.techflow.dto.DashboardResumoDTO;
import br.unitins.techflow.repository.CategoriaRepository;
import br.unitins.techflow.repository.ChamadoRepository;
import br.unitins.techflow.repository.ProblemaRepository;
import br.unitins.techflow.repository.SolucaoRepository;
import br.unitins.techflow.repository.TecnicoRepository;
import org.springframework.stereotype.Service;

@Service
public class DashboardService {

    private final CategoriaRepository categoriaRepository;
    private final TecnicoRepository tecnicoRepository;
    private final ProblemaRepository problemaRepository;
    private final SolucaoRepository solucaoRepository;
    private final ChamadoRepository chamadoRepository;

    public DashboardService(
            CategoriaRepository categoriaRepository,
            TecnicoRepository tecnicoRepository,
            ProblemaRepository problemaRepository,
            SolucaoRepository solucaoRepository,
            ChamadoRepository chamadoRepository
    ) {
        this.categoriaRepository = categoriaRepository;
        this.tecnicoRepository = tecnicoRepository;
        this.problemaRepository = problemaRepository;
        this.solucaoRepository = solucaoRepository;
        this.chamadoRepository = chamadoRepository;
    }

    public DashboardResumoDTO gerarResumo() {
        long totalCategorias = categoriaRepository.count();
        long totalTecnicos = tecnicoRepository.count();
        long totalProblemas = problemaRepository.count();
        long totalSolucoes = solucaoRepository.count();
        long totalChamados = chamadoRepository.count();

        long chamadosAbertos = chamadoRepository.countByStatusIgnoreCase("ABERTO");
        long chamadosEmAndamento = chamadoRepository.countByStatusIgnoreCase("EM_ANDAMENTO");
        long chamadosFechados = chamadoRepository.countByStatusIgnoreCase("FECHADO");

        return new DashboardResumoDTO(
                totalCategorias,
                totalTecnicos,
                totalProblemas,
                totalSolucoes,
                totalChamados,
                chamadosAbertos,
                chamadosEmAndamento,
                chamadosFechados
        );
    }
}