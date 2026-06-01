package br.unitins.techflow.controller;

import br.unitins.techflow.model.Tecnico;
import br.unitins.techflow.repository.TecnicoRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final TecnicoRepository tecnicoRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    public AuthController(
            TecnicoRepository tecnicoRepository,
            BCryptPasswordEncoder passwordEncoder
    ) {
        this.tecnicoRepository = tecnicoRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest request) {
        Tecnico tecnico = tecnicoRepository.findByEmailIgnoreCase(request.email())
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.UNAUTHORIZED,
                        "E-mail ou senha inválidos"
                ));

        if (!senhaConfere(request.senha(), tecnico)) {
            throw new ResponseStatusException(
                    HttpStatus.UNAUTHORIZED,
                    "E-mail ou senha inválidos"
            );
        }

        UsuarioResponse usuario = new UsuarioResponse(
                tecnico.getId(),
                tecnico.getNome(),
                tecnico.getEmail(),
                normalizarNivel(tecnico.getNivelAcesso())
        );

        LoginResponse response = new LoginResponse(
                "token-demo-" + tecnico.getId(),
                usuario
        );

        return ResponseEntity.ok(response);
    }

    private boolean senhaConfere(String senhaInformada, Tecnico tecnico) {
        if (senhaInformada == null || senhaInformada.isBlank()) {
            return false;
        }

        String senhaBanco = tecnico.getSenha();

        if (senhaBanco == null || senhaBanco.isBlank()) {
            return false;
        }

        if (senhaJaEstaCriptografada(senhaBanco)) {
            return passwordEncoder.matches(senhaInformada, senhaBanco);
        }

        boolean senhaAntigaConfere = senhaBanco.equals(senhaInformada);

        if (senhaAntigaConfere) {
            tecnico.setSenha(passwordEncoder.encode(senhaInformada));
            tecnicoRepository.save(tecnico);
        }

        return senhaAntigaConfere;
    }

    private boolean senhaJaEstaCriptografada(String senha) {
        return senha.startsWith("$2a$")
                || senha.startsWith("$2b$")
                || senha.startsWith("$2y$");
    }

    private String normalizarNivel(String nivelAcesso) {
        if (nivelAcesso == null) {
            return "Colaborador";
        }

        if (nivelAcesso.equalsIgnoreCase("ADMIN")) {
            return "Admin";
        }

        if (nivelAcesso.equalsIgnoreCase("COLABORADOR")) {
            return "Colaborador";
        }

        return nivelAcesso;
    }

    public record LoginRequest(
            String email,
            String senha
    ) {
    }

    public record LoginResponse(
            String token,
            UsuarioResponse usuario
    ) {
    }

    public record UsuarioResponse(
            Long id,
            String nome,
            String email,
            String nivel
    ) {
    }
}