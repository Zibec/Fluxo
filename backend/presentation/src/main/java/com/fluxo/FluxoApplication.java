package com.fluxo;

import agendamento.AgendamentoRepositorio;
import agendamento.AgendamentoService;
import analise.RelatoriosService;
import cartao.CartaoRepositorio;
import cartao.CartaoService;
import cartao.FaturaRepositorio;
import categoria.CategoriaRepositorio;
import categoria.CategoriaRepositorioProxy;
import categoria.CategoriaService;
import com.fluxo.agendador.AgendadorTarefas;
import divida.DividaRepositorio;
import divida.DividaService;
import historicoInvestimento.HistoricoInvestimentoRepositorio;
import historicoInvestimento.HistoricoInvestimentoService;
import investimento.InvestimentoRepositorio;
import investimento.InvestimentoService;
import meta.MetaRepositorio;
import meta.MetaService;
import metaInversa.MetaInversaRepositorio;
import metaInversa.MetaInversaService;
import orcamento.OrcamentoRepositorio;
import orcamento.OrcamentoService;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.impl.StdSchedulerFactory;
import org.quartz.impl.matchers.GroupMatcher;
import org.springframework.scheduling.annotation.EnableScheduling;
import patrimonio.PatrimonioRepositorio;
import patrimonio.PatrimonioService;
import perfil.PerfilRepository;
import perfil.PerfilService;
import persistencia.jpa.Mapper;
import persistencia.jpa.categoria.CategoriaJpaRepository;
import persistencia.jpa.categoria.CategoriaRepository;
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
@EnableScheduling
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
    public CartaoService cartaoService(CartaoRepositorio repositorio, FaturaRepositorio faturaRepositorio) {
        return new CartaoService(repositorio, faturaRepositorio);
    }

    @Bean
    public ContaService contaService(ContaRepositorio repositorio) {
        return new ContaService(repositorio);
    }

    @Bean
    public CategoriaRepositorio categoriaRepositorio(CategoriaJpaRepository jpa, Mapper mapper) {
        // implementação real
        CategoriaRepositorio real = new CategoriaRepository(jpa, mapper);

        // wrap com o proxy
        return new CategoriaRepositorioProxy(real);
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
    public TransacaoService transacaoService(TransacaoRepositorio transacaoRepositorio, ContaRepositorio contaRepositorio, CartaoRepositorio cartaoRepositorio, FaturaRepositorio faturaRepositorio){
        return new TransacaoService(transacaoRepositorio, contaRepositorio, cartaoRepositorio, faturaRepositorio);
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
    public InvestimentoService investimentoService(InvestimentoRepositorio investimentoRepositorio, TaxaSelicRepository taxaSelicRepository, HistoricoInvestimentoRepositorio historicoInvestimentoRepositorio, ContaRepositorio contaRepositorio){
        return new InvestimentoService(investimentoRepositorio, taxaSelicRepository, historicoInvestimentoRepositorio, contaRepositorio);
    }

    @Bean
    public HistoricoInvestimentoService historicoInvestimentoService(HistoricoInvestimentoRepositorio historicoInvestimentoRepositorio){
        return new HistoricoInvestimentoService(historicoInvestimentoRepositorio);
    }

    @Bean
    public MetaInversaService metaInversaService(MetaInversaRepositorio metaInversaRepositorio, ContaRepositorio contaRepositorio){
        return new MetaInversaService(metaInversaRepositorio, contaRepositorio);
    }

    @Bean
    public PatrimonioService patrimonioService(ContaRepositorio contaRepositorio, InvestimentoRepositorio investimentoRepositorio, DividaRepositorio dividaRepositorio, PatrimonioRepositorio patrimonioRepositorio, CartaoRepositorio cartaoRepositorio) {
        return new PatrimonioService(contaRepositorio, investimentoRepositorio, dividaRepositorio, patrimonioRepositorio, cartaoRepositorio);
    }

    @Bean
    public RelatoriosService relatoriosService(CartaoService cartaoService, ContaService contaService) {
        return new RelatoriosService(contaService, cartaoService);
    }

    @Bean
    public DividaService dividaService(DividaRepositorio repositorio) {
        return new DividaService(repositorio);
    }

    public static void main(String[] args) {
        SpringApplication.run(FluxoApplication.class, args);
    }

    @Autowired
    private Scheduler scheduler;

    @PostConstruct
    public void setUp() throws SchedulerException {
        scheduler = StdSchedulerFactory.getDefaultScheduler();
        scheduler.start();
        objectMapper.findAndRegisterModules();
    }

}
