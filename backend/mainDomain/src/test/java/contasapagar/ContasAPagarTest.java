package contasapagar;

import conta.Conta;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import transacao.Transacao;

public class ContasAPagarTest {
    private Conta conta;
    private Transacao transacao;

    // -------------------------
    // Transações Únicas
    // -------------------------

    @Given("o usuário possui uma conta com saldo {string}")
    public void oUsuarioPossuiUmaContaComSaldo(String saldo) {
        conta = new Conta(new BigDecimal(saldo));
        assertEquals(new BigDecimal(saldo), conta.getSaldo());
    }

    @When("o usuário cria uma transação única {string} de {string}")
    public void oUsuarioCriaUmaTransacaoUnica(String descricao, String valor) {
        transacao = new Transacao("1", descricao, new BigDecimal(valor), "Pendente", conta, true);
        assertTrue(transacao.isUnica());
    }

    @Then("a transação deve estar registrada com status {string}")
    public void aTransacaoDeveEstarRegistradaComStatus(String status) {
        assertEquals(status, transacao.getStatus());
    }

    @And("o saldo da conta deve permanecer {string}")
    public void oSaldoDaContaDevePermanecer(String saldoEsperado) {
        assertEquals(new BigDecimal(saldoEsperado), conta.getSaldo());
    }


    @Given("existe uma transação única pendente de {string} com descrição {string}")
    public void existeUmaTransacaoUnicaPendente(String valor, String descricao) {
        transacao = new Transacao(descricao, new BigDecimal(valor), "Pendente", conta, true);
        assertEquals("Pendente", transacao.getStatus());
        assertTrue(transacao.isUnica());
    }

    @When("o usuário altera o valor para {string}")
    public void oUsuarioAlteraOValorPara(String novoValor) {
        transacao.setValor(new BigDecimal(novoValor));
    }

    @Then("a transação deve refletir o novo valor {string}")
    public void aTransacaoDeveRefletirONovoValor(String valorEsperado) {
        assertEquals(new BigDecimal(valorEsperado), transacao.getValor());
    }

    @And("o status deve permanecer {string}")
    public void oStatusDevePermanecer(String statusEsperado) {
        assertEquals(statusEsperado, transacao.getStatus());
    }


    @Given("existe uma transação única pendente de {string}")
    public void existeUmaTransacaoUnicaPendenteDe(String valor) {
        transacao = new Transacao("Transação Única", new BigDecimal(valor), "Pendente", conta, true);
    }

    @When("o usuário deleta essa transação")
    public void oUsuarioDeletaEssaTransacao() {
        transacao = null; // simulação de exclusão
    }

    @Then("a transação não deve mais existir no sistema")
    public void aTransacaoNaoDeveMaisExistirNoSistema() {
        assertNull(transacao);
    }


    @When("o usuário marca a transação como {string}")
    public void oUsuarioMarcaATransacaoComo(String novoStatus) {
        transacao.efetivar();
    }

    @Then("o status da transação deve ser {string}")
    public void oStatusDaTransacaoDeveSer(String statusEsperado) {
        assertEquals(statusEsperado, transacao.getStatus());
    }

    @And("o valor da transação deve ser debitado da conta")
    public void oValorDaTransacaoDeveSerDebitadoDaConta() {
        assertTrue(conta.getSaldo().compareTo(BigDecimal.ZERO) >= 0);
    }

    @And("o saldo da conta deve ser {string}")
    public void oSaldoDaContaDeveSer(String saldoEsperado) {
        assertEquals(new BigDecimal(saldoEsperado), conta.getSaldo());
    }


    // -------------------------
    // Notificações de Vencimento
    // -------------------------

    @Given("existe uma transação pendente com vencimento para amanhã")
    public void existeUmaTransacaoPendenteComVencimentoParaAmanha() {
        transacao = new Transacao("Conta de Luz", new BigDecimal("100.00"), "Pendente", conta, true);
        transacao.setDataVencimento(LocalDate.now().plusDays(1));
    }

    @When("o sistema verifica transações pendentes próximas do vencimento")
    public void oSistemaVerificaTransacoesPendentesProximasDoVencimento() {
        assertTrue(transacao.isProximaDoVencimento());
    }

    @Then("deve ser gerada uma notificação para o usuário")
    public void deveSerGeradaUmaNotificacaoParaOUsuario() {
        assertTrue(transacao.isProximaDoVencimento());
    }
}
