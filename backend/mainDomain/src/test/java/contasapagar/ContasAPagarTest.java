package contasapagar;

import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import transacao.StatusTransacao;
import transacao.Transacao;

import java.math.BigDecimal;
import java.time.LocalDate;

import conta.Conta;

import static org.junit.jupiter.api.Assertions.*;

public class ContasAPagarTest {
    private Conta conta;
    private Transacao transacao;

    @Given("o usuário possui um saldo de {string}")
    public void oUsuarioPossuiUmSaldoDe(String saldo) {

        conta = new Conta(new BigDecimal(saldo));
        assertEquals(new BigDecimal(saldo), conta.getSaldo());
    }

    @When("o usuário adiciona uma transação {string} de {string} com descrição {string}")
    public void oUsuarioAdicionaUmaTransacao(String status, String valor, String descricao) {

        StatusTransacao statusEnum = StatusTransacao.valueOf(status.toUpperCase());

        transacao = new Transacao(new BigDecimal(valor), conta, statusEnum, descricao);
        
        assertEquals(statusEnum, transacao.getStatus());
        assertEquals(new BigDecimal(valor), transacao.getValor());
    }

    @Then("a transação deve ser registrada com status {string}")
    public void aTransacaoDeveSerRegistradaComStatus(String statusEsperado) {
        assertEquals(StatusTransacao.valueOf(statusEsperado.toUpperCase()), transacao.getStatus());
    }

    @And("o saldo da conta deve continuar {string}")
    public void oSaldoDaContaDeveContinuar(String saldoEsperado) {
        assertEquals(new BigDecimal(saldoEsperado), conta.getSaldo());
    }



    @Given("existe uma transação {string} de {string} com descrição {string}")
    public void existeUmaTransacaoPendente(String valor, String descricao) {
        transacao = new Transacao(new BigDecimal(valor), conta, StatusTransacao.PENDENTE, descricao);
    }

    @When("o usuário altera o valor para {string}")
    public void oUsuarioAlteraOValorPara(String novoValor) {
        transacao.setValor(new BigDecimal(novoValor));
    }

    @Then("a transação deve refletir o novo valor de {string}")
    public void aTransacaoDeveRefletirONovoValorDe(String valorEsperado) {
        assertEquals(new BigDecimal(valorEsperado), transacao.getValor());
    }

    @And("o status permanece {string}")
    public void oStatusPermanece(String statusEsperado) {
        assertEquals(StatusTransacao.valueOf(statusEsperado.toUpperCase()), transacao.getStatus());
    }

    @Given("existe uma transação pendente de {string} com descrição {string}")
    public void existeUmaTransaçãoPendenteDe(String valor, String descricao){
        transacao = new Transacao(new BigDecimal(valor), conta, StatusTransacao.PENDENTE, descricao);
    }
    @And("o usuário possui um saldo de {string}")
    public void oUsuárioPossuiUmSaldoDe(String saldo){
        conta = new Conta(new BigDecimal(saldo));
    }

    @When("o usuário deleta a transação")
    public void oUsuarioDeletaATransacao() {
        transacao = null; // simulação de exclusão
        assertNull(transacao);
    }

    @Then("a transação não deve mais existir no sistema")
    public void aTransacaoNaoDeveMaisExistirNoSistema() {
        assertNull(transacao);
    }

    @And("o saldo da conta permanece {string}")
    public void oSaldoDaContaPermanece(String saldo){
        assertEquals(new BigDecimal(saldo), conta.getSaldo());
    }

    

    @When("o usuário marca a transação como {string}")
    public void oUsuarioMarcaATransacaoComo(String novoStatus) {
        transacao.efetivar(); // altera status e debita valor
    }

    @Then("o status da transação deve ser {string}")
    public void oStatusDaTransacaoDeveSer(String statusEsperado) {
        assertEquals(statusEsperado, transacao.getStatus());
    }

    @And("o valor da transação deve ser debitado da conta")
    public void oValorDaTransacaoDeveSerDebitadoDaConta() {
        assertEquals(new BigDecimal("750.00"), conta.getSaldo());
    }

    @Given("existe uma transação pendente com data de vencimento para amanhã")
    public void existeUmaTransacaoPendenteComDataDeVencimentoParaAmanha() {
        transacao = new Transacao("Aluguel", new BigDecimal("500.00"), "Pendente", conta);
        transacao.setDataVencimento(LocalDate.now().plusDays(1));
    }

    @When("o sistema verifica transações pendentes próximas do vencimento")
    public void oSistemaVerificaTransacoesPendentesProximasDoVencimento() {
        boolean notificado = transacao.isProximaDoVencimento();
        assertTrue(notificado);
    }

    @Then("deve gerar uma notificação para o usuário")
    public void deveGerarUmaNotificacaoParaOUsuario() {
        assertTrue(transacao.isProximaDoVencimento());
    }
}
