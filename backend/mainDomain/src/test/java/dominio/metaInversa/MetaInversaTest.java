package dominio.metaInversa;

import static org.junit.jupiter.api.Assertions.*;
import java.math.BigDecimal;
import java.time.LocalDate;

import conta.Conta;
import conta.ContaRepositorio;
import conta.ContaService;
import infraestrutura.persistencia.memoria.Repositorio;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.When;
import metaInversa.MetaInversa;
import metaInversa.MetaInversaRepositorio;
import metaInversa.MetaInversaService;
import metaInversa.MetaInversaStatus;
import io.cucumber.java.en.Then;

public class MetaInversaTest {

    private Conta conta;
    private MetaInversa meta;
    private Exception excecao;

    private ContaRepositorio contaRepo = new Repositorio();
    private ContaService contaService = new ContaService(contaRepo);
    private MetaInversaRepositorio mIRepo = new Repositorio();
    private MetaInversaService mIService = new MetaInversaService(mIRepo, contaRepo);

    // Cenário base
    @Given("que o usuário possui uma conta com saldo {string}")
    public void queUsuarioPossuiConta(String saldo) {
        this.conta = new Conta();
        this.conta.setSaldo(new BigDecimal(saldo));
        contaService.salvar(conta);
    }

    @When("o usuário cria uma meta inversa com nome {string} e valor alvo {string}")
    public void usuarioCriaMeta(String nomeMeta, String valorDivida) {
        this.meta = new MetaInversa("1", nomeMeta, new BigDecimal(valorDivida), conta.getId().getId(), LocalDate.now().plusMonths(3));
        mIService.salvar(meta);
    }

    @Then("a meta deve ser criada com status {string}")
    public void metaDeveTerStatus(String statusEsperado) {
        MetaInversa metaInversa = mIService.buscar("1");
        assertEquals(MetaInversaStatus.valueOf(statusEsperado), metaInversa.getStatus());
    }

    @Then("o valor amortizado inicial deve ser 0.00")
    public void valorAmortizadoInicial() {
        MetaInversa metaInversa = mIService.buscar("1");
        assertEquals(BigDecimal.ZERO, metaInversa.getValorAmortizado());
    }

    // Cenários de aporte
    @Given("que existe uma meta inversa ativa de {string}")
    public void existeMetaAtiva(String valorDivida) {
        this.meta = new MetaInversa("1", "Meta Dívida", new BigDecimal(valorDivida), conta.getId().getId(), LocalDate.now().plusMonths(1));
        mIService.salvar(meta);
    }

    @When("o usuário realiza um aporte de {string} para amortizar a dívida")
    public void usuarioRealizaAporte(String valor) {
        
        mIService.realizarAporte(meta.getId(), new BigDecimal(valor));
        
    }

    @Then("o valor amortizado deve ser {string}")
    public void valorAmortizado(String esperado) {
        MetaInversa metaInversa = mIRepo.obterMetaInversa("1").orElse(null);
        assertEquals(new BigDecimal(esperado), metaInversa.getValorAmortizado());
    }

    @Then("o progresso deve ser {string}")
    public void progressoEsperado(String esperado) {
        MetaInversa metaInversa = mIRepo.obterMetaInversa("1").orElse(null);
        assertEquals(new BigDecimal(esperado), metaInversa.getProgresso());
    }

    // Cenário de conclusão
    @When("o usuário realiza um aporte de {string}")
    public void usuarioRealizaAporteGenerico(String valor) {
        mIService.realizarAporte(meta.getId(), new BigDecimal(valor));
        
    }

    @Then("o status da meta deve mudar para {string}")
    public void statusMetaDeveMudarPara(String statusEsperado) {
        MetaInversa metaInversa = mIService.buscar("1");
        assertEquals(MetaInversaStatus.valueOf(statusEsperado), metaInversa.getStatus());
    }

    // Cenários de exceção
    @When("o usuário tenta realizar um aporte de valor nulo")
    public void aporteNulo() {
        try {
            mIService.realizarAporte(meta.getId(), null);
        } catch (Exception e) {
            this.excecao = e;
        }
    }

    @When("o usuário tenta realizar um aporte de {string}")
    public void aporteNegativo(String valor) {
        try {
            mIService.realizarAporte(meta.getId(), new BigDecimal(valor));
        } catch (Exception e) {
            this.excecao = e;
        }
    }

    @Then("o sistema deve lançar uma exceção com a mensagem {string}")
    public void deveLancarExcecaoComMensagem(String mensagemEsperada) {
        assertNotNull(excecao);
        assertEquals(mensagemEsperada, excecao.getMessage());
    }
}
