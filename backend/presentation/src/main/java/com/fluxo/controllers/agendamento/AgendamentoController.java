package com.fluxo.controllers.agendamento;


import agendamento.Agendamento;
import agendamento.AgendamentoService;
import agendamento.Frequencia;
import categoria.CategoriaService;
import com.fluxo.config.security.SecurityFilter;
import com.fluxo.config.security.TokenService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import usuario.Usuario;
import usuario.UsuarioService;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Map;
import java.util.NoSuchElementException;

import static java.util.UUID.randomUUID;


@RestController
@RequestMapping("/api/agendamento")
public class AgendamentoController {

    @Autowired
    private AgendamentoService service;

    @Autowired
    private CategoriaService categoriaService;

    @Autowired
    private UsuarioService usuarioService;

    @Autowired
    private TokenService tokenService;

    @Autowired
    private SecurityFilter securityFilter;

    @GetMapping("/todos")
    public Iterable<Agendamento> buscarTodos(@RequestParam(name = "pageSize", required = false) Integer pageSize, HttpServletRequest request){
        String token   = securityFilter.recoverToken(request);
        String name    = tokenService.extractUsername(token);
        Usuario usuario = usuarioService.obterPorNome(name);
 
        int pS=100;
        if(pageSize != null && pageSize > 0){
            pS = pageSize;
        }
        return service.buscarTodosPorPerfilId(usuario.getId(), pS);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Agendamento> obterAgendamento(@PathVariable String id){

        return service.obterAgendamento(id).map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping("/criar")
    public ResponseEntity<Void> salvarAgendamento(@RequestBody Map<String,String> body, HttpServletRequest request) {
        try {

            String token   = securityFilter.recoverToken(request);
            String name    = tokenService.extractUsername(token);
            Usuario usuario = usuarioService.obterPorNome(name);

            String descricao = body.get("descricao");
            String valorStr = body.get("valor");
            String freqStr  = body.get("frequencia");
            String dataStr  = body.get("proximaData");
            String categoriaId = body.get("categoriaId");

            if (descricao == null || descricao.isBlank() || valorStr == null || valorStr.isBlank() || freqStr == null || freqStr.isBlank() || dataStr == null || dataStr.isBlank()) {
                return ResponseEntity.badRequest().build();
            }

            BigDecimal valor = new BigDecimal(valorStr.replace(",", "."));

            Frequencia frequencia;
            try {
                frequencia = Frequencia.valueOf(freqStr.trim().toUpperCase());
            } catch (IllegalArgumentException e) {
                return ResponseEntity.badRequest().build();
            }

            LocalDate proximaData;
            try {
                proximaData = LocalDate.parse(dataStr);
            } catch (DateTimeParseException e1) {
                try {
                    var fmt = DateTimeFormatter.ofPattern("dd/MM/yyyy");
                    proximaData = LocalDate.parse(dataStr, fmt);
                } catch (DateTimeParseException e2) {
                    return ResponseEntity.badRequest().build();
                }
            }

            Agendamento novo = new Agendamento(
                    randomUUID().toString(),
                    descricao.trim(),
                    valor,
                    frequencia,
                    proximaData,
                    usuario.getId()
            );

            if (categoriaId != null && !categoriaId.isBlank()) {
                var catOpt = categoriaService.obterCategoria(categoriaId);
                if (catOpt.isEmpty() || !catOpt.get().getUsuarioId().equals(usuario.getId())) {
                    return ResponseEntity.notFound().build();
                }
                novo.setCategoriaId(categoriaId);
            }

            service.salvarValidandoNaoNoPassado(novo, LocalDate.now());
            return ResponseEntity.status(201).build();

        } catch (IllegalStateException e) {
            return ResponseEntity.status(409).build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }


    @PutMapping("/atualizar/{id}")
    public ResponseEntity<Void> atualizarValor(@PathVariable String id, @RequestBody Map<String,String> body) {
        try {

            var existenteOpt = service.obterAgendamento(id);
            if (existenteOpt.isEmpty()) {
                return ResponseEntity.status(404).build();
            }
            var existente = existenteOpt.get();

            String valorStr = body.get("valor");
            if (valorStr == null || valorStr.isBlank()) {
                return ResponseEntity.badRequest().build();
            }
            BigDecimal novoValor = new BigDecimal(valorStr.replace(",", "."));

            Agendamento atualizado = new Agendamento(
                    existente.getId(),
                    existente.getDescricao(),
                    novoValor,
                    existente.getFrequencia(),
                    existente.getProximaData(),
                    existente.getPerfilId()
            );

            service.salvarValidandoNaoNoPassado(atualizado, LocalDate.now());
            return ResponseEntity.noContent().build();

        } catch (IllegalStateException e) {
            return ResponseEntity.status(409).build();
        } catch (Exception e) {
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
