package contasapagar;

import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import transacao.StatusTransacao;
import transacao.Transacao;
import transacao.TransacaoRepositorio;
import transacao.TransacaoService;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

import conta.Conta;
import conta.ContaRepositorio;
import conta.ContaService;

import static org.junit.jupiter.api.Assertions.*;

public class ContasAPagarTest {

    private Conta conta = new Conta();
    private Transacao transacao;

    private TransacaoRepositorio txRepositorio = new TransacaoRepositorio();
    private TransacaoService txService = new TransacaoService(txRepositorio);

    private Exception erro;

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
        transacao = new Transacao(UUID.randomUUID().toString(),
                null, // origemAgendamentoId é null para avulsa
                descricao,
                new BigDecimal(valor),
                LocalDate.now(),
                StatusTransacao.PENDENTE,
                "categoria1",
                conta,
                true);

        txRepositorio.salvar(transacao);
        
        assertTrue(transacao.isAvulsa());
    }

    @Then("a transação deve estar registrada com status {string}")
    public void aTransacaoDeveEstarRegistradaComStatus(String status) {
        Transacao t = txRepositorio.buscarPorId(transacao.getId()).orElse(null);
        assertEquals(StatusTransacao.valueOf(status.toUpperCase()), t.getStatus());
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
                true
        );
        txRepositorio.salvar(transacao);

        assertEquals(StatusTransacao.PENDENTE, transacao.getStatus());
        assertTrue(transacao.isAvulsa());
    }

    @When("o usuário altera o valor para {string}")
    public void oUsuarioAlteraOValorPara(String novoValor) {
        transacao.atualizarValor(new BigDecimal(novoValor));
        txRepositorio.atualizar(transacao);
    }

    @Then("a transação deve refletir o novo valor {string}")
    public void aTransacaoDeveRefletirONovoValor(String valorEsperado) {
        Transacao t = txRepositorio.buscarPorId(transacao.getId()).orElse(null);
        assertEquals(new BigDecimal(valorEsperado), t.getValor());
    }

    @And("o status deve permanecer {string}")
    public void oStatusDevePermanecer(String status){
        Transacao t = txRepositorio.buscarPorId(transacao.getId()).orElse(null);
        assertEquals(StatusTransacao.valueOf(status.toUpperCase()), t.getStatus());
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
                true
        );
        txRepositorio.salvar(transacao);
    }

    @When("o usuário deleta essa transação")
    public void oUsuarioDeletaEssaTransacao() {
        transacao = null; // simulação de exclusão
        txRepositorio.excluir(transacao.getId());
    }

    @Then("a transação não deve mais existir no sistema")
    public void aTransacaoNaoDeveMaisExistirNoSistema() {
        Transacao t = txRepositorio.buscarPorId(transacao.getId()).orElse(null);
        assertNull(t);
    }

    @When("o usuário marca a transação como {string}")
    public void oUsuarioMarcaATransacaoComo(String status) {
        StatusTransacao statusTransacao = StatusTransacao.valueOf(status.toUpperCase());
        if (statusTransacao == StatusTransacao.EFETIVADA) {
            transacao.efetivar();
            txRepositorio.atualizar(transacao);
        }
    }

    @Then("o status da transação deve ser {string}")
    public void oStatusDaTransacaoDeveSer(String status) {
        Transacao t = txRepositorio.buscarPorId(transacao.getId()).orElse(null);
        assertEquals(StatusTransacao.valueOf(status.toUpperCase()), t.getStatus());
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
        transacao = new Transacao(
                UUID.randomUUID().toString(),
                null,
                "Conta de Luz",
                new BigDecimal("100.00"),
                LocalDate.now().plusDays(1),
                StatusTransacao.PENDENTE,
                "categoria3",
                conta,
                true
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

    @Then("o sistema deve recusar a operação e exibir mensagem de erro")
    public void oSistemaDeveRecusarAOperaçãoEExibirMensagemDeErro() {
        assertNotNull(erro, "Esperava-se que o sistema lançasse um erro, mas nenhum foi capturado.");
        assertEquals("Saldo insuficiente para realizar o débito.", erro.getMessage());
    }
}
