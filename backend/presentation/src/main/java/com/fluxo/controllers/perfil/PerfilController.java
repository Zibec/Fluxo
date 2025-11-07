package com.fluxo.controllers.perfil;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import perfil.Perfil;
import perfil.PerfilService;

import java.util.List;

@RestController
@RequestMapping(name = "/api/perfis")
public class PerfilController {

    @Autowired
    private PerfilService perfilService;

    @GetMapping("/")
    private ResponseEntity<List<Perfil>> getAllPerfis(){
        return ResponseEntity.ok(perfilService.obterTodosPerfis());
    }

    @GetMapping("/{id}")
    private ResponseEntity<Perfil> getPerfil(@PathVariable String id){
        return ResponseEntity.ok(perfilService.obterPerfil(id));
    }

    @PostMapping("/")
    private ResponseEntity<Perfil> salvarPefil(@RequestBody Perfil perfil){
        try {
            perfilService.salvarPerfil(perfil);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return ResponseEntity.badRequest().build();
        }
    }

    @PutMapping("/{id}")
    private ResponseEntity<Perfil> editarPerfil(@PathVariable String id, @RequestBody Perfil perfil){
        perfilService.alterarPerfil(id, perfil);
        return ResponseEntity.ok().build();
    }
}
