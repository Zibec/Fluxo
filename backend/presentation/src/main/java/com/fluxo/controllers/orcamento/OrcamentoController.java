package com.fluxo.controllers.orcamento;

import com.fluxo.config.security.SecurityFilter;
import com.fluxo.config.security.TokenService;
import jakarta.servlet.http.HttpServletRequest;
import orcamento.Orcamento;
import orcamento.OrcamentoChave;
import orcamento.OrcamentoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import usuario.Usuario;
import usuario.UsuarioService;

import java.time.YearMonth;
import java.time.format.DateTimeParseException;
import java.util.List;

@RestController
@RequestMapping("/api/orcamento")
public class OrcamentoController {

    @Autowired
    private OrcamentoService service;

    @Autowired
    private categoria.CategoriaRepositorio categoriaRepo;

    @Autowired
    private UsuarioService usuarioService;

    @Autowired
    private TokenService tokenService;

    @Autowired
    private SecurityFilter securityFilter;

    @GetMapping("/todos")
    public ResponseEntity<List<Orcamento>> listarTodos(){
        return ResponseEntity.ok(service.listarTodos());
    }

    @GetMapping("/todos/by-user")
    public ResponseEntity<List<Orcamento>> listarTodosByUser(HttpServletRequest request){
        String token = securityFilter.recoverToken(request);
        String name = tokenService.extractUsername(token);
        Usuario usuario = usuarioService.obterPorNome(name);
        return ResponseEntity.ok(service.listarTodosByUser(usuario.getId()));
    }


    @GetMapping("/{categoriaId}/{anoMes}")
    public ResponseEntity<Orcamento> obterOrcamento(@PathVariable String categoriaId, @PathVariable String anoMes, HttpServletRequest request){
        String token = securityFilter.recoverToken(request);
        String name = tokenService.extractUsername(token);
        Usuario usuario = usuarioService.obterPorNome(name);

        try {
            var chave = new OrcamentoChave(usuario.getId(), YearMonth.parse(anoMes), categoriaId);
            return service.obterOrcamento(chave)
                    .map(ResponseEntity::ok)
                    .orElseGet(() -> ResponseEntity.notFound().build());
        } catch (DateTimeParseException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/saldo-total/{categoriaId}/{anoMes}")
    public Object obterSaldoTotal(@PathVariable String categoriaId, @PathVariable String anoMes, HttpServletRequest request){
        String token = securityFilter.recoverToken(request);
        String name = tokenService.extractUsername(token);
        Usuario usuario = usuarioService.obterPorNome(name);

        try {
            return service.saldoMensalTotal(usuario.getId(), categoriaId, YearMonth.parse(anoMes));
        } catch (DateTimeParseException e) {

            return ResponseEntity.badRequest().build();
        }
    }

    @PostMapping("/criar")
    public ResponseEntity<Void> criarOrcamentoMensal(@RequestBody java.util.Map<String, String> body, HttpServletRequest request){
        String token = securityFilter.recoverToken(request);
        String name = tokenService.extractUsername(token);
        Usuario usuario = usuarioService.obterPorNome(name);

        try {
            var usuarioId = usuario.getId();
            var categoriaId = body.get("categoriaId");
            var anoMes = java.time.YearMonth.parse(body.get("anoMes"));
            System.out.println(anoMes);
            var limite = new java.math.BigDecimal(body.get("limite").replace(",", "."));

            service.criarOrcamentoMensal(usuarioId, categoriaId, anoMes, limite);
            return ResponseEntity.status(201).build();
        } catch (IllegalStateException e) {
            System.out.println(e.getMessage());
            return ResponseEntity.status(409).build();
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return ResponseEntity.badRequest().build();
        }
    }

    @PutMapping("/{categoriaId}/{anoMes}")
    public ResponseEntity<Void> atualizarOrcamento(HttpServletRequest request, @PathVariable String categoriaId, @PathVariable String anoMes, @RequestBody java.util.Map<String,String> body){
        String token = securityFilter.recoverToken(request);
        String name = tokenService.extractUsername(token);
        Usuario usuario = usuarioService.obterPorNome(name);

        try {
            System.out.println(body);
            var chave = body.get("chave").split("\\|");
            var ym = YearMonth.parse(anoMes);
            var limite = new java.math.BigDecimal(body.get("limite").replace(",", "."));
            service.atualizarOrcamento(usuario.getId(), categoriaId, ym, limite);
            return ResponseEntity.noContent().build();
        } catch (IllegalStateException e) { return ResponseEntity.status(404).build(); }
        catch (Exception e) {
            System.out.println(e.getMessage());
            return ResponseEntity.badRequest().build(); }
    }
}
