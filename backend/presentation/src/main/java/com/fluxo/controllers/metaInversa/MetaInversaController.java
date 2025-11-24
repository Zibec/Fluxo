package com.fluxo.controllers.metaInversa;

import com.fluxo.config.security.SecurityFilter;
import com.fluxo.config.security.TokenService;
import jakarta.servlet.http.HttpServletRequest;
import metaInversa.MetaInversa;
import metaInversa.MetaInversaService;

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
@RequestMapping("/api/metas-quitacao")
public class MetaInversaController {

    private final MetaInversaService metaInversaService;

    @Autowired
    private UsuarioService usuarioService;

    @Autowired
    private TokenService tokenService;

    @Autowired
    private SecurityFilter securityFilter;

    public MetaInversaController(MetaInversaService metaInversaService) {
        this.metaInversaService = metaInversaService;
    }

    @PostMapping
    public ResponseEntity<Void> criar(@RequestBody MetaInversa meta, HttpServletRequest request) {
        String token = securityFilter.recoverToken(request);
        String name = tokenService.extractUsername(token);
        Usuario usuario = usuarioService.obterPorNome(name);

        meta.setUsuarioId(usuario.getId());
        metaInversaService.salvar(meta);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/id/{id}")
    public ResponseEntity<MetaInversa> buscarPorId(@PathVariable String id) {
        Optional<MetaInversa> meta = metaInversaService.obterOptional(id);
        return meta.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/by-user")
    public ResponseEntity<List<MetaInversa>> buscarPorUser(HttpServletRequest request) {
        String token = securityFilter.recoverToken(request);
        String name = tokenService.extractUsername(token);
        Usuario usuario = usuarioService.obterPorNome(name);

        List<MetaInversa> meta = metaInversaService.obterPorUsuario(usuario.getId());
        return ResponseEntity.ok(meta);
    }

    // Buscar meta por nome
    @GetMapping("/nome/{nome}")
    public ResponseEntity<MetaInversa> buscarPorNome(@PathVariable String nome) {
        Optional<MetaInversa> meta = metaInversaService.buscarPorNomeOptional(nome);
        return meta.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }


    // Atualizar meta (ex: descrição, valor alvo, prazo)
    @PutMapping("/{id}")
    public ResponseEntity<Void> atualizar(@PathVariable String id, @RequestBody MetaInversa novaMeta,  HttpServletRequest request) {
        var existente = metaInversaService.buscar(id);
        if (existente == null) return ResponseEntity.notFound().build();

        String token = securityFilter.recoverToken(request);
        String name = tokenService.extractUsername(token);
        Usuario usuario = usuarioService.obterPorNome(name);

        metaInversaService.deletar(id);

        novaMeta.setUsuarioId(usuario.getId());
        novaMeta.setId(id);
        novaMeta.setValorAmortizado(existente.getValorAmortizado());
        metaInversaService.salvar(novaMeta);

        return ResponseEntity.ok().build();
    }

    // Deletar meta
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable String id) {
        try {
            metaInversaService.deletar(id);
            return ResponseEntity.noContent().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/{id}/aporte")
    public ResponseEntity<String> realizarAporte(@PathVariable String id, @RequestBody Map<String, Object> body) {
        try {
            metaInversaService.realizarAporte(id, BigDecimal.valueOf((Integer) body.get("valor")));
            return ResponseEntity.ok("Aporte de R$ " + body.get("valor") + " realizado na meta " + id);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
