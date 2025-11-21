package com.fluxo.controllers.divida;

import com.fluxo.config.security.SecurityFilter;
import com.fluxo.config.security.TokenService;
import divida.Divida;
import divida.DividaRepositorio;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import usuario.Usuario;
import usuario.UsuarioService;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@RestController
@RequestMapping("/api/divida")
public class DividaController {

    @Autowired
    private DividaRepositorio dividaRepositorio;

    @Autowired
    private UsuarioService usuarioService;

    @Autowired
    private TokenService tokenService;

    @Autowired
    private SecurityFilter securityFilter;

    @GetMapping
    public ResponseEntity<List<Divida>> listarDividas(HttpServletRequest request) {
        String token = securityFilter.recoverToken(request);
        if (token == null) return ResponseEntity.status(401).build();

        String name = tokenService.extractUsername(token);
        Usuario usuario = usuarioService.obterPorNome(name);

        List<Divida> listaOriginal = dividaRepositorio.obterDividaPorUsuarioId(usuario.getId());

        List<Divida> listaResposta = new ArrayList<>();
        Iterator<Divida> it = listaOriginal.iterator();

        while(it.hasNext()) {
            Divida d = it.next();
            listaResposta.add(d);
        }

        return ResponseEntity.ok(listaResposta);
    }

    @PostMapping
    public ResponseEntity<Void> criarDivida(@RequestBody Divida divida, HttpServletRequest request) {
        String token = securityFilter.recoverToken(request);
        if (token == null) return ResponseEntity.status(401).build();

        String name = tokenService.extractUsername(token);
        Usuario usuario = usuarioService.obterPorNome(name);

        divida.setUsuarioId(usuario.getId());

        dividaRepositorio.salvar(divida);

        return ResponseEntity.ok().build();
    }

}