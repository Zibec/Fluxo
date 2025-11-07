package com.fluxo.controllers.investimento;


import historicoInvestimento.HistoricoInvestimento;
import historicoInvestimento.HistoricoInvestimentoService;
import investimento.Investimento;
import investimento.InvestimentoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import taxaSelic.TaxaSelic;
import taxaSelic.TaxaSelicService;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/api/investimentos")
public class InvestimentoController {

    @Autowired
    private InvestimentoService investimentoService;

    @Autowired
    private TaxaSelicService taxaSelicService;

    @Autowired
    private HistoricoInvestimentoService historicoInvestimentoService;

    @GetMapping("/")
    public ResponseEntity<List<Investimento>> getAllInvestimentos(){
        return ResponseEntity.ok(investimentoService.obterTodos());
    }

    @GetMapping("/taxa-selic")
    public ResponseEntity<TaxaSelic> getTaxaSelic(){
        return ResponseEntity.ok(taxaSelicService.obterTaxaSelic());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Investimento> getInvestimento(@PathVariable String id){
        return ResponseEntity.ok(investimentoService.obterInvestimento(id));
    }

    @GetMapping("/historicos/{id}")
    public ResponseEntity<List<HistoricoInvestimento>> getHistorico(@PathVariable String id){
        return ResponseEntity.ok(historicoInvestimentoService.obterTodosHistoricosPorInvestimento(id));
    }

    @PostMapping("/")
    public ResponseEntity<Investimento> salvarInvestimento(@RequestBody Investimento investimento){
        try {
            investimentoService.salvar(investimento);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return ResponseEntity.badRequest().build();
        }
    }

    @PostMapping("/resgate-total/{id}")
    public ResponseEntity<Object> resgateTotal(@PathVariable String investimentoId){
        investimentoService.resgateTotal(investimentoId);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/resgate-total/{id}")
    public ResponseEntity<Object> resgateTotal(@PathVariable String investimentoId, @RequestBody BigDecimal valor){
        investimentoService.resgateParcial(investimentoId, valor);
        return ResponseEntity.ok().build();
    }
}
