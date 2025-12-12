package com.fluxo.controllers.investimento;


import com.fluxo.config.security.SecurityFilter;
import com.fluxo.config.security.TokenService;
import historicoInvestimento.HistoricoInvestimento;
import historicoInvestimento.HistoricoInvestimentoService;
import investimento.Investimento;
import investimento.InvestimentoService;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.quartz.JobKey;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.*;
import taxaSelic.TaxaSelic;
import taxaSelic.TaxaSelicService;
import usuario.Usuario;
import usuario.UsuarioService;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/investimentos")
public class InvestimentoController {

    @Autowired
    private InvestimentoService investimentoService;

    @Autowired
    private TaxaSelicService taxaSelicService;

    @Autowired
    private HistoricoInvestimentoService historicoInvestimentoService;

    @Autowired
    private UsuarioService usuarioService;

    @Autowired
    private TokenService tokenService;

    @Autowired
    private SecurityFilter securityFilter;

    @Autowired
    private Scheduler scheduler;

    @PostConstruct
    public void initialize() {
        taxaSelicService.attach(investimentoService);
    }

    @GetMapping("/")
    public ResponseEntity<List<Investimento>> getAllInvestimentos(HttpServletRequest request){
        String token = securityFilter.recoverToken(request);
        String name = tokenService.extractUsername(token);
        Usuario usuario = usuarioService.obterPorNome(name);

        return ResponseEntity.ok(investimentoService.obterTodosPorUsuarioId(usuario.getId()));
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
    public ResponseEntity<Investimento> salvarInvestimento(@RequestBody Investimento investimento, HttpServletRequest request){
        String token = securityFilter.recoverToken(request);
        String name = tokenService.extractUsername(token);
        Usuario usuario = usuarioService.obterPorNome(name);

        try {
            investimento.setUsuarioId(usuario.getId());
            investimentoService.salvar(investimento);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return ResponseEntity.badRequest().build();
        }
    }

    @PostMapping("/resgate-total/{id}")
    public ResponseEntity<Object> resgateTotal(@PathVariable String id){
        investimentoService.resgateTotal(id);
        return ResponseEntity.ok().build();
    }

    //Para executar atualização manual (bash): POST http://localhost:8080/api/investimentos/forcar-atualizacao
    @PostMapping("/forcar-atualizacao")
    public String dispararManual() throws SchedulerException {
        JobKey jobKey = new JobKey("jobAtualizarTaxaSelic");
        scheduler.triggerJob(jobKey);

        return "Atualização de Taxa Selic executada manualmente.";
    }

    @PutMapping("/resgate-parcial/{id}")
    public ResponseEntity<Object> resgateParcial(@PathVariable String id, @RequestBody Map<Object, String> body){
        investimentoService.resgateParcial(id, BigDecimal.valueOf(Integer.parseInt(body.get("valor"))));
        return ResponseEntity.ok().build();
    }
}
