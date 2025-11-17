package com.fluxo.controllers.user;

import com.fluxo.config.security.SecurityFilter;
import com.fluxo.config.security.TokenService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;
import usuario.Email;
import usuario.Usuario;
import usuario.UsuarioService;

import java.util.Map;

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

    @PostMapping("/preferences")
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

    @PostMapping("/change-email")
    public ResponseEntity<Usuario> changeEmail(HttpServletRequest request, @RequestBody Map<String, String> body){
        String token = securityFilter.recoverToken(request);
        String name = tokenService.extractUsername(token);
        Usuario usuario = service.obterPorNome(name);

        service.deletar(usuario.getId());

        usuario.setEmail(new Email(body.get("newEmail")));

        service.salvar(usuario);

        return ResponseEntity.ok(usuario);
    }

    @PostMapping("/change-password")
    public ResponseEntity<Usuario> changePassword(HttpServletRequest request, @RequestBody Map<String, String> body){
        String token = securityFilter.recoverToken(request);
        String name = tokenService.extractUsername(token);
        Usuario usuario = service.obterPorNome(name);

        service.deletar(usuario.getId());

        String encryptedPassword = new BCryptPasswordEncoder().encode(body.get("newPassword"));
        usuario.setPassword(encryptedPassword);

        service.salvar(usuario);

        return ResponseEntity.ok(usuario);
    }
}
