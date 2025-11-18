package com.fluxo.controllers.patrimonio;

import com.fluxo.config.security.SecurityFilter;
import com.fluxo.config.security.TokenService;
import com.fluxo.controllers.ControllerMapper;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

// --- IMPORTS CORRIGIDOS ---
import patrimonio.PatrimonioService;
import patrimonio.Patrimonio; // Usando a classe correta que está no seu Service
import usuario.Usuario;
import usuario.UsuarioService;
// --------------------------

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/patrimonio")
public class PatrimonioController {

    @Autowired
    private PatrimonioService service;

    @Autowired
    private ControllerMapper mapper;

    @Autowired
    private UsuarioService usuarioService;

    @Autowired
    private TokenService tokenService;

    @Autowired
    private SecurityFilter securityFilter;

    @GetMapping("/atual")
    public ResponseEntity<BigDecimal> getPatrimonioAtual(HttpServletRequest request) {
        String token = securityFilter.recoverToken(request);
        String name = tokenService.extractUsername(token);
        Usuario usuario = usuarioService.obterPorNome(name);

        BigDecimal valorAtual = service.calcularPatrimonioLiquido(usuario.getId());
        return ResponseEntity.ok(valorAtual);
    }

    // Ajustado para retornar List<Patrimonio>
    @GetMapping("/historico")
    public ResponseEntity<List<Patrimonio>> getHistoricoEvolucao() {
        List<Patrimonio> historico = service.obterHistoricoDePatrimonio();
        return ResponseEntity.ok(historico);
    }

    @PostMapping("/snapshot")
    public ResponseEntity<Void> gerarSnapshotManual(HttpServletRequest request) {
        String token = securityFilter.recoverToken(request);
        String name = tokenService.extractUsername(token);
        Usuario usuario = usuarioService.obterPorNome(name);

        service.gerarEsalvarSnapshot(LocalDate.now(), usuario.getId());
        return ResponseEntity.ok().build();
    }
}