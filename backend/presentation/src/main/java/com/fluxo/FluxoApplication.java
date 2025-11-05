package com.fluxo;

import agendamento.AgendamentoService;
import cartao.CartaoRepositorio;
import cartao.CartaoService;
import categoria.CategoriaRepositorio;
import categoria.CategoriaService;
import transacao.TransacaoRepositorio;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import conta.ContaRepositorio;
import conta.ContaService;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import usuario.UsuarioRepositorio;
import usuario.UsuarioService;

@SpringBootApplication(scanBasePackages = "persistencia.jpa")
@EnableJpaRepositories(basePackages = "persistencia.jpa")
@EntityScan(basePackages = "persistencia.jpa")
@ComponentScan(basePackages = {
        "persistencia",
        "com.fluxo" // seu pacote principal
})
public class FluxoApplication {

    @Autowired
    private ObjectMapper objectMapper;

    @Bean
    public UsuarioService usuarioService(UsuarioRepositorio repositorio) {
        return new UsuarioService(repositorio);
    }

    @Bean
    public CartaoService cartaoService(CartaoRepositorio repositorio) {
        return new CartaoService(repositorio);
    }

    @Bean
    public ContaService contaService(ContaRepositorio repositorio) {
        return new ContaService(repositorio);
    }

    @Bean
    public CategoriaService categoriaService(CategoriaRepositorio repositorio, TransacaoRepositorio repositorioTransacao) {
        return new CategoriaService(repositorio, repositorioTransacao);
    }

    public static void main(String[] args) {
        SpringApplication.run(FluxoApplication.class, args);
    }

    @PostConstruct
    public void setUp() {
        objectMapper.findAndRegisterModules();
    }

}
