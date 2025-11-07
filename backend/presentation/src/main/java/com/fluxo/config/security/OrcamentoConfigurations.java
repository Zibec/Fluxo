package com.fluxo.config.security;

import orcamento.OrcamentoService;
import orcamento.OrcamentoRepositorio;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import transacao.TransacaoService;
import transacao.TransacaoRepositorio;
import conta.ContaRepositorio;
import cartao.CartaoRepositorio;

@Configuration
public class OrcamentoConfigurations {

    @Bean
    public OrcamentoService orcamentoService(OrcamentoRepositorio orcamentoRepositorio, TransacaoService transacaoService){
        return new OrcamentoService(orcamentoRepositorio, transacaoService);
    }

    @Bean
    public TransacaoService transacaoService(TransacaoRepositorio transacaoRepositorio, ContaRepositorio contaRepositorio, CartaoRepositorio cartaoRepositorio){
        return new TransacaoService(transacaoRepositorio, contaRepositorio, cartaoRepositorio);
    }
}
