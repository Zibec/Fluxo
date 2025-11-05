package com.fluxo.controllers.cartao;

import cartao.*;
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

    @GetMapping("/{id}")
    public ResponseEntity<CartaoDTO> getCartao(@PathVariable String id){
        CartaoId cartaoId = new CartaoId(id);

        if(service.obterPorId(cartaoId) == null){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        CartaoDTO cartaoDTO = mapper.map(service.obterPorId(cartaoId),  CartaoDTO.class);

        return ResponseEntity.ok(cartaoDTO);
    }

    @GetMapping("/by-user")
    public ResponseEntity<List<?>> getCartaoByUser(HttpServletRequest request){
        String token = securityFilter.recoverToken(request);
        String name = tokenService.extractUsername(token);
        Usuario usuario = usuarioService.obterPorNome(name);

        List<Cartao> cartoes =  service.obterPorUsuarioId(usuario.getId());

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
        return ResponseEntity.ok(cartaoDTO);
    }

    @PutMapping("/{id}")
    public ResponseEntity<CartaoDTO> updateCartao(@PathVariable String id, @RequestBody CartaoDTO cartao){
            Cartao newCartao = mapper.map(cartao, Cartao.class);
            if(service.obter(newCartao.getNumero()) != null){
                service.deletarCartao(new CartaoId(id));
            }

            newCartao.setId(new CartaoId(id));
            service.salvar(newCartao);

            CartaoDTO cartaoDTO = mapper.map(newCartao, CartaoDTO.class);
            return ResponseEntity.ok(cartaoDTO);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deleteCartao(@PathVariable String id){
        CartaoId cartaoId = new CartaoId(id);
        if(service.obterPorId(cartaoId) == null){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

        service.deletarCartao(cartaoId);

        return ResponseEntity.ok().build();
    }
}
