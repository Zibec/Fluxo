package com.fluxo.controllers.cartao;

import cartao.*;
import com.fluxo.agendador.AgendadorTarefas;
import com.fluxo.config.security.SecurityFilter;
import com.fluxo.config.security.TokenService;
import com.fluxo.controllers.ControllerMapper;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import usuario.Usuario;
import usuario.UsuarioService;

import java.util.List;

@RestController
@RequestMapping("/api/cartao")
public class CartaoController {

    @Autowired
    private CartaoService service;

    @Autowired
    private ControllerMapper mapper;

    @Autowired
    private UsuarioService usuarioService;

    @Autowired
    private TokenService tokenService;

    @Autowired
    private SecurityFilter securityFilter;

    @Autowired
    private AgendadorTarefas agendador;

    @GetMapping("/{id}")
    public ResponseEntity<CartaoDTO> getCartao(@PathVariable String id){

        if(service.obterPorId(id) == null){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        CartaoDTO cartaoDTO = mapper.map(service.obterPorId(id),  CartaoDTO.class);

        return ResponseEntity.ok(cartaoDTO);
    }

    @GetMapping("/by-user")
    public ResponseEntity<List<?>> getCartaoByUser(HttpServletRequest request){
        String token = securityFilter.recoverToken(request);
        String name = tokenService.extractUsername(token);
        Usuario usuario = usuarioService.obterPorNome(name);

        List<Cartao> cartoes =  service.obterPorUsuarioId(usuario.getId());

        cartoes.stream()
                .map(c -> {
                    var cartaoDTO = new CartaoDTO();
                    cartaoDTO.usuarioId = c.getUsuarioId();
                    cartaoDTO.dataVencimentoFatura = c.getDataVencimentoFatura().toString();
                    cartaoDTO.dataFechamentoFatura = c.getDataFechamentoFatura().toString();
                    cartaoDTO.saldo = c.getSaldo();
                    cartaoDTO.titular = c.getTitular();
                    cartaoDTO.validade = c.getValidade();
                    cartaoDTO.cvv = c.getCvv().getCodigo();
                    cartaoDTO.limite = c.getLimite();
                    cartaoDTO.numero = c.getNumero().getCodigo();
                    cartaoDTO.id = c.getId().getId();
                    return cartaoDTO;
                })
                .toList();

        return ResponseEntity.ok(cartoes);
    }

    @GetMapping("/")
    public ResponseEntity<List<Cartao>> getAllCartoes(){
        return ResponseEntity.ok(service.obterTodos());
    }

    @PostMapping("/")
    public ResponseEntity<CartaoDTO> createCartao(@RequestBody CartaoDTO cartao, HttpServletRequest request){
        Cartao newCartao = mapper.map(cartao, Cartao.class);

        if(service.obter(newCartao.getNumero()) != null){
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }

        String token = securityFilter.recoverToken(request);
        String name = tokenService.extractUsername(token);
        Usuario usuario = usuarioService.obterPorNome(name);

        newCartao.setUsuarioId(usuario.getId());
        service.salvar(newCartao);
        CartaoDTO cartaoDTO = mapper.map(newCartao, CartaoDTO.class);

        agendador.agendarFechamentoFatura(newCartao);
        agendador.agendarVencimentoFatura(newCartao);
        return ResponseEntity.ok(cartaoDTO);
    }

    @PutMapping("/{id}")
    public ResponseEntity<CartaoDTO> updateCartao(@PathVariable String id, @RequestBody CartaoDTO cartao,  HttpServletRequest request){
            Cartao newCartao = mapper.map(cartao, Cartao.class);
            if(service.obter(newCartao.getNumero()) != null){
                service.deletarCartao(id);
            }

            String token = securityFilter.recoverToken(request);
            String name = tokenService.extractUsername(token);
            Usuario usuario = usuarioService.obterPorNome(name);

            newCartao.setId(new CartaoId(id));
            newCartao.setUsuarioId(usuario.getId());
            service.salvar(newCartao);

            CartaoDTO cartaoDTO = mapper.map(newCartao, CartaoDTO.class);
            return ResponseEntity.ok(cartaoDTO);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deleteCartao(@PathVariable String id){
        if(service.obterPorId(id) == null){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

        service.deletarCartao(id);

        return ResponseEntity.ok().build();
    }

    @GetMapping("/fatura/{cartaoId}")
    public ResponseEntity<Fatura> getCartaoFatura(@PathVariable String cartaoId){
        return ResponseEntity.ok(service.obterFatura(cartaoId));
    }

    @PostMapping("/fatura/{cartaoId}/fechar")
    public ResponseEntity<Fatura> closeCartaoFatura(@PathVariable String cartaoId){
        service.fecharFatura(cartaoId);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/fatura/{cartaoId}/pagar")
    public ResponseEntity<Fatura> payCartaoFatura(@PathVariable String cartaoId){
        service.pagarFatura(cartaoId);
        return ResponseEntity.ok().build();
    }
}
