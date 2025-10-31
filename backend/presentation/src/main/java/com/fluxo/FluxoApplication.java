package com.fluxo;

import agendamento.AgendamentoService;
import cartao.CartaoRepositorio;
import cartao.CartaoService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import persistencia.jpa.agendamento.AgendamentoRepositoryImpl;
import persistencia.jpa.cartao.CartaoRepositoryImpl;

@SpringBootApplication(scanBasePackages = "persistencia.jpa")
@EnableJpaRepositories(basePackages = "persistencia.jpa")
@EntityScan(basePackages = "persistencia.jpa")
@ComponentScan(basePackages = {
        "persistencia",
        "com.fluxo" // seu pacote principal
})
public class FluxoApplication {

    @Bean
    public CartaoService cartaoService(CartaoRepositorio repositorio) {
        return new CartaoService(repositorio);
    }

    public static void main(String[] args) {
        SpringApplication.run(FluxoApplication.class, args);
    }

}
