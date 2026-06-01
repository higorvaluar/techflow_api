package br.unitins.techflow.service;

import br.unitins.techflow.model.Tecnico;
import br.unitins.techflow.repository.TecnicoRepository;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TecnicoService {

    private final TecnicoRepository tecnicoRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    public TecnicoService(
            TecnicoRepository tecnicoRepository,
            BCryptPasswordEncoder passwordEncoder
    ) {
        this.tecnicoRepository = tecnicoRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public List<Tecnico> listarTodos() {
        return tecnicoRepository.findAll();
    }

    public Tecnico buscarPorId(Long id) {
        return tecnicoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Técnico não encontrado"));
    }

    public Tecnico criar(Tecnico tecnico) {
        tecnico.setSenha(criptografarSenha(tecnico.getSenha()));
        tecnico.setNivelAcesso(normalizarNivel(tecnico.getNivelAcesso()));

        return tecnicoRepository.save(tecnico);
    }

    public Tecnico atualizar(Long id, Tecnico tecnicoAtualizado) {
        Tecnico tecnico = buscarPorId(id);

        tecnico.setNome(tecnicoAtualizado.getNome());
        tecnico.setEmail(tecnicoAtualizado.getEmail());
        tecnico.setNivelAcesso(normalizarNivel(tecnicoAtualizado.getNivelAcesso()));

        if (tecnicoAtualizado.getSenha() != null && !tecnicoAtualizado.getSenha().isBlank()) {
            tecnico.setSenha(criptografarSenha(tecnicoAtualizado.getSenha()));
        }

        return tecnicoRepository.save(tecnico);
    }

    public void deletar(Long id) {
        Tecnico tecnico = buscarPorId(id);
        tecnicoRepository.delete(tecnico);
    }

    private String criptografarSenha(String senha) {
        if (senha == null || senha.isBlank()) {
            throw new RuntimeException("A senha é obrigatória");
        }

        if (senhaJaEstaCriptografada(senha)) {
            return senha;
        }

        return passwordEncoder.encode(senha);
    }

    private boolean senhaJaEstaCriptografada(String senha) {
        return senha.startsWith("$2a$")
                || senha.startsWith("$2b$")
                || senha.startsWith("$2y$");
    }

    private String normalizarNivel(String nivel) {
        if (nivel == null || nivel.isBlank()) {
            return "COLABORADOR";
        }

        if (nivel.equalsIgnoreCase("Admin")) {
            return "ADMIN";
        }

        if (nivel.equalsIgnoreCase("Colaborador")) {
            return "COLABORADOR";
        }

        return nivel.toUpperCase();
    }
}