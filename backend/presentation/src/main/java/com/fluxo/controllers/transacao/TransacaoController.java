package com.fluxo.controllers.transacao;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fluxo.config.security.SecurityFilter;
import com.fluxo.config.security.TokenService;

import cartao.CartaoId;
import cartao.CartaoService;
import conta.ContaId;
import conta.ContaService;
import jakarta.servlet.http.HttpServletRequest;
import transacao.FormaPagamentoId;
import transacao.StatusTransacao;
import transacao.Tipo;
import transacao.Transacao;
import transacao.TransacaoService;
import usuario.Usuario;
import usuario.UsuarioService;


@RestController
@RequestMapping("/api/transacao")
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
        var transacoesOriginal = transacaoService.listarTodasTransacoes();

        if (transacoesOriginal.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        List<Transacao> listaResposta = new ArrayList<>();
        Iterator<Transacao> it = transacoesOriginal.iterator();

        while(it.hasNext()) {
            Transacao t = it.next();
            listaResposta.add(t);
        }

        return ResponseEntity.ok(listaResposta);
    }

    @GetMapping("/agendamento-data/{agendamentoId}/{data}")
    public ResponseEntity<Transacao> buscarPorNome(@PathVariable String agendamentoId, @PathVariable LocalDate data) {
        Optional<Transacao> transacao = transacaoService.encontrarTransacaoPorAgendamentoEData(agendamentoId, data);
        return transacao.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Transacao> buscarPorId(@PathVariable String id) {
        Optional<Transacao> transacao = transacaoService.obterTransacaoPorId(id);
        return transacao.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    
    @GetMapping("/by-user")
    public ResponseEntity<List<Transacao>> buscarPorUser(HttpServletRequest request)    {
        String token = securityFilter.recoverToken(request);
        String name = tokenService.extractUsername(token);
        Usuario usuario = usuarioService.obterPorNome(name);

        List<Transacao> transacao = transacaoService.obterPorConta(usuario.getId());
        return ResponseEntity.ok(transacao);
    }
    
    @PostMapping
    public ResponseEntity<Void> criar(@RequestBody TransacaoDTO dto, HttpServletRequest request) {
        String token = securityFilter.recoverToken(request);
        String name = tokenService.extractUsername(token);
        Usuario usuario = usuarioService.obterPorNome(name);

        //receber formaPagamentoId como String
        String formaPagamentoId = dto.pagamentoId();

        //resolver a classe concreta
        FormaPagamentoId pagamentoId;

        if (cartaoService.obterPorId(formaPagamentoId) != null) {
            pagamentoId = new CartaoId(formaPagamentoId);
            pagamentoId.setType("CARTAO");
        } 
        else if (contaService.obter(formaPagamentoId).isPresent()) {
            pagamentoId = new ContaId(formaPagamentoId);
            pagamentoId.setType("CONTA");
        }
        else {
            throw new IllegalArgumentException("Forma de pagamento não encontrada.");
        }

        //Instanciar a transação usando o construtor correto
        Transacao transacao;

        if (dto.categoriaId() != null) {
            //Construtor com categoria
            transacao = new Transacao(
                    UUID.randomUUID().toString(),
                dto.origemAgendamentoId(),
                dto.descricao(),
                dto.valor(),
                dto.data(),
                StatusTransacao.valueOf(dto.status()),
                dto.categoriaId(),
                pagamentoId,
                dto.avulsa(),
                Tipo.valueOf(dto.tipo()),
                dto.perfilId()
            );
        } else {
            //Construtor sem categoria
            transacao = new Transacao(
                    UUID.randomUUID().toString(),
                dto.origemAgendamentoId(),
                dto.descricao(),
                dto.valor(),
                dto.data(),
                StatusTransacao.valueOf(dto.status()),
                pagamentoId,
                dto.avulsa(),
                Tipo.valueOf(dto.tipo()),
                dto.perfilId()
            );
        }

        transacao.setUsuarioId(usuario.getId());
        //Salvar
        transacaoService.salvarTransacao(transacao);

        if(transacao.isAvulsa()) {
            transacaoService.efetivarTransacao(transacao.getId());
        }


        return ResponseEntity.ok().build();
    }

    @PostMapping("/{id}/efetivar")
    public ResponseEntity<Void> efetivar(@PathVariable String id) {
        transacaoService.efetivarTransacao(id);
        return ResponseEntity.ok().build();
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<Void> atualizar(
            @PathVariable String id,
            @RequestBody TransacaoDTO dto,
            HttpServletRequest request
    ) throws Exception {

        //Validar usuário pelo token
        String token = securityFilter.recoverToken(request);
        String username = tokenService.extractUsername(token);

        //garantir que usuário existe (opcional)
        Usuario usuario = usuarioService.obterPorNome(username);

        //Determinar se pagamentoId é de cartão ou conta
        FormaPagamentoId pagamentoId;


        if (cartaoService.obterPorId(dto.pagamentoId()) != null) {
            pagamentoId = new CartaoId(dto.pagamentoId());

        } else if (contaService.obter(dto.pagamentoId()) != null) {
            pagamentoId = new ContaId(dto.pagamentoId());

        } else {
            throw new IllegalArgumentException("Forma de pagamento inválida");
        }

        //Criar uma entidade transação com os dados atualizados
        Transacao transacaoAtualizada = new Transacao(
                id,
                dto.origemAgendamentoId(),
                dto.descricao(),
                dto.valor(),
                dto.data(),
                StatusTransacao.valueOf(dto.status()),
                dto.categoriaId(),
                pagamentoId,
                dto.avulsa(),
                Tipo.valueOf(dto.tipo()),
                dto.perfilId()
        );

        transacaoAtualizada.setUsuarioId(usuario.getId());
        //Salvar
        transacaoService.atualizarTransacao(transacaoAtualizada);

        return ResponseEntity.noContent().build();
    }

    @PostMapping("/reembolso")
    public ResponseEntity<Object> criarReembolso(@RequestBody TransacaoDTO dto, HttpServletRequest request) {
        if (dto.valor() == null || dto.transacaoOriginalId() == null) {
            return ResponseEntity.badRequest().body("Valor e ID da transação original são obrigatórios.");
        }

        try {
            Transacao reembolso = transacaoService.registrarReembolso(
                    dto.valor(),
                    dto.transacaoOriginalId()
            );
            return ResponseEntity.ok(reembolso);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Erro ao processar reembolso: " + e.getMessage());
        }
    }

}
