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
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.fluxo.config.security.SecurityFilter;
import com.fluxo.config.security.TokenService;

import cartao.Cartao;
import cartao.CartaoId;
import cartao.CartaoNumero;
import cartao.CartaoService;
import conta.ContaService;
import jakarta.servlet.http.HttpServletRequest;
import meta.Meta;
import transacao.FormaPagamentoId;
import transacao.Transacao;
import transacao.TransacaoService;
import usuario.Usuario;
import usuario.UsuarioService;

public class TransacaoController {
    private final TransacaoService transacaoService;
    
    @Autowired
    private ContaService contaService;

    @Autowired
    private CartaoService cartaoService;

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

    
    @GetMapping("/by-conta")
    public ResponseEntity<List<Transacao>> buscarPorUser(HttpServletRequest request) {
        String token = securityFilter.recoverToken(request);
        String name = tokenService.extractUsername(token);
        Usuario usuario = usuarioService.obterPorNome(name);

        if(transacaoService.obterPorConta(usuario.getId()).isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        List<Transacao> transacao = transacaoService.obterPorConta(usuario.getId());
        return ResponseEntity.ok(transacao);
    }
    
    @PostMapping
    public ResponseEntity<Void> criar(@RequestBody Transacao transacao, HttpServletRequest request) throws Exception {
        String token = securityFilter.recoverToken(request);
        String name = tokenService.extractUsername(token);
        FormaPagamentoId formaPagamentoId;

        if(cartaoService.obterPorId(new CartaoId(transacao.getPagamentoId().getId())) != null) {
            formaPagamentoId = transacao.getPagamentoId();

        } else if(contaService.obter(transacao.getPagamentoId().getId()) != null) {
            formaPagamentoId = transacao.getPagamentoId();
        }

        transacaoService.salvarTransacao(transacao);

        return ResponseEntity.ok().build();
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<Void> atualizar(@PathVariable String id, @RequestBody Transacao transacao,  HttpServletRequest request) {
        var existente = transacaoService.obterTransacaoPorId(id);
        if (existente.isEmpty()) return ResponseEntity.notFound().build();

        String token = securityFilter.recoverToken(request);
        String name = tokenService.extractUsername(token);
        Usuario usuario = usuarioService.obterPorNome(name);
        FormaPagamentoId formaPagamentoId = null;

        transacaoService.excluirTransacao(id);

        if(cartaoService.obterPorId(new CartaoId(transacao.getPagamentoId().getId())) != null) {
            formaPagamentoId = cartaoService.obterPorId(new CartaoId(transacao.getPagamentoId().getId())).getId();

        } else if(contaService.obter(transacao.getPagamentoId().getId()) != null) {
            formaPagamentoId = contaService.obter(transacao.getPagamentoId().getId()).orElse(null).getId();
        }

        transacao.setPagamentoId(formaPagamentoId);
        transacaoService.salvarTransacao(transacao);

        return ResponseEntity.ok().build();
    }

}
