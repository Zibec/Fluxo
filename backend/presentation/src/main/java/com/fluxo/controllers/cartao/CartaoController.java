package com.fluxo.controllers.cartao;

import cartao.Cartao;
import cartao.CartaoId;
import cartao.CartaoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import persistencia.jpa.cartao.CartaoJpaRepository;
import persistencia.jpa.cartao.CartaoRepositoryImpl;

import java.util.List;

@RestController
@RequestMapping("/api/cartao")
public class CartaoController {

    @Autowired
    private CartaoService service;

    @GetMapping("/{id}")
    public ResponseEntity<Cartao> getCartao(@PathVariable String id){
        CartaoId cartaoId = new CartaoId(id);

        return ResponseEntity.ok(service.obterPorId(cartaoId));
    }

    @GetMapping("/")
    public ResponseEntity<List<Cartao>> getAllCartoes(){
        return ResponseEntity.ok(service.obterTodos());
    }
}
