package com.fluxo.controllers.agendamento;


import agendamento.Agendamento;
import agendamento.AgendamentoService;
import com.fluxo.config.security.SecurityFilter;
import com.fluxo.config.security.TokenService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import usuario.Usuario;
import usuario.UsuarioService;

import java.time.LocalDate;
import java.util.NoSuchElementException;


@RestController
@RequestMapping("/api/agendamento")
public class AgendamentoController {

    @Autowired
    private AgendamentoService service;

    @Autowired
    private UsuarioService usuarioService;

    @Autowired
    private TokenService tokenService;

    @Autowired
    private SecurityFilter securityFilter;

    @GetMapping("/todos")
    public Iterable<Agendamento> buscarTodos(@RequestParam(name = "pageSize", required = false) Integer pageSize){
        int pS=100;
        if(pageSize != null && pageSize > 0){
            pS = pageSize;
        }
        return service.buscarTodos(pS);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Agendamento> obterAgendamento(@PathVariable String id){

        return service.obterAgendamento(id).map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping("/criar")
    public ResponseEntity<Void> salvarAgendamento(@RequestBody Agendamento agendamento, HttpServletRequest request){
        String token = securityFilter.recoverToken(request);
        String name = tokenService.extractUsername(token);
        Usuario usuario = usuarioService.obterPorNome(name);

        agendamento.setPerfilId(usuario.getId());

        service.salvarValidandoNaoNoPassado(agendamento, LocalDate.now());
        return ResponseEntity.ok().build();
    }


    @PutMapping("/atualizar/{id}")
    public ResponseEntity<Void> atualizarAgendamento(@PathVariable String id, @RequestBody Agendamento agendamento){
        try{
            if(agendamento == null || agendamento.getValor() == null){
                return ResponseEntity.badRequest().build();
            }
            service.atualizarAgendamento(id, agendamento.getValor());
            return ResponseEntity.noContent().build();
        }catch (NoSuchElementException e){
            return ResponseEntity.notFound().build();
        }catch (IllegalArgumentException e){
            return ResponseEntity.badRequest().build();
        }
    }

    @DeleteMapping("/deletar/{id}")
    public ResponseEntity<Void> deletarAgendamento(@PathVariable String id){

        try{
            service.deletarAgendamento(id);
            return ResponseEntity.noContent().build();
        }catch (NoSuchElementException e){
            return  ResponseEntity.notFound().build();
        }catch (IllegalStateException e){
            return ResponseEntity.badRequest().body(null);
        }
    }
}
