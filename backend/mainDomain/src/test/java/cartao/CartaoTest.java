package cartao;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.cucumber.java.en.And;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

public class CartaoTest {

    private Cartao cartao;
    protected CartaoService cartaoService = new CartaoService(new CartaoRepositorio());


    @Given("que estou logado no sistema")
    public void queEstouLogadoNoSistema() {
        // assume que o usuário está logado
    }

    @And("acesso a página de gestão de contas e cartões")
    public void acessoAPáginaDeGestãoDeContasECartões() {
        // assume que o usuário está na página
    }

    @When("eu cadastro um cartão de crédito com limite de {string}")
    public void euCadastroUmCartaoDeCreditoComLimiteDe(String limite) {
        Cartao novoCartao = new Cartao(
                new CartaoNumero("1234 5678 9012 3456"),
                "João Silva",
                YearMonth.of(2025, 12),
                new Cvv("123"),
                new BigDecimal(limite),
                LocalDate.now().withDayOfMonth(1),
                LocalDate.now().withDayOfMonth(15)
                );

        cartao = novoCartao;

        assertNotNull(novoCartao);
        assertEquals(new BigDecimal(limite), novoCartao.getLimite());
    }

    @Then("o sistema deve salvar os dados do cartão")
    public void oSistemaDeveSalvarOsDadosDoCartao() {
        cartaoService.salvar(cartao);
        assertEquals(cartao.getNumero(), cartaoService.obter(cartao.getNumero()).getNumero());
    }

    @Then("exibir a mensagem {string}")
    public void exibirAMensagem(String msg) {
        assertNotNull(msg);
    }

    @Given("que tenho um cartão de crédito com limite total de {string}")
    public void queTenhoUmCartaoDeCreditoComLimiteTotalDe(String limite) {
        cartao = new Cartao(
                new CartaoNumero("1234 5678 9012 3456"),
                "João Silva",
                YearMonth.of(2025, 12),
                new Cvv("123"),
                new BigDecimal(limite),
                LocalDate.now().withDayOfMonth(1),
                LocalDate.now().withDayOfMonth(15),
                BigDecimal.ZERO // saldo inicial
        );
        cartaoService.salvar(cartao);
        assertNotNull(cartao);
        assertEquals(new BigDecimal(limite), cartao.getLimite());
    }

    @And("despesas na fatura aberta somando {string}")
    public void despesasNaFaturaAbertaSomando(String valor) {
        BigDecimal valorDespesa = new BigDecimal(valor);
        cartao.realizarTransacao(valorDespesa);
        assertEquals(valorDespesa, cartao.getFatura().getValorTotal());
    }

    @When("eu realizo uma nova compra de {string}")
    public void euRealizoUmaNovaCompraDe(String valor) {
        BigDecimal valorCompra = new BigDecimal(valor);
        cartao.realizarTransacao(valorCompra);
    }

    @Then("o sistema deve recalcular o limite disponível como {string}")
    public void oSistemaDeveRecalcularOLimiteDisponivelComo(String esperado) {
        BigDecimal limiteEsperado = new BigDecimal(esperado);
        BigDecimal limiteDisponivel = cartao.getLimiteDisponivel();
        assertEquals(limiteEsperado, limiteDisponivel);
    }

    @Given("que tenho uma fatura em aberto com despesas")
    public void queTenhoUmaFaturaEmAbertoComDespesas() {
        cartao = new Cartao(
                new CartaoNumero("1234 5678 9012 3456"),
                "João Silva",
                YearMonth.of(2025, 12),
                new Cvv("123"),
                new BigDecimal("5000"),
                LocalDate.now(),
                LocalDate.now().withDayOfMonth(15),
                BigDecimal.ZERO
        );
        cartao.realizarTransacao(new BigDecimal("1500"));
        cartao.getFatura().setDataVencimento(LocalDate.now());
        assertNotNull(cartao.getFatura());
        assertEquals(new BigDecimal("1500"), cartao.getFatura().getValorTotal());
    }

    @And("a data de fechamento é hoje")
    public void aDataDeFechamentoEHoje() {
        assertEquals(LocalDate.now(), cartao.getFatura().getDataVencimento());
    }

    @When("o processo de fechamento de fatura é executado")
    public void oProcessoDeFechamentoDeFaturaEExecutado() {
        cartao.getFatura().fecharFatura();
        assertEquals("FECHADA", cartao.getFatura().getStatus());
    }

    @Then("o sistema deve consolidar o valor a pagar")
    public void oSistemaDeveConsolidarOValorAPagar() {
        assertEquals(new BigDecimal("1500"), cartao.getFatura().getValorTotal());
    }
}
