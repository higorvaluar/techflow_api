package br.unitins.techflow.controller;

import br.unitins.techflow.model.Tecnico;
import br.unitins.techflow.service.TecnicoService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/tecnicos")
public class TecnicoController {

    private final TecnicoService tecnicoService;

    public TecnicoController(TecnicoService tecnicoService) {
        this.tecnicoService = tecnicoService;
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
    public ResponseEntity<Tecnico> criar(@Valid @RequestBody Tecnico tecnico) {
        return ResponseEntity.ok(tecnicoService.criar(tecnico));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Tecnico> atualizar(
            @PathVariable Long id,
            @Valid @RequestBody Tecnico tecnico
    ) {
        return ResponseEntity.ok(tecnicoService.atualizar(id, tecnico));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        tecnicoService.deletar(id);
        return ResponseEntity.noContent().build();
    }
}