package com.fluxo.controllers.cartao;

import cartao.*;
import com.fluxo.controllers.ControllerMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/cartao")
public class CartaoController {

    @Autowired
    private CartaoService service;

    @Autowired
    private ControllerMapper mapper;

    @GetMapping("/{id}")
    public ResponseEntity<CartaoDTO> getCartao(@PathVariable String id){
        CartaoId cartaoId = new CartaoId(id);

        if(service.obterPorId(cartaoId) == null){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        CartaoDTO cartaoDTO = mapper.map(service.obterPorId(cartaoId),  CartaoDTO.class);

        return ResponseEntity.ok(cartaoDTO);
    }

    @GetMapping("/")
    public ResponseEntity<List<Cartao>> getAllCartoes(){
        return ResponseEntity.ok(service.obterTodos());
    }

    @PostMapping("/")
    public ResponseEntity<CartaoDTO> createCartao(@RequestBody CartaoDTO cartao){
        Cartao newCartao = mapper.map(cartao, Cartao.class);

        if(service.obter(newCartao.getNumero()) != null){
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }

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
