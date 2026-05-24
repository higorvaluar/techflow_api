package br.unitins.techflow.service;

import br.unitins.techflow.model.Tecnico;
import br.unitins.techflow.repository.TecnicoRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TecnicoService {

    private final TecnicoRepository tecnicoRepository;

    public TecnicoService(TecnicoRepository tecnicoRepository) {
        this.tecnicoRepository = tecnicoRepository;
    }

    public List<Tecnico> listarTodos() {
        return tecnicoRepository.findAll();
    }

    public Tecnico buscarPorId(Long id) {
        return tecnicoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Técnico não encontrado"));
    }

    public Tecnico criar(Tecnico tecnico) {
        return tecnicoRepository.save(tecnico);
    }

    public Tecnico atualizar(Long id, Tecnico tecnicoAtualizado) {
        Tecnico tecnico = buscarPorId(id);

        tecnico.setNome(tecnicoAtualizado.getNome());
        tecnico.setEmail(tecnicoAtualizado.getEmail());
        tecnico.setSenha(tecnicoAtualizado.getSenha());
        tecnico.setNivelAcesso(tecnicoAtualizado.getNivelAcesso());

        return tecnicoRepository.save(tecnico);
    }

    public void deletar(Long id) {
        Tecnico tecnico = buscarPorId(id);
        tecnicoRepository.delete(tecnico);
    }
}