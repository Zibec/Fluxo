package com.fluxo.controllers.categorias;

import categoria.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import persistencia.jpa.categoria.CategoriaRepository;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/categoria")
public class CategoriaController {
    @Autowired
    private CategoriaRepository categoriaRepository;

    @Autowired
    private CategoriaService categoriaService;

    @GetMapping
    public ResponseEntity<List<Categoria>> listarTodas() {
        var categorias = categoriaService.listar();

        if (categorias.isEmpty()) {
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.ok(categorias);
    }

    @PostMapping
    public ResponseEntity<Void> criar(@RequestBody Categoria categoria) {
        categoriaService.salvar(categoria);
        return ResponseEntity.ok().build();
    }

    @GetMapping("id/{id}")
    public ResponseEntity<Categoria> buscarPorId(@PathVariable String id) {
        var categoriaOpt = categoriaService.obterCategoria(id);

        return categoriaOpt
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("nome/{nome}")
    public ResponseEntity<Categoria> buscarPorNome(@PathVariable String nome) {
        Optional<Categoria> categoria = categoriaService.obterPorNome(nome);
        return categoria.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable String id) {
        try {
            categoriaService.deletar(id);
            return ResponseEntity.noContent().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        } catch (IllegalStateException e) {
            return ResponseEntity.badRequest().body(null);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> atualizar(@PathVariable String id, @RequestBody Categoria categoriaAtualizada) {
        var categoriaOpt = categoriaService.obterCategoria(id);

        if (categoriaOpt.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        var categoria = categoriaOpt.get();
        categoria.setId(id);
        categoria.setNome(categoriaAtualizada.getNome());
        categoriaService.salvar(categoria);

        return ResponseEntity.ok().build();
    }
}
