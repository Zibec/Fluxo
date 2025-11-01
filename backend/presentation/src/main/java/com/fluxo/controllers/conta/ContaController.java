package com.fluxo.controllers.conta;

import conta.Conta;
import conta.ContaId;
import conta.ContaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/conta")
public class ContaController {

    @Autowired
    private ContaService service;

    @GetMapping("/")
    public ResponseEntity<List<Conta>> getAllContas(){
        return ResponseEntity.ok(service.listarTodasContas());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Conta> getContaById(@PathVariable String id){

        if(service.obter(id).isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(service.obter(id).get());
    }

    @PostMapping("/")
    public ResponseEntity<Conta> createConta(@RequestBody Conta conta){
        try {
            service.salvar(conta);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return ResponseEntity.badRequest().build();
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<Conta> updateConta(@PathVariable String id, @RequestBody Conta conta){
        if(service.obter(id).isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        service.deletar(id);

        conta.setId(id);
        service.salvar(conta);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Conta> deleteConta(@PathVariable String id){
        if(service.obter(id).isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        service.deletar(id);
        return ResponseEntity.ok().build();
    }
}
