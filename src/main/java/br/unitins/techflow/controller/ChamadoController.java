package br.unitins.techflow.controller;

import br.unitins.techflow.model.Chamado;
import br.unitins.techflow.service.ChamadoService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/chamados")
public class ChamadoController {

    private final ChamadoService chamadoService;

    public ChamadoController(ChamadoService chamadoService) {
        this.chamadoService = chamadoService;
    }

    @GetMapping
    public ResponseEntity<List<Chamado>> listarTodos() {
        return ResponseEntity.ok(chamadoService.listarTodos());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Chamado> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(chamadoService.buscarPorId(id));
    }

    @PostMapping
    public ResponseEntity<Chamado> criar(@Valid @RequestBody Chamado chamado) {
        return ResponseEntity.ok(chamadoService.criar(chamado));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Chamado> atualizar(
            @PathVariable Long id,
            @Valid @RequestBody Chamado chamado
    ) {
        return ResponseEntity.ok(chamadoService.atualizar(id, chamado));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        chamadoService.deletar(id);
        return ResponseEntity.noContent().build();
    }
}