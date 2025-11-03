package com.fluxo.controllers.user;

import com.fluxo.config.security.TokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import usuario.Usuario;
import usuario.UsuarioService;

@RestController
@RequestMapping("/api/user")
public class UsuarioController {

    @Autowired
    private UsuarioService service;

    @Autowired
    private TokenService tokenService;

    @GetMapping("/me")
    public ResponseEntity<Usuario> getUser( @RequestHeader("Authorization") String authHeader){
        System.out.println("teste");

        String token = authHeader.replace("Bearer ", "");

        System.out.println(token);

        String name = tokenService.extractUsername(token);

        System.out.println(name);

        Usuario usuario = service.obterPorNome(name);

        System.out.println(usuario);

        return ResponseEntity.ok(usuario);
    }
}
