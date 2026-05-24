package br.unitins.techflow.controller;

import br.unitins.techflow.model.Problema;
import br.unitins.techflow.service.ProblemaService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/problemas")
public class ProblemaController {

    private final ProblemaService problemaService;

    public ProblemaController(ProblemaService problemaService) {
        this.problemaService = problemaService;
    }

    @GetMapping
    public ResponseEntity<List<Problema>> listarTodos() {
        return ResponseEntity.ok(problemaService.listarTodos());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Problema> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(problemaService.buscarPorId(id));
    }

    @PostMapping
    public ResponseEntity<Problema> criar(@Valid @RequestBody Problema problema) {
        return ResponseEntity.ok(problemaService.criar(problema));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Problema> atualizar(
            @PathVariable Long id,
            @Valid @RequestBody Problema problema
    ) {
        return ResponseEntity.ok(problemaService.atualizar(id, problema));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        problemaService.deletar(id);
        return ResponseEntity.noContent().build();
    }
}