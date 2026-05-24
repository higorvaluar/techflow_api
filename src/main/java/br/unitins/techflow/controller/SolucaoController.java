package br.unitins.techflow.controller;

import br.unitins.techflow.model.Solucao;
import br.unitins.techflow.service.SolucaoService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/solucoes")
public class SolucaoController {

    private final SolucaoService solucaoService;

    public SolucaoController(SolucaoService solucaoService) {
        this.solucaoService = solucaoService;
    }

    @GetMapping
    public ResponseEntity<List<Solucao>> listarTodas() {
        return ResponseEntity.ok(solucaoService.listarTodas());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Solucao> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(solucaoService.buscarPorId(id));
    }

    @PostMapping
    public ResponseEntity<Solucao> criar(@Valid @RequestBody Solucao solucao) {
        return ResponseEntity.ok(solucaoService.criar(solucao));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Solucao> atualizar(
            @PathVariable Long id,
            @Valid @RequestBody Solucao solucao
    ) {
        return ResponseEntity.ok(solucaoService.atualizar(id, solucao));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        solucaoService.deletar(id);
        return ResponseEntity.noContent().build();
    }
}