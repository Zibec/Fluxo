package com.fluxo.controllers.orcamento;

import orcamento.Orcamento;
import orcamento.OrcamentoChave;
import orcamento.OrcamentoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.YearMonth;
import java.time.format.DateTimeParseException;
import java.util.List;

@RestController
@RequestMapping("/api/orcamento")
public class OrcamentoController {

    @Autowired
    private OrcamentoService service;
    @Autowired
    private usuario.UsuarioRepositorio usuarioRepo;
    @Autowired
    private categoria.CategoriaRepositorio categoriaRepo;

    @GetMapping("/todos")
    public ResponseEntity<List<Orcamento>> listarTodos(){
        return ResponseEntity.ok(service.listarTodos());
    }


    @GetMapping("/{usuarioId}/{categoriaId}/{anoMes}")
    public ResponseEntity<Orcamento> obterOrcamento(@PathVariable String usuarioId,@PathVariable String categoriaId, @PathVariable String anoMes ){
        try {
            var chave = new OrcamentoChave(usuarioId, YearMonth.parse(anoMes), categoriaId);
            return service.obterOrcamento(chave)
                    .map(ResponseEntity::ok)
                    .orElseGet(() -> ResponseEntity.notFound().build());
        } catch (DateTimeParseException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PostMapping("/criar")
    public ResponseEntity<Void> criarOrcamentoMensal(@RequestParam String usuarioId, @RequestParam String categoriaId, @RequestParam String anoMes, @RequestParam BigDecimal limite){
        try {
            if (usuarioRepo.obterUsuario(usuarioId).isEmpty())
                return ResponseEntity.status(404).build(); //usuário não existe
            if (categoriaRepo.obterCategoria(categoriaId).isEmpty())
                return ResponseEntity.status(404).build(); //categoria não existe

            var ym = YearMonth.parse(anoMes);
            service.criarOrcamentoMensal(usuarioId, categoriaId, ym, limite);
            return ResponseEntity.status(201).build();
        } catch (IllegalStateException e) {
            return ResponseEntity.status(409).build();
        }
        catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PutMapping("/{usuarioId}/{categoriaId}/{anoMes}")
    public ResponseEntity<Void> atualizarOrcamento(@PathVariable String usuarioId, @PathVariable String categoriaId, @PathVariable String anoMes, @RequestParam BigDecimal limite){
        try{
            if (usuarioRepo.obterUsuario(usuarioId).isEmpty() || categoriaRepo.obterCategoria(categoriaId).isEmpty()) {
                return ResponseEntity.status(404).build();
            }
            var ym = YearMonth.parse(anoMes);
            service.atualizarOrcamento(usuarioId, categoriaId, ym, limite);
            return ResponseEntity.noContent().build();
        } catch (IllegalStateException e) {
            return ResponseEntity.status(404).build();
        }
        catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }
}
