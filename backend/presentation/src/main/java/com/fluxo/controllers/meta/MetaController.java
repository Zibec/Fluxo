package com.fluxo.controllers.meta;

import conta.Conta;
import meta.Meta;
import meta.MetaService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/metas-poupanca")
public class MetaController {

    private final MetaService metaService;

    public MetaController(MetaService metaService) {
        this.metaService = metaService;
    }

    // Criar meta
    @PostMapping
    public ResponseEntity<Void> criar(@RequestBody Meta meta) {
        metaService.salvar(meta);
        return ResponseEntity.ok().build();
    }

    // Buscar meta por ID
    @GetMapping("/id/{id}")
    public ResponseEntity<Meta> buscarPorId(@PathVariable String id) {
        Optional<Meta> meta = metaService.obter(id);
        return meta.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    // Buscar meta por nome
    @GetMapping("/nome/{nome}")
    public ResponseEntity<Meta> buscarPorNome(@PathVariable String nome) {
        Optional<Meta> meta = metaService.obterPorNome(nome);
        return meta.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    // Listar todas as metas
    @GetMapping
    public ResponseEntity<List<Meta>> listarTodas() {
        var metas = metaService.listar();
        if (metas.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(metas);
    }

    // Atualizar meta (ex: descrição, valor alvo, prazo)
    @PutMapping("/{id}")
    public ResponseEntity<Void> atualizar(@PathVariable String id, @RequestBody Meta novaMeta) {
        var existente = metaService.obter(id);
        if (existente.isEmpty()) return ResponseEntity.notFound().build();

        var meta = existente.get();
        meta.setDescricao(novaMeta.getDescricao());
        meta.setValorAlvo(novaMeta.getValorAlvo());
        meta.setPrazo(novaMeta.getPrazo());
        metaService.salvar(meta);

        return ResponseEntity.ok().build();
    }

    // Deletar meta
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable String id) {
        try {
            metaService.deletar(id);
            return ResponseEntity.noContent().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/{id}/aporte")
    public ResponseEntity<String> realizarAporte(
            @PathVariable String id,
            @RequestParam BigDecimal valor,
            @RequestParam String contaId) {
        try {
            metaService.realizarAporte(id, valor, contaId);
            return ResponseEntity.ok("Aporte de R$ " + valor + " realizado na meta " + id);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
