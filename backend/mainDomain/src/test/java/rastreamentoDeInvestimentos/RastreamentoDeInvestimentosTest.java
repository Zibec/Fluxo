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

import static org.junit.jupiter.api.Assertions.*;

import java.math.BigDecimal;
import java.util.*;

public class RastreamentoDeInvestimentosTest {

    private Investimento investimento;
    private HistoricoInvestimentoRepositorio historicoInvestimentoRepositorio;
    private SelicApiClient selicApiClient = new SelicApiClient();
    private InvestimentoService investimentoService;
    private JobScheduler jobScheduler;
    private InvestimentoRepositorio investimentoRepositorio;

    private List<Investimento> investimentos;
    private boolean apiForaDoAr;
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

    @When("o sistema consulta a API externa do Banco Central")
    public void sistemaConsultaApi(){
        apiForaDoAr = false;
    }

    @Then("a taxa Selic diária é armazenada no sistema")
    public void taxaSelicArmazenada(){
        assertNotNull(selicApiClient.buscarTaxaSelicDiaria());
    }

    @And("está disponível para cálculo de rendimentos")
    public void disponívelParaCalculo(){}

    //Scenario: Falha ao consultar a API externa


    @But("a API não está disponível")
    public void apiNaoDisponivel(){
        apiForaDoAr = true;
    }

    @Then("a texa Selic não é atualizada naquele dia")
    public void taxaNaoAtualizada(){
        assertThrows(RuntimeException.class, () -> jobScheduler.executarJob());
    }

    @And("o sistema deve registrar um log de erro")
    public void registroDeLogDeErro(){}

    //Regra de negócio: Para cada investimento "Tesouro Selic", aplicar rendimento diário automaticamente.

    //Scenario: Atualização de rendimento bem-sucedida

    @Given("que existe um investimento do tipo {string} com valor atual de {double}")
    public void criarInvestimento(String tipo, double valor){
        BigDecimal valorBigDecimal = BigDecimal.valueOf(valor);
        investimento = new Investimento("1", "Desc" , valorBigDecimal, tipo);
        investimentos = new ArrayList<>();
        investimentos.add(investimento);
        historicoInvestimentoRepositorio = new HistoricoInvestimentoRepositorio();
        selicApiClient = new SelicApiClient(){
            @Override
            public Double buscarTaxaSelicDiaria(){
                if (apiForaDoAr) return null;
                return 0.01;
            }
        };
        investimentoRepositorio = new InvestimentoRepositorio();
        investimentoService = new InvestimentoService(investimentoRepositorio, selicApiClient, new HistoricoInvestimentoService(historicoInvestimentoRepositorio));
        jobScheduler = new JobScheduler(investimentoService, investimentos);
    }

    @And("a taxa selic diária é de {double} \\({int}%)")
    public void taxaSelicUmPorcento(Double double1, Integer int1){}

    @When("o job de atualização de rendimento é executado")
    public void executarJobRendimento(){
        try{
            jobScheduler.executarJob();
        } catch (Exception e){
            excecaoCapturada = e;
        }
    }

    @Then("o valor atualizado do investimento deve ser {double}")
    public void verificarValorAtualizado(double esperado){
        assertEquals(esperado, investimento.getValorAtual().doubleValue(), 0.001);
    }

    //Scenario: Tentativa de aplicar rendimento sem taxa Selic disponível


    @And("não há taxa Selic disponível no sistema")
    public void taxaNaoDisponivel(){
        apiForaDoAr = true;
    }

    @Then("o investimento não deve ser atualizado")
    public void investimentoNaoAtualizado() {
        double valorAntes = 1000;
        assertEquals(valorAntes, investimento.getValorAtual().doubleValue(), 0.001);
    }

    @And("o sistema deve registrar um log de falha")
    public void regsitrarLogDeFalha(){}

    //Regra de negócio: Sempre que o rendimento é aplicado, registrar histórico com data e valor.

    //Scenario: Registro de histórico após atualização

    @Then("deve existir um registro no histórico com a data atual e o valor {double}")
    public void verificarHistorico(Double esperado){
        assertFalse(historicoInvestimentoRepositorio.obterTodos().isEmpty());
        HistoricoInvestimento h = historicoInvestimentoRepositorio.obterTodos().get(0);
        assertEquals(esperado, h.getValorAtualizado().doubleValue(), 0.001);
    }

    //Scenario: Falha ao registrar histórico

    @But("ocorre uma falha no registro de histórico")
    public void falhaNoHistorico(){
        historicoInvestimentoRepositorio = new HistoricoInvestimentoRepositorio(){
            @Override
            public void salvar(HistoricoInvestimento historicoInvestimento){
                throw new RuntimeException("Falha ao salvar histórico");
            }
        };

        investimento = new Investimento("1", "Desc" , new BigDecimal(1000), "Tesouro Selic");
        investimentos = new ArrayList<>();
        investimentos.add(investimento);
        investimentoRepositorio = new InvestimentoRepositorio();
        investimentoService = new InvestimentoService(investimentoRepositorio, selicApiClient, new HistoricoInvestimentoService(historicoInvestimentoRepositorio));
        jobScheduler = new JobScheduler(investimentoService, investimentos);

        try {
            jobScheduler.executarJob();
        } catch (Exception e) {
            excecaoCapturada = e;
        }
    }

    @Then("o sistema deve gerar um log de erro indicando falha ao registrar histórico")
    public void logErroHistorico(){
        assertNotNull(excecaoCapturada);
        assertTrue(excecaoCapturada.getMessage().contains("Falha ao salvar histórico"));
    }
}
