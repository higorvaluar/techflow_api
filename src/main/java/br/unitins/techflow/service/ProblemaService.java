package br.unitins.techflow.service;

import br.unitins.techflow.model.Categoria;
import br.unitins.techflow.model.Problema;
import br.unitins.techflow.model.Tecnico;
import br.unitins.techflow.repository.CategoriaRepository;
import br.unitins.techflow.repository.ProblemaRepository;
import br.unitins.techflow.repository.TecnicoRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProblemaService {

    private final ProblemaRepository problemaRepository;
    private final CategoriaRepository categoriaRepository;
    private final TecnicoRepository tecnicoRepository;

    public ProblemaService(
            ProblemaRepository problemaRepository,
            CategoriaRepository categoriaRepository,
            TecnicoRepository tecnicoRepository
    ) {
        this.problemaRepository = problemaRepository;
        this.categoriaRepository = categoriaRepository;
        this.tecnicoRepository = tecnicoRepository;
    }

    public List<Problema> listarTodos() {
        return problemaRepository.findAll();
    }

    public Problema buscarPorId(Long id) {
        return problemaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Problema não encontrado"));
    }

    public Problema criar(Problema problema) {
        vincularCategoriaETecnico(problema);
        return problemaRepository.save(problema);
    }

    public Problema atualizar(Long id, Problema problemaAtualizado) {
        Problema problema = buscarPorId(id);

        problema.setTitulo(problemaAtualizado.getTitulo());
        problema.setDescricao(problemaAtualizado.getDescricao());
        problema.setDataRegistro(problemaAtualizado.getDataRegistro());
        problema.setCategoria(problemaAtualizado.getCategoria());
        problema.setTecnico(problemaAtualizado.getTecnico());

        vincularCategoriaETecnico(problema);

        return problemaRepository.save(problema);
    }

    public void deletar(Long id) {
        Problema problema = buscarPorId(id);
        problemaRepository.delete(problema);
    }

    private void vincularCategoriaETecnico(Problema problema) {
        if (problema.getCategoria() == null || problema.getCategoria().getId() == null) {
            throw new RuntimeException("Informe o ID da categoria");
        }

        if (problema.getTecnico() == null || problema.getTecnico().getId() == null) {
            throw new RuntimeException("Informe o ID do técnico");
        }

        Categoria categoria = categoriaRepository.findById(problema.getCategoria().getId())
                .orElseThrow(() -> new RuntimeException("Categoria não encontrada"));

        Tecnico tecnico = tecnicoRepository.findById(problema.getTecnico().getId())
                .orElseThrow(() -> new RuntimeException("Técnico não encontrado"));

        problema.setCategoria(categoria);
        problema.setTecnico(tecnico);
    }
}