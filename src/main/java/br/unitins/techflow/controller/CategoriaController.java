package br.unitins.techflow.controller;

import br.unitins.techflow.model.Categoria;
import br.unitins.techflow.service.AutorizacaoService;
import br.unitins.techflow.service.CategoriaService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/categorias")
public class CategoriaController {

    private final CategoriaService categoriaService;
    private final AutorizacaoService autorizacaoService;

    public CategoriaController(
            CategoriaService categoriaService,
            AutorizacaoService autorizacaoService
    ) {
        this.categoriaService = categoriaService;
        this.autorizacaoService = autorizacaoService;
    }

    @GetMapping
    public ResponseEntity<List<Categoria>> listarTodas() {
        return ResponseEntity.ok(categoriaService.listarTodas());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Categoria> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(categoriaService.buscarPorId(id));
    }

    @PostMapping
    public ResponseEntity<Categoria> criar(
            @RequestHeader(value = "Authorization", required = false) String authorization,
            @Valid @RequestBody Categoria categoria
    ) {
        autorizacaoService.exigirAdmin(authorization);
        return ResponseEntity.ok(categoriaService.criar(categoria));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Categoria> atualizar(
            @RequestHeader(value = "Authorization", required = false) String authorization,
            @PathVariable Long id,
            @Valid @RequestBody Categoria categoria
    ) {
        autorizacaoService.exigirAdmin(authorization);
        return ResponseEntity.ok(categoriaService.atualizar(id, categoria));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(
            @RequestHeader(value = "Authorization", required = false) String authorization,
            @PathVariable Long id
    ) {
        autorizacaoService.exigirAdmin(authorization);
        categoriaService.deletar(id);
        return ResponseEntity.noContent().build();
    }
}