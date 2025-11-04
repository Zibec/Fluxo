package com.fluxo.controllers.user;

import com.fluxo.config.security.TokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
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
        String token = authHeader.replace("Bearer ", "");
        String name = tokenService.extractUsername(token);
        Usuario usuario = service.obterPorNome(name);

        return ResponseEntity.ok(usuario);
    }

    @PostMapping("/me")
    public ResponseEntity<Usuario> changeMoedaPreferia( @RequestHeader("Authorization") String authHeader, @RequestBody UserPreferencesDTO userPreferences){
        String token = authHeader.replace("Bearer ", "");
        String name = tokenService.extractUsername(token);
        Usuario usuario = service.obterPorNome(name);

        service.deletar(usuario.getId());

        if(!userPreferences.formatoData().isEmpty()) {
            usuario.setFormatoDataPreferido(userPreferences.formatoData());
        }

        if(!userPreferences.moeda().isEmpty()) {
            usuario.setMoedaPreferida(userPreferences.moeda());
        }

        service.salvar(usuario);

        return ResponseEntity.ok(usuario);
    }
}
