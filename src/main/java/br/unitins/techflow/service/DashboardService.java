package br.unitins.techflow.service;

import br.unitins.techflow.dto.DashboardResumoDTO;
import br.unitins.techflow.repository.CategoriaRepository;
import br.unitins.techflow.repository.ChamadoRepository;
import br.unitins.techflow.repository.ProblemaRepository;
import br.unitins.techflow.repository.SolucaoRepository;
import br.unitins.techflow.repository.TecnicoRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.stream.Collectors;

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

    public Map<String, Object> gerarCharts() {
        Map<String, Long> problemasPorCategoriaMap = problemaRepository.findAll()
                .stream()
                .collect(Collectors.groupingBy(
                        problema -> problema.getCategoria() != null
                                ? problema.getCategoria().getNome()
                                : "Sem categoria",
                        LinkedHashMap::new,
                        Collectors.counting()
                ));

        List<Map<String, Object>> problemasPorCategoria = new ArrayList<>();

        problemasPorCategoriaMap.forEach((categoria, total) -> {
            Map<String, Object> item = new LinkedHashMap<>();
            item.put("categoria", categoria);
            item.put("total", total);
            problemasPorCategoria.add(item);
        });

        Map<Integer, Long> chamadosPorMesMap = chamadoRepository.findAll()
                .stream()
                .filter(chamado -> chamado.getDataAbertura() != null)
                .collect(Collectors.groupingBy(
                        chamado -> chamado.getDataAbertura().getMonthValue(),
                        TreeMap::new,
                        Collectors.counting()
                ));

        List<Map<String, Object>> chamadosPorMes = new ArrayList<>();

        chamadosPorMesMap.forEach((mes, total) -> {
            Map<String, Object> item = new LinkedHashMap<>();
            item.put("mes", abreviarMes(mes));
            item.put("total", total);
            chamadosPorMes.add(item);
        });

        Map<String, Object> resposta = new LinkedHashMap<>();
        resposta.put("problemasPorCategoria", problemasPorCategoria);
        resposta.put("chamadosPorMes", chamadosPorMes);

        return resposta;
    }

    private String abreviarMes(Integer mes) {
        return switch (mes) {
            case 1 -> "Jan";
            case 2 -> "Fev";
            case 3 -> "Mar";
            case 4 -> "Abr";
            case 5 -> "Mai";
            case 6 -> "Jun";
            case 7 -> "Jul";
            case 8 -> "Ago";
            case 9 -> "Set";
            case 10 -> "Out";
            case 11 -> "Nov";
            case 12 -> "Dez";
            default -> "Mês";
        };
    }
}