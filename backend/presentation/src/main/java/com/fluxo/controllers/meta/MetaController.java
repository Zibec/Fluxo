package com.fluxo.controllers.meta;

import com.fluxo.config.security.SecurityFilter;
import com.fluxo.config.security.TokenService;
import jakarta.servlet.http.HttpServletRequest;
import meta.Meta;
import meta.MetaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import usuario.Usuario;
import usuario.UsuarioService;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/metas-poupanca")
public class MetaController {

    private final MetaService metaService;

    @Autowired
    private UsuarioService usuarioService;

    @Autowired
    private TokenService tokenService;

    @Autowired
    private SecurityFilter securityFilter;

    public MetaController(MetaService metaService) {
        this.metaService = metaService;
    }

    @PostMapping
    public ResponseEntity<Void> criar(@RequestBody Meta meta, HttpServletRequest request) {
        String token = securityFilter.recoverToken(request);
        String name = tokenService.extractUsername(token);
        Usuario usuario = usuarioService.obterPorNome(name);

        meta.setUsuarioId(usuario.getId());
        metaService.salvar(meta);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/id/{id}")
    public ResponseEntity<Meta> buscarPorId(@PathVariable String id) {
        Optional<Meta> meta = metaService.obter(id);
        return meta.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/by-user")
    public ResponseEntity<List<Meta>> buscarPorUser(HttpServletRequest request) {
        String token = securityFilter.recoverToken(request);
        String name = tokenService.extractUsername(token);
        Usuario usuario = usuarioService.obterPorNome(name);

        List<Meta> meta = metaService.obterPorUsuario(usuario.getId());
        return ResponseEntity.ok(meta);
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
    public ResponseEntity<Void> atualizar(@PathVariable String id, @RequestBody Meta novaMeta,  HttpServletRequest request) {
        var existenteOptional = metaService.obter(id);
        if (existenteOptional.isEmpty()) return ResponseEntity.notFound().build();

        Meta existente = existenteOptional.get(); // Pega a meta atual

        String token = securityFilter.recoverToken(request);
        String name = tokenService.extractUsername(token);
        Usuario usuario = usuarioService.obterPorNome(name);

        novaMeta.setId(id);
        novaMeta.setUsuarioId(usuario.getId());

        if (novaMeta.getSaldoAcumulado() == null){
            novaMeta.setSaldoAcumulado(existente.getSaldoAcumulado());
        }

        metaService.deletar(id);

        metaService.salvar(novaMeta);

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
            @RequestBody Map<String, Object> body) {
        try {
            metaService.realizarAporte(id, BigDecimal.valueOf((Integer) body.get("valor")), (String) body.get("contaId"));
            return ResponseEntity.ok("Aporte de R$ " + body.get("valor") + " realizado na meta " + id);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
