package rastreamentoDeInvestimentos;

import historicoInvestimento.HistoricoInvestimento;
import historicoInvestimento.HistoricoInvestimentoRepositorio;
import historicoInvestimento.HistoricoInvestimentoService;
import investimento.Investimento;
import investimento.InvestimentoRepositorio;
import investimento.InvestimentoService;
import io.cucumber.java.en.*;
import jobScheduler.JobScheduler;
import selicApiClient.SelicApiClient;
import taxaSelic.TaxaSelicRepository;
import taxaSelic.TaxaSelicService;

import static org.junit.jupiter.api.Assertions.*;

import java.math.BigDecimal;
import java.util.*;

public class RastreamentoDeInvestimentosTest {

    private Investimento investimento;
    private HistoricoInvestimentoRepositorio historicoInvestimentoRepositorio;
    private SelicApiClient selicApiClient = new SelicApiClient(true);
    private InvestimentoService investimentoService;
    private JobScheduler jobScheduler;
    private InvestimentoRepositorio investimentoRepositorio;
    private TaxaSelicRepository taxaSelicRepository = new TaxaSelicRepository(null);
    private TaxaSelicService taxaSelicService = new TaxaSelicService(selicApiClient, taxaSelicRepository);
    private Exception excecaoCapturada;

    //Regra de negócio: O sistema precisa de um job agendado para buscar a taxa Selic diária de uma API externa confiável.

    //Scenario: Buscar taxa Selic com sucesso

    @Given("que o job agendado é executado")
    public void jobExecutado() {
        try{
            jobScheduler.executarJob();
        } catch (Exception e){
            excecaoCapturada = e;
        }
    }
    // Fazer com um mock configurável da API. Quando funcionando oferecer o valor e quando não raise exception
    @When("o sistema consulta a API externa do Banco Central")
    public void sistemaConsultaApi(){
        taxaSelicService.atualizarTaxaSelic();
    }

    //Checar via repositório se a taxa foi armazenada
    @Then("a taxa Selic diária é armazenada no sistema")
    public void taxaSelicArmazenada(){
        assertNotNull(taxaSelicRepository.obter());
    }

    //Scenario: Falha ao consultar a API externa


    @When("o sistema consulta a API externa do Banco Central, mas a API não está disponível")
    public void apiNaoDisponivel(){
        selicApiClient.setStatus(false);

        try{
            taxaSelicService.atualizarTaxaSelic();
        }catch (Exception e) {
            excecaoCapturada = e;
        }
    }

    @Then("a texa Selic não é atualizada naquele dia")
    public void taxaNaoAtualizada(){
        assertNull(taxaSelicRepository.obter());
    }

    @And("o sistema deve registrar um log de erro")
    public void registroDeLogDeErro(){
        assertNotNull(excecaoCapturada);
        assertTrue(excecaoCapturada.getMessage().contains("Falha em buscar a taxa selic."));
    }

    //Regra de negócio: Para cada investimento "Tesouro Selic", aplicar rendimento diário automaticamente.

    //Scenario: Atualização de rendimento bem-sucedida

    @Given("que existe um investimento do tipo Tesouro Selic com valor atual de {double}")
    public void criarInvestimento(double valor){
        BigDecimal valorBigDecimal = BigDecimal.valueOf(valor);
        investimento = new Investimento("1", "Investimento A", "Desc" , valorBigDecimal);
        historicoInvestimentoRepositorio = new HistoricoInvestimentoRepositorio();
        selicApiClient.setStatus(true);
        investimentoRepositorio = new InvestimentoRepositorio();

        //taxaSelicRepository = new TaxaSelicRepository(null);

        investimentoService = new InvestimentoService(investimentoRepositorio, taxaSelicRepository, historicoInvestimentoRepositorio);
        investimentoService.salvar(investimento);
        jobScheduler = new JobScheduler(investimentoService, investimentoRepositorio);
        excecaoCapturada = null;
        historicoInvestimentoRepositorio.setStatus(true);
    }

    @And("a taxa selic diária é de {double} \\({int}%)")
    public void taxaSelicUmPorcento(Double double1, Integer int1){
        taxaSelicService.atualizarTaxaSelic();
        double a = 1;
        assertNotNull(taxaSelicRepository.obter().getValor());
        assertEquals(new BigDecimal(double1), taxaSelicRepository.obter().getValor());
    }

    @When("o job de atualização de rendimento é executado")
    public void executarJobRendimento(){
        try{
            jobScheduler.executarJob();
        } catch (Exception e){
            excecaoCapturada = e;
        }
    }

    @Then("o valor atualizado do investimento deve ser {double}")

    // Fazer a verificação pelo repositório
    public void verificarValorAtualizado(double esperado){
        double valorAtualizado = investimentoRepositorio.obter(investimento.getId()).getValorAtual().doubleValue();
        assertEquals(esperado, valorAtualizado, 0.001);
    }

    //Scenario: Tentativa de aplicar rendimento sem taxa Selic disponível


    @And("não há taxa Selic disponível no sistema")
    //Criar um repositório que guarda essa informação como mock
    public void taxaNaoDisponivel(){
        selicApiClient.setStatus(false);
    }

    @Then("o investimento não deve ser atualizado")
    public void investimentoNaoAtualizado() {
        double valorAntes = 1000;
        double valorAtual = investimentoRepositorio.obter(investimento.getId()).getValorAtual().doubleValue();
        assertEquals(valorAntes, valorAtual, 0.001);
    }

    @And("o sistema deve registrar um log de falha")
    public void regsitrarLogDeFalha(){
        assertNotNull(excecaoCapturada);
        assertTrue(excecaoCapturada.getMessage().contains("Taxa Selic não disponível."));
    }

    //Regra de negócio: Sempre que o rendimento é aplicado, registrar histórico com data e valor.

    //Scenario: Registro de histórico após atualização

    @Then("deve existir um registro no histórico com a data atual e o valor {double}")
    public void verificarHistorico(Double esperado){
        assertFalse(historicoInvestimentoRepositorio.obterTodos().isEmpty());
        HistoricoInvestimento h = historicoInvestimentoRepositorio.obterTodos().getFirst();
        assertEquals(esperado, h.getValorAtualizado().doubleValue(), 0.001);
    }

    //Scenario: Falha ao registrar histórico

    @When("o job de atualização de rendimento é executado, mas ocorre uma falha no registro de histórico")
    public void executarJobRendimentoFalhaHistorico(){
        historicoInvestimentoRepositorio.setStatus(false);
        try{
            jobScheduler.executarJob();
        } catch (Exception e){
            excecaoCapturada = e;
        }
    }


    @Then("o sistema deve gerar um log de erro indicando falha ao registrar histórico")
    public void logErroHistorico(){
        assertNotNull(excecaoCapturada);
        assertTrue(excecaoCapturada.getMessage().contains("Falha ao salvar histórico"));
    }

    //Scenario: Resgate total bem-sucedido

    @When("realizo o resgate total do valor investido")
    public void resgateTotaldoValor(){
        try{
            investimentoService.resgateTotal(investimentoRepositorio.obter("1").getId());
        }catch (Exception e){
            excecaoCapturada = e;
        }

    }

    @Then("o investimento deve ser removido do sistema")
    public void investimentoRemovido(){
        ArrayList<Investimento> investimentos = investimentoRepositorio.obterTodos();
        assertTrue(investimentos.isEmpty());
    }

    //Scenario: Falha em etapas anteriores do resgate total

    @When("realizo o resgate total do valor investido, mas uma falha ocorre")
    public void falhaEmResgateTotal(){
        historicoInvestimentoRepositorio.setStatus(false);
        try{
            investimentoService.resgateTotal(investimentoRepositorio.obter("1").getId());
        }catch (Exception e){
            excecaoCapturada = e;
        }
    }

    @Then("o investimento não deve ser removido")
    public void investimentoNaoRemovido(){
        ArrayList<Investimento> investimentos = investimentoRepositorio.obterTodos();
        assertFalse(investimentos.isEmpty());
    }

    @And("o sistema deve emitir um log de falha")
    public void emicaoLogFalha(){
        assertNotNull(excecaoCapturada);
        assertTrue(excecaoCapturada.getMessage().contains("Falha ao deletar histórico."));
    }

    //Scenario: Resgate parcial bem-sucedido

    @When("realizo o resgate parcial de {double} reais do valor investido")
    public void resgateParcialQuinhetos(double valor){
        try{
            investimentoService.resgateParcial(investimentoRepositorio.obter("1").getId(), new BigDecimal(valor));
        }catch (Exception e){
            excecaoCapturada = e;
        }

    }

    @Then("o sistema deve atualizar o valor investido para {double} reais")
    public void valorAtualizado(double valorAtualizado){
        BigDecimal valorInvestimento = investimentoRepositorio.obter("1").getValorAtual();
        BigDecimal valorAtualizadoBigDecimal = new BigDecimal(valorAtualizado);
        assertEquals(0, valorAtualizadoBigDecimal.compareTo(valorInvestimento));

    }

    //Scenario: Tentativa de resgate total em resgate parcial

    @When("realizo o resgate parcial com o valor total investido")
    public void resgateParcialTotal(){
        try{
            investimentoService.resgateParcial(investimentoRepositorio.obter("1").getId(), new BigDecimal(1000));
        }catch (Exception e){
            excecaoCapturada = e;
        }
    }

    @Then("o sistema deve impedir a atualização do valor investido")
    public void valorNaoAtualizado(){
        BigDecimal valorInvestimento = investimentoRepositorio.obter("1").getValorAtual();
        assertEquals(0, valorInvestimento.compareTo(new BigDecimal(1000)));
    }

    @And("exibir aviso de tentativa de resgate total em resgate parcial")
    public void exibirAviso(){
        assertNotNull(excecaoCapturada);
        assertTrue(excecaoCapturada.getMessage().contains("Tentativa de resgate total em resgate parcial ou valor inválido."));
    }

    //Scenario: Deleção do histórico de valorização bem-sucedido em resgate total

    @Then("o sistema deve apagar o histórico de valorização daquele investimento")
    public void historicoDeletado(){
        List<HistoricoInvestimento> historico = historicoInvestimentoRepositorio.obterTodos();
        assertTrue(historico.isEmpty());
    }

    //Scenario: Falha deleção do histórico de valorização em resgate total

    @When("realizo o resgate total do valor investido, mas ocorre uma falha na deleção o histórico")
    public void falhaDelecaoHistorico(){
        historicoInvestimentoRepositorio.setStatus(false);
        try{
            investimentoService.resgateTotal(investimentoRepositorio.obter("1").getId());
        }catch (Exception e){
            excecaoCapturada = e;
        }
    }

    @Then("o sistema deve levantar uma exceção referente à falha na deleção")
    public void levantarExcecao(){
        assertNotNull(excecaoCapturada);
        assertTrue(excecaoCapturada.getMessage().contains("Falha ao deletar histórico."));

    }

    //Scenario: Histórico de valorização atualizado com sucesso em resgate parcial

    @Then("o sistema deve atualizar o histórico com uma nova entrada com o valor restante investido de {double} reais")
    public void atualizacaoHistorico(double valor){
        List<HistoricoInvestimento> historico = historicoInvestimentoRepositorio.obterTodos();
        HistoricoInvestimento novaEntrada = historico.getFirst();
        assertEquals(0, novaEntrada.getValorAtualizado().compareTo(new BigDecimal(valor)));
    }

    //Scenario: Falha em etapas anteriores à atualização do histórico em resgate parcial

    @When("realizo o resgate parcial, mas uma falha ocorre")
    public void falhaEmResgateParcial(){
        try{
            investimentoService.resgateParcial(investimentoRepositorio.obter("1").getId(), new BigDecimal(1000));
        }catch (Exception e){
            excecaoCapturada = e;
        }
    }

    @Then("o sistema não deve atualizar o histórico com uma nova entrada")
    public void naoAtualizacaoHistorico(){
        List<HistoricoInvestimento> historico = historicoInvestimentoRepositorio.obterTodos();
        assertTrue(historico.isEmpty());
    }
}
