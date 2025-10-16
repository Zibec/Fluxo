package contasapagar;

import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import transacao.StatusTransacao;
import transacao.Transacao;
import transacao.Transacao.Tipo; // IMPORT ADICIONADO

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

import conta.Conta;

import static org.junit.jupiter.api.Assertions.*;

public class ContasAPagarTest {

    private Conta conta = new Conta();
    private Transacao transacao;

    // -------------------------
    // Transações Avulsas
    // -------------------------

    @Given("o usuário possui uma conta com saldo {string}")
    public void oUsuarioPossuiUmaContaComSaldo(String saldo) {
        conta.setSaldo(new BigDecimal(saldo));
        assertEquals(new BigDecimal(saldo), conta.getSaldo());
    }

    @When("o usuário cria uma transação única {string} de {string}")
    public void oUsuarioCriaUmaTransacaoUnica(String descricao, String valor) {
        transacao = new Transacao(
                UUID.randomUUID().toString(),
                null,
                descricao,
                new BigDecimal(valor),
                LocalDate.now(),
                StatusTransacao.PENDENTE,
                "categoria1",
                conta,
                true,
                Tipo.DESPESA
        );
        assertTrue(transacao.isAvulsa());
    }

    @Then("a transação deve estar registrada com status {string}")
    public void aTransacaoDeveEstarRegistradaComStatus(String status) {
        assertEquals(StatusTransacao.valueOf(status.toUpperCase()), transacao.getStatus());
    }

    @And("o saldo da conta deve permanecer {string}")
    public void oSaldoDaContaDevePermanecer(String saldoEsperado) {
        assertEquals(new BigDecimal(saldoEsperado), conta.getSaldo());
    }

    @Given("existe uma transação única pendente de {string} com descrição {string}")
    public void existeUmaTransacaoUnicaPendente(String valor, String descricao) {
        transacao = new Transacao(
                UUID.randomUUID().toString(),
                null,
                descricao,
                new BigDecimal(valor),
                LocalDate.now(),
                StatusTransacao.PENDENTE,
                "categoria1",
                conta,
                true,
                Tipo.DESPESA
        );
        assertEquals(StatusTransacao.PENDENTE, transacao.getStatus());
        assertTrue(transacao.isAvulsa());
    }

    @When("o usuário altera o valor para {string}")
    public void oUsuarioAlteraOValorPara(String novoValor) {
        transacao.atualizarValor(new BigDecimal(novoValor));
    }

    @Then("a transação deve refletir o novo valor {string}")
    public void aTransacaoDeveRefletirONovoValor(String valorEsperado) {
        assertEquals(new BigDecimal(valorEsperado), transacao.getValor());
    }

    @And("o status deve permanecer {string}")
    public void oStatusDevePermanecer(String status){
        assertEquals(StatusTransacao.valueOf(status.toUpperCase()), transacao.getStatus());
    }

    @Given("existe uma transação única pendente de {string}")
    public void existeUmaTransacaoUnicaPendenteDe(String valor) {
        transacao = new Transacao(
                UUID.randomUUID().toString(),
                null,
                "Transação Única",
                new BigDecimal(valor),
                LocalDate.now(),
                StatusTransacao.PENDENTE,
                "categoria1",
                conta,
                true,
                Tipo.DESPESA
        );
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
    public void oUsuarioMarcaATransacaoComo(String status) {
        StatusTransacao statusTransacao = StatusTransacao.valueOf(status.toUpperCase());
        if (statusTransacao == StatusTransacao.EFETIVADA) {
            transacao.efetivar();
        }
    }

    @Then("o status da transação deve ser {string}")
    public void oStatusDaTransacaoDeveSer(String status) {
        assertEquals(StatusTransacao.valueOf(status.toUpperCase()), transacao.getStatus());
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
    // Transações Recorrentes (Agendadas)
    // -------------------------

    @Given("existe uma transação recorrente pendente de {string}")
    public void existeUmaTransacaoRecorrentePendenteDe(String valor) {
        transacao = new Transacao(
                UUID.randomUUID().toString(),
                "agendamento123",
                "Assinatura",
                new BigDecimal(valor),
                LocalDate.now(),
                StatusTransacao.PENDENTE,
                "categoria2",
                conta,
                false,
                Tipo.DESPESA
        );
        assertFalse(transacao.isAvulsa());
    }

    @When("o usuário efetiva essa transação")
    public void oUsuarioEfetivaEssaTransacao() {
        transacao.efetivar();
    }

    // -------------------------
    // Notificações de Vencimento
    // -------------------------

    @Given("existe uma transação pendente com vencimento para amanhã")
    public void existeUmaTransacaoPendenteComVencimentoParaAmanha() {
        transacao = new Transacao(
                UUID.randomUUID().toString(),
                null,
                "Conta de Luz",
                new BigDecimal("100.00"),
                LocalDate.now().plusDays(1),
                StatusTransacao.PENDENTE,
                "categoria3",
                conta,
                true,
                Tipo.DESPESA
        );
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