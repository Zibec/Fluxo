package com.fluxo.controllers.conta;

import cartao.Cartao;
import com.fluxo.config.security.SecurityFilter;
import com.fluxo.config.security.TokenService;
import com.fluxo.controllers.ControllerMapper;
import com.fluxo.controllers.cartao.CartaoDTO;
import conta.Conta;
import conta.ContaId;
import conta.ContaService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import usuario.Usuario;
import usuario.UsuarioService;

import java.util.List;
import java.util.Objects;

@RestController
@RequestMapping("/api/conta")
public class ContaController {

    @Autowired
    private ContaService service;

    @Autowired
    private ControllerMapper mapper;

    @Autowired
    private UsuarioService usuarioService;

    @Autowired
    private TokenService tokenService;

    @Autowired
    private SecurityFilter securityFilter;


    @GetMapping("/")
    public ResponseEntity<List<Conta>> getAllContas(){
        return ResponseEntity.ok(service.listarTodasContas());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ContaDTO> getContaById(@PathVariable String id){

        if(service.obter(id).isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        ContaDTO contaDTO = mapper.map(service.obter(id).get(),  ContaDTO.class);
        return ResponseEntity.ok(contaDTO);
    }

    @GetMapping("/by-user")
    public ResponseEntity<List<?>> getCartaoByUser(HttpServletRequest request){
        String token = securityFilter.recoverToken(request);
        String name = tokenService.extractUsername(token);
        Usuario usuario = usuarioService.obterPorNome(name);

        List<Conta> contas =  service.obterPorUsuarioId(usuario.getId());

        return ResponseEntity.ok(contas);
    }

    @PostMapping("/")
    public ResponseEntity<Object> createConta(@RequestBody Conta conta, HttpServletRequest request){
        String token = securityFilter.recoverToken(request);
        String name = tokenService.extractUsername(token);
        Usuario usuario = usuarioService.obterPorNome(name);

        try {
            conta.setUsuarioId(usuario.getId());
            service.salvar(conta);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return ResponseEntity.badRequest().build();
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<Object> updateConta(@PathVariable String id, @RequestBody Conta conta){
        if(service.obter(id).isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        service.deletar(id);

        conta.setId(id);
        service.salvar(conta);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deleteConta(@PathVariable String id){
        if(service.obter(id).isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        service.deletar(id);
        return ResponseEntity.ok().build();
    }
}
