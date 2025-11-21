package com.fluxo.controllers.patrimonio;

import com.fluxo.config.security.SecurityFilter;
import com.fluxo.config.security.TokenService;
import com.fluxo.controllers.ControllerMapper;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import patrimonio.PatrimonioService;
import patrimonio.Patrimonio;
import usuario.Usuario;
import usuario.UsuarioService;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList; // Importante
import java.util.Iterator;  // Importante
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
        if (token == null) return ResponseEntity.status(401).build();

        String name = tokenService.extractUsername(token);
        Usuario usuario = usuarioService.obterPorNome(name);

        BigDecimal valorAtual = service.calcularPatrimonioLiquido(usuario.getId());
        return ResponseEntity.ok(valorAtual);
    }

    @GetMapping("/historico")
    public ResponseEntity<List<Patrimonio>> getHistoricoEvolucao() {
        List<Patrimonio> listaOriginal = service.obterHistoricoDePatrimonio();

        List<Patrimonio> listaResposta = new ArrayList<>();
        Iterator<Patrimonio> it = listaOriginal.iterator();

        while(it.hasNext()) {
            Patrimonio p = it.next();
            listaResposta.add(p);
        }

        return ResponseEntity.ok(listaResposta);
    }

    @PostMapping("/snapshot")
    public ResponseEntity<Void> gerarSnapshotManual(HttpServletRequest request) {
        String token = securityFilter.recoverToken(request);
        if (token == null) return ResponseEntity.status(401).build();

        String name = tokenService.extractUsername(token);
        Usuario usuario = usuarioService.obterPorNome(name);

        service.gerarEsalvarSnapshot(LocalDate.now(), usuario.getId());
        return ResponseEntity.ok().build();
    }
}