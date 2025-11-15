package com.fluxo.controllers.transacao;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.fluxo.config.security.SecurityFilter;
import com.fluxo.config.security.TokenService;

import jakarta.servlet.http.HttpServletRequest;
import transacao.Transacao;
import transacao.TransacaoService;
import usuario.Usuario;
import usuario.UsuarioService;

public class TransacaoController {
    private final TransacaoService transacaoService;

    @Autowired
    private UsuarioService usuarioService;

    @Autowired
    private TokenService tokenService;

    @Autowired
    private SecurityFilter securityFilter;

    public TransacaoController(TransacaoService transacaoService) {
        this.transacaoService = transacaoService;
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable String id) {
        try {
            transacaoService.excluirTransacao(id);
            return ResponseEntity.noContent().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping
    public ResponseEntity<List<Transacao>> listarTodas() {
        var transacoes = transacaoService.listarTodasTransacoes();
        if (transacoes.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(transacoes);
    }

    @GetMapping("/agendamento-data/{agendamento}/{data}")
    public ResponseEntity<Transacao> buscarPorNome(@PathVariable String agendamentoId, @PathVariable LocalDate data) {
        Optional<Transacao> transacao = transacaoService.encontrarTransacaoPorAgendamentoEData(agendamentoId, data);
        return transacao.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    /*
    @GetMapping("/by-user")
    public ResponseEntity<List<Meta>> buscarPorUser(HttpServletRequest request) {
        
    }

    @PostMapping
    public ResponseEntity<Void> criar(@RequestBody Transacao transacao, HttpServletRequest request) {

    }
    */
}
