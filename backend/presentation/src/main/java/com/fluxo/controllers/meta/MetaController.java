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

        if(metaService.obterPorUsuario(usuario.getId()).isEmpty()) {
            return ResponseEntity.notFound().build();
        }

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
        var existente = metaService.obter(id);
        if (existente.isEmpty()) return ResponseEntity.notFound().build();

        String token = securityFilter.recoverToken(request);
        String name = tokenService.extractUsername(token);
        Usuario usuario = usuarioService.obterPorNome(name);

        metaService.deletar(id);

        novaMeta.setUsuarioId(usuario.getId());
        novaMeta.setId(usuario.getId());
        novaMeta.setSaldoAcumulado(novaMeta.getSaldoAcumulado());
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
