package br.unitins.techflow.controller;

import br.unitins.techflow.model.Tecnico;
import br.unitins.techflow.service.AutorizacaoService;
import br.unitins.techflow.service.TecnicoService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/tecnicos")
public class TecnicoController {

    private final TecnicoService tecnicoService;
    private final AutorizacaoService autorizacaoService;

    public TecnicoController(
            TecnicoService tecnicoService,
            AutorizacaoService autorizacaoService
    ) {
        this.tecnicoService = tecnicoService;
        this.autorizacaoService = autorizacaoService;
    }

    @GetMapping
    public ResponseEntity<List<Tecnico>> listarTodos() {
        return ResponseEntity.ok(tecnicoService.listarTodos());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Tecnico> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(tecnicoService.buscarPorId(id));
    }

    @PostMapping
    public ResponseEntity<Tecnico> criar(
            @RequestHeader(value = "Authorization", required = false) String authorization,
            @Valid @RequestBody Tecnico tecnico
    ) {
        autorizacaoService.exigirAdmin(authorization);
        return ResponseEntity.ok(tecnicoService.criar(tecnico));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Tecnico> atualizar(
            @RequestHeader(value = "Authorization", required = false) String authorization,
            @PathVariable Long id,
            @RequestBody Tecnico tecnico
    ) {
        autorizacaoService.exigirAdmin(authorization);
        return ResponseEntity.ok(tecnicoService.atualizar(id, tecnico));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(
            @RequestHeader(value = "Authorization", required = false) String authorization,
            @PathVariable Long id
    ) {
        autorizacaoService.exigirAdmin(authorization);
        tecnicoService.deletar(id);
        return ResponseEntity.noContent().build();
    }
}