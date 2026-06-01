package br.unitins.techflow.service;

import br.unitins.techflow.model.Tecnico;
import br.unitins.techflow.repository.TecnicoRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
public class AutorizacaoService {

    private final TecnicoRepository tecnicoRepository;

    public AutorizacaoService(TecnicoRepository tecnicoRepository) {
        this.tecnicoRepository = tecnicoRepository;
    }

    public Tecnico obterTecnicoLogado(String authorizationHeader) {
        if (authorizationHeader == null || authorizationHeader.isBlank()) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Token não informado");
        }

        if (!authorizationHeader.startsWith("Bearer ")) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Token inválido");
        }

        String token = authorizationHeader.replace("Bearer ", "").trim();

        if (!token.startsWith("token-demo-")) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Token inválido");
        }

        String idTexto = token.replace("token-demo-", "").trim();

        Long idTecnico;

        try {
            idTecnico = Long.parseLong(idTexto);
        } catch (NumberFormatException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Token inválido");
        }

        return tecnicoRepository.findById(idTecnico)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.UNAUTHORIZED,
                        "Usuário do token não encontrado"
                ));
    }

    public void exigirAdmin(String authorizationHeader) {
        Tecnico tecnico = obterTecnicoLogado(authorizationHeader);

        if (tecnico.getNivelAcesso() == null ||
                !tecnico.getNivelAcesso().equalsIgnoreCase("ADMIN")) {
            throw new ResponseStatusException(
                    HttpStatus.FORBIDDEN,
                    "Acesso negado. Apenas administradores podem realizar esta ação."
            );
        }
    }
}