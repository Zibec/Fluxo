package com.fluxo.controllers.perfil;

import com.fluxo.config.security.SecurityFilter;
import com.fluxo.config.security.TokenService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import perfil.Perfil;
import perfil.PerfilService;
import usuario.Usuario;
import usuario.UsuarioService;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/perfis")
public class PerfilController {

    @Autowired
    private PerfilService perfilService;

    @Autowired
    private UsuarioService usuarioService;

    @Autowired
    private TokenService tokenService;

    @Autowired
    private SecurityFilter securityFilter;

    @GetMapping("/")
    private ResponseEntity<List<Perfil>> getAllPerfis(HttpServletRequest request) {
        String token = securityFilter.recoverToken(request);
        String name = tokenService.extractUsername(token);
        Usuario usuario = usuarioService.obterPorNome(name);

        return ResponseEntity.ok(perfilService.obterTodosPerfisPorUsuarioId(usuario.getId()));
    }

    @GetMapping("/{id}")
    private ResponseEntity<Perfil> getPerfil(@PathVariable String id){
        return ResponseEntity.ok(perfilService.obterPerfil(id));
    }

    @PostMapping("/")
    private ResponseEntity<Perfil> salvarPefil(@RequestBody Perfil perfil,  HttpServletRequest request){
        String token = securityFilter.recoverToken(request);
        String name = tokenService.extractUsername(token);
        Usuario usuario = usuarioService.obterPorNome(name);

        try {
            perfil.setUsuarioId(usuario.getId());
            perfilService.salvarPerfil(perfil);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return ResponseEntity.badRequest().build();
        }
    }

    @PutMapping("/{id}")
    private ResponseEntity<Perfil> editarPerfil(@PathVariable String id, @RequestBody Perfil perfil, HttpServletRequest request){
        String token = securityFilter.recoverToken(request);
        String name = tokenService.extractUsername(token);
        Usuario usuario = usuarioService.obterPorNome(name);
        perfil.setUsuarioId(usuario.getId());

        perfilService.alterarPerfil(id, perfil);
        return ResponseEntity.ok().build();
    }
}
