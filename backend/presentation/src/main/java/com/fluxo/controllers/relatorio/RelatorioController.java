package com.fluxo.controllers.relatorio;

import analise.RelatoriosService;
import cartao.CartaoService;
import com.fluxo.config.security.SecurityFilter;
import com.fluxo.config.security.TokenService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import usuario.Usuario;
import usuario.UsuarioService;

import java.math.BigDecimal;

@RestController
@RequestMapping("/api/relatorios")
public class RelatorioController {

    @Autowired
    private RelatoriosService relatoriosService;

    @Autowired
    private UsuarioService usuarioService;

    @Autowired
    private TokenService tokenService;

    @Autowired
    private SecurityFilter securityFilter;

    @GetMapping("/dinheiro-total")
    public ResponseEntity<Object> getDinheiroTotal(HttpServletRequest request) {
        try {
            String token = securityFilter.recoverToken(request);
            String name = tokenService.extractUsername(token);
            Usuario usuario = usuarioService.obterPorNome(name);

            BigDecimal valorTotal = relatoriosService.calcularDinheiroTotal(usuario.getId());
            return ResponseEntity.ok(valorTotal);
        } catch(Exception ex) {
            System.out.println(ex.getMessage());
            return ResponseEntity.badRequest().body(ex.getMessage());
        }
    }
}
