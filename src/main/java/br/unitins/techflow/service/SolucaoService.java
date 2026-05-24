package br.unitins.techflow.service;

import br.unitins.techflow.model.Problema;
import br.unitins.techflow.model.Solucao;
import br.unitins.techflow.repository.ProblemaRepository;
import br.unitins.techflow.repository.SolucaoRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SolucaoService {

    private final SolucaoRepository solucaoRepository;
    private final ProblemaRepository problemaRepository;

    public SolucaoService(SolucaoRepository solucaoRepository, ProblemaRepository problemaRepository) {
        this.solucaoRepository = solucaoRepository;
        this.problemaRepository = problemaRepository;
    }

    public List<Solucao> listarTodas() {
        return solucaoRepository.findAll();
    }

    public Solucao buscarPorId(Long id) {
        return solucaoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Solução não encontrada"));
    }

    public Solucao criar(Solucao solucao) {
        vincularProblema(solucao);
        return solucaoRepository.save(solucao);
    }

    public Solucao atualizar(Long id, Solucao solucaoAtualizada) {
        Solucao solucao = buscarPorId(id);

        solucao.setDescricaoSolucao(solucaoAtualizada.getDescricaoSolucao());
        solucao.setResumoIa(solucaoAtualizada.getResumoIa());
        solucao.setDataSolucao(solucaoAtualizada.getDataSolucao());
        solucao.setProblema(solucaoAtualizada.getProblema());

        vincularProblema(solucao);

        return solucaoRepository.save(solucao);
    }

    public void deletar(Long id) {
        Solucao solucao = buscarPorId(id);
        solucaoRepository.delete(solucao);
    }

    private void vincularProblema(Solucao solucao) {
        if (solucao.getProblema() == null || solucao.getProblema().getId() == null) {
            throw new RuntimeException("Informe o ID do problema");
        }

        Problema problema = problemaRepository.findById(solucao.getProblema().getId())
                .orElseThrow(() -> new RuntimeException("Problema não encontrado"));

        solucao.setProblema(problema);
    }
}