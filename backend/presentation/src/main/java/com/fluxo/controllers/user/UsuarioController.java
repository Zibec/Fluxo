package com.fluxo.controllers.user;

import com.fluxo.config.security.SecurityFilter;
import com.fluxo.config.security.TokenService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
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

    @Autowired
    private SecurityFilter securityFilter;

    @GetMapping("/me")
    public ResponseEntity<Usuario> getUser(HttpServletRequest request){
        String token = securityFilter.recoverToken(request);
        String name = tokenService.extractUsername(token);
        Usuario usuario = service.obterPorNome(name);

        return ResponseEntity.ok(usuario);
    }

    @PostMapping("/me")
    public ResponseEntity<Usuario> changeMoedaPreferia( HttpServletRequest request, @RequestBody UserPreferencesDTO userPreferences){
        String token = securityFilter.recoverToken(request);
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
