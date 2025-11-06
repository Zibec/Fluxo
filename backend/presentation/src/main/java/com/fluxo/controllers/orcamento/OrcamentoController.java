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
            var ym = YearMonth.parse(anoMes);
            service.criarOrcamentoMensal(usuarioId, categoriaId, ym, limite);
            return ResponseEntity.status(HttpStatus.CREATED).build();
        } catch (IllegalStateException e) {//orçamento duplicado
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }catch (IllegalArgumentException | DateTimeParseException e){//limite inválido
            return ResponseEntity.badRequest().build();
        }
    }

    @PutMapping("/{usuarioId}/{categoriaId}/{anoMes}")
    public ResponseEntity<Void> atualizarOrcamento(@PathVariable String usuarioId, @PathVariable String categoriaId, @PathVariable String anoMes, @RequestParam BigDecimal limite){
        try{
            var ym = YearMonth.parse(anoMes);
            service.atualizarOrcamento(usuarioId, categoriaId, ym, limite);
            return ResponseEntity.noContent().build();
        }catch (IllegalStateException e){//não existe orçamento p/ essa chave
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }catch (IllegalArgumentException | DateTimeParseException e){//validação do limite
            return ResponseEntity.badRequest().build();
        }
    }
}
