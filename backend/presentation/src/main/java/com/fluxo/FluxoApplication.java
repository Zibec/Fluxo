package com.fluxo;

import agendamento.AgendamentoRepositorio;
import agendamento.AgendamentoService;
import cartao.CartaoRepositorio;
import cartao.CartaoService;
import categoria.CategoriaRepositorio;
import categoria.CategoriaService;
import historicoInvestimento.HistoricoInvestimentoRepositorio;
import historicoInvestimento.HistoricoInvestimentoService;
import investimento.InvestimentoRepositorio;
import investimento.InvestimentoService;
import meta.MetaRepositorio;
import meta.MetaService;
import orcamento.OrcamentoRepositorio;
import orcamento.OrcamentoService;
import perfil.PerfilRepository;
import perfil.PerfilService;
import selicApiClient.SelicApiClient;
import taxaSelic.TaxaSelicRepository;
import taxaSelic.TaxaSelicService;
import transacao.TransacaoRepositorio;
import com.fasterxml.jackson.databind.ObjectMapper;
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
import transacao.TransacaoService;
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
    public SelicApiClient selicApiClient() {
        return new SelicApiClient(true);
    };

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
    @Bean
    public AgendamentoService agendamentoService(AgendamentoRepositorio agendamentoRepo, TransacaoService transacaoService) {
        return new AgendamentoService(agendamentoRepo, transacaoService);
    }
    @Bean
    public OrcamentoService orcamentoService(OrcamentoRepositorio orcamentoRepositorio, TransacaoService transacaoService){
        return new OrcamentoService(orcamentoRepositorio, transacaoService);
    }

    @Bean
    public TransacaoService transacaoService(TransacaoRepositorio transacaoRepositorio, ContaRepositorio contaRepositorio, CartaoRepositorio cartaoRepositorio){
        return new TransacaoService(transacaoRepositorio, contaRepositorio, cartaoRepositorio);
    }

    @Bean
    public MetaService metaService(MetaRepositorio repositorio, ContaRepositorio repositorioConta) {
        return new MetaService(repositorio, repositorioConta);
    }

    @Bean
    public TaxaSelicService taxaSelicService(SelicApiClient selicApiClient, TaxaSelicRepository taxaSelicRepository){
        return new TaxaSelicService(selicApiClient, taxaSelicRepository);
    }

    @Bean
    public PerfilService perfilService(PerfilRepository perfilRepository){
        return  new PerfilService(perfilRepository);
    }

    @Bean
    public InvestimentoService investimentoService(InvestimentoRepositorio investimentoRepositorio, TaxaSelicRepository taxaSelicRepository, HistoricoInvestimentoRepositorio historicoInvestimentoRepositorio){
        return new InvestimentoService(investimentoRepositorio, taxaSelicRepository, historicoInvestimentoRepositorio);
    }

    @Bean
    public HistoricoInvestimentoService historicoInvestimentoService(HistoricoInvestimentoRepositorio historicoInvestimentoRepositorio){
        return new HistoricoInvestimentoService(historicoInvestimentoRepositorio);
    }

    public static void main(String[] args) {
        SpringApplication.run(FluxoApplication.class, args);
    }



    @PostConstruct
    public void setUp() {
        objectMapper.findAndRegisterModules();
    }

}
