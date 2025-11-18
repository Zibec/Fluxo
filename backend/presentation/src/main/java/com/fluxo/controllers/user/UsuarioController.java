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
    public ResponseEntity<Object> getUser(HttpServletRequest request){
        String token = securityFilter.recoverToken(request);
        String name = tokenService.extractUsername(token);
        Usuario usuario = service.obterPorNome(name);

        return ResponseEntity.ok(new UserPreferencesDTO(usuario.getUsername(), usuario.getFormatoDataPreferido().getFormato(), usuario.getMoedaPreferida().getCodigo()));
    }

    @PostMapping("/preferences")
    public ResponseEntity<Object> changeMoedaPreferia( HttpServletRequest request, @RequestBody UserPreferencesDTO userPreferences){
        String token = securityFilter.recoverToken(request);
        String name = tokenService.extractUsername(token);
        Usuario usuario = service.obterPorNome(name);

        if(!userPreferences.username().equals(usuario.getUsername())){
            usuario.setUsername(userPreferences.username());
        }

        if(!userPreferences.formatoDataPreferido().isEmpty()) {
            usuario.setFormatoDataPreferido(userPreferences.formatoDataPreferido());
        }

        if(!userPreferences.moedaPreferida().isEmpty()) {
            usuario.setMoedaPreferida(userPreferences.moedaPreferida());
        }

        service.changePreferences(usuario, usuario.getPassword());

        return ResponseEntity.ok(new UserPreferencesDTO(usuario.getUsername(), usuario.getFormatoDataPreferido().getFormato(), usuario.getMoedaPreferida().getCodigo()));
    }

    @PostMapping("/alterar-email")
    public ResponseEntity<Object> changeEmail(HttpServletRequest request, @RequestBody Map<String, String> body){
        String token = securityFilter.recoverToken(request);
        String name = tokenService.extractUsername(token);
        Usuario usuario = service.obterPorNome(name);

        String newEmail = body.get("newEmail");

        service.changeEmail(usuario, usuario.getEmail(), newEmail, usuario.getPassword());

        return ResponseEntity.ok(new UserPreferencesDTO(usuario.getUsername(), usuario.getFormatoDataPreferido().getFormato(), usuario.getMoedaPreferida().getCodigo()));
    }

    @PostMapping("/alterar-senha")
    public ResponseEntity<Object> changePassword(HttpServletRequest request, @RequestBody Map<String, String> body){
        String token = securityFilter.recoverToken(request);
        String name = tokenService.extractUsername(token);
        Usuario usuario = service.obterPorNome(name);

        String oldPassword = body.get("currentPassword");
        String newPassword = body.get("newPassword");

        service.changePassword(usuario, oldPassword, newPassword);
        return ResponseEntity.ok(new UserPreferencesDTO(usuario.getUsername(), usuario.getFormatoDataPreferido().getFormato(), usuario.getMoedaPreferida().getCodigo()));
    }
}
