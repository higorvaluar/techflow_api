package br.unitins.techflow.service;

import br.unitins.techflow.model.Chamado;
import br.unitins.techflow.model.Problema;
import br.unitins.techflow.model.Tecnico;
import br.unitins.techflow.repository.ChamadoRepository;
import br.unitins.techflow.repository.ProblemaRepository;
import br.unitins.techflow.repository.TecnicoRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ChamadoService {

    private final ChamadoRepository chamadoRepository;
    private final TecnicoRepository tecnicoRepository;
    private final ProblemaRepository problemaRepository;

    public ChamadoService(
            ChamadoRepository chamadoRepository,
            TecnicoRepository tecnicoRepository,
            ProblemaRepository problemaRepository
    ) {
        this.chamadoRepository = chamadoRepository;
        this.tecnicoRepository = tecnicoRepository;
        this.problemaRepository = problemaRepository;
    }

    public List<Chamado> listarTodos() {
        return chamadoRepository.findAll();
    }

    public Chamado buscarPorId(Long id) {
        return chamadoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Chamado não encontrado"));
    }

    public Chamado criar(Chamado chamado) {
        vincularTecnicoEProblema(chamado);
        return chamadoRepository.save(chamado);
    }

    public Chamado atualizar(Long id, Chamado chamadoAtualizado) {
        Chamado chamado = buscarPorId(id);

        chamado.setTitulo(chamadoAtualizado.getTitulo());
        chamado.setDescricao(chamadoAtualizado.getDescricao());
        chamado.setStatus(chamadoAtualizado.getStatus());
        chamado.setPrioridade(chamadoAtualizado.getPrioridade());
        chamado.setDataAbertura(chamadoAtualizado.getDataAbertura());
        chamado.setDataFechamento(chamadoAtualizado.getDataFechamento());
        chamado.setTecnico(chamadoAtualizado.getTecnico());
        chamado.setProblema(chamadoAtualizado.getProblema());

        vincularTecnicoEProblema(chamado);

        return chamadoRepository.save(chamado);
    }

    public void deletar(Long id) {
        Chamado chamado = buscarPorId(id);
        chamadoRepository.delete(chamado);
    }

    private void vincularTecnicoEProblema(Chamado chamado) {
        if (chamado.getTecnico() == null || chamado.getTecnico().getId() == null) {
            throw new RuntimeException("Informe o ID do técnico");
        }

        if (chamado.getProblema() == null || chamado.getProblema().getId() == null) {
            throw new RuntimeException("Informe o ID do problema");
        }

        Tecnico tecnico = tecnicoRepository.findById(chamado.getTecnico().getId())
                .orElseThrow(() -> new RuntimeException("Técnico não encontrado"));

        Problema problema = problemaRepository.findById(chamado.getProblema().getId())
                .orElseThrow(() -> new RuntimeException("Problema não encontrado"));

        chamado.setTecnico(tecnico);
        chamado.setProblema(problema);
    }
}