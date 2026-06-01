package br.unitins.techflow.controller;

import br.unitins.techflow.model.Tecnico;
import br.unitins.techflow.repository.TecnicoRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final TecnicoRepository tecnicoRepository;

    public AuthController(TecnicoRepository tecnicoRepository) {
        this.tecnicoRepository = tecnicoRepository;
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest request) {
        Tecnico tecnico = tecnicoRepository.findByEmailIgnoreCase(request.email())
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.UNAUTHORIZED,
                        "E-mail ou senha inválidos"
                ));

        if (!tecnico.getSenha().equals(request.senha())) {
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