package dominio.contasapagar;

import cartao.CartaoRepositorio;
import infraestrutura.persistencia.memoria.Repositorio;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import perfil.Perfil;
import perfil.PerfilRepository;
import perfil.PerfilService;
import transacao.StatusTransacao;
import transacao.Tipo;
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

    private Conta conta;
    private Transacao transacao;

    private Perfil perfil = new Perfil("0", "Pai");
    private PerfilRepository perfilRepository = new Repositorio();

    private TransacaoRepositorio txRepositorio = new Repositorio();
    private ContaRepositorio contaRepositorio = new Repositorio();
    private CartaoRepositorio cartaoRepositorio = new Repositorio();

    private TransacaoService txService = new TransacaoService(txRepositorio, contaRepositorio, cartaoRepositorio, new Repositorio());
    private ContaService contaService = new ContaService(contaRepositorio);
    private PerfilService perfilService = new PerfilService(perfilRepository);

    private Exception erro;

    public ContasAPagarTest() {
        this.conta = new Conta();
    }


    // -------------------------
    // Transações Avulsas
    // -------------------------

    @Given("o usuário possui uma conta com saldo {string}")
    public void oUsuarioPossuiUmaContaComSaldo(String saldo) {
        perfilService.salvarPerfil(perfil);
        conta.setSaldo(new BigDecimal(saldo));
        contaService.salvar(conta);
        assertEquals(new BigDecimal(saldo), conta.getSaldo());
    }

    @When("o usuário cria uma transação única {string} de {string}")
    public void oUsuarioCriaUmaTransacaoUnica(String descricao, String valor) {

        transacao = new Transacao(
                UUID.randomUUID().toString(),
                null, // origemAgendamentoId é null para avulsa
                descricao,
                new BigDecimal(valor),
                LocalDate.now(),
                StatusTransacao.PENDENTE,
                "categoria1",
                conta.getId(),
                true,
                Tipo.DESPESA,
                perfilService.obterPerfil("0").getId()
        );

        txService.salvarTransacao(transacao);
        

        assertTrue(transacao.isAvulsa());
    }

    @Then("a transação deve estar registrada com status {string}")
    public void aTransacaoDeveEstarRegistradaComStatus(String status) {
        Transacao t = txService.obterTransacaoPorId(transacao.getId()).orElse(null);
        assertEquals(StatusTransacao.valueOf(status.toUpperCase()), t.getStatus());
    }

    @And("o saldo da conta deve permanecer {string}")
    public void oSaldoDaContaDevePermanecer(String saldoEsperado) {
        Conta c = contaService.obter(conta.getId().getId()).orElse(null);
        assertEquals(new BigDecimal(saldoEsperado), c.getSaldo());
    }

    @Given("existe uma transação única pendente de {string} com descrição {string}")
    public void existeUmaTransacaoUnicaPendente(String valor, String descricao) {
        perfilService.salvarPerfil(perfil);
        transacao = new Transacao(
                UUID.randomUUID().toString(),
                null,
                descricao,
                new BigDecimal(valor),
                LocalDate.now(),
                StatusTransacao.PENDENTE,
                "categoria1",
                conta.getId(),
                true,
                Tipo.DESPESA,
                perfilService.obterPerfil("0").getId()
        );
        txService.salvarTransacao(transacao);

        assertEquals(StatusTransacao.PENDENTE, transacao.getStatus());
        assertTrue(transacao.isAvulsa());
    }

    @When("o usuário altera o valor para {string}")
    public void oUsuarioAlteraOValorPara(String novoValor) {
        Transacao t = txService.obterTransacaoPorId(transacao.getId()).orElse(null);
        t.atualizarValor(new BigDecimal(novoValor));
        txService.atualizarTransacao(t);
    }

    @Then("a transação deve refletir o novo valor {string}")
    public void aTransacaoDeveRefletirONovoValor(String valorEsperado) {
        Transacao t = txService.obterTransacaoPorId(transacao.getId()).orElse(null);
        assertEquals(new BigDecimal(valorEsperado), t.getValor());
    }

    @And("o status deve permanecer {string}")
    public void oStatusDevePermanecer(String status){
        Transacao t = txService.obterTransacaoPorId(transacao.getId()).orElse(null);
        assertEquals(StatusTransacao.valueOf(status.toUpperCase()), t.getStatus());
    }

    @Given("existe uma transação única pendente de {string}")
    public void existeUmaTransacaoUnicaPendenteDe(String valor) {
        perfilService.salvarPerfil(perfil);
        transacao = new Transacao(
                UUID.randomUUID().toString(),
                null,
                "Transação Única",
                new BigDecimal(valor),
                LocalDate.now(),
                StatusTransacao.PENDENTE,
                "categoria1",
                conta.getId(),
                true,
                Tipo.DESPESA,
                perfilRepository.obterPerfil("0").getId()
        );
        txService.salvarTransacao(transacao);
    }

    @When("o usuário deleta essa transação")
    public void oUsuarioDeletaEssaTransacao() {
        txService.excluirTransacao(transacao.getId());
    }

    @Then("a transação não deve mais existir no sistema")
    public void aTransacaoNaoDeveMaisExistirNoSistema() {
        Transacao t = txService.obterTransacaoPorId(transacao.getId()).orElse(null);
        assertNull(t);
    }

    @When("o usuário marca a transação como {string}")
    public void oUsuarioMarcaATransacaoComo(String status) {
        StatusTransacao statusTransacao = StatusTransacao.valueOf(status.toUpperCase());
        if (statusTransacao == StatusTransacao.EFETIVADA) {
            try {
                txService.efetivarTransacao(transacao.getId());
                txService.atualizarTransacao(transacao);
            } catch (Exception e) {
                erro = e;
            }
            
        }
    }

    @Then("o status da transação deve ser {string}")
    public void oStatusDaTransacaoDeveSer(String status) {
        Transacao t = txService.obterTransacaoPorId(transacao.getId()).orElse(null);
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
    // Transações Recorrentes (Agendadas)
    // -------------------------

    @Given("existe uma transação recorrente pendente de {string}")
    public void existeUmaTransacaoRecorrentePendenteDe(String valor) {
        perfilService.salvarPerfil(perfil);
        transacao = new Transacao(
                UUID.randomUUID().toString(),
                "agendamento123",
                "Assinatura",
                new BigDecimal(valor),
                LocalDate.now(),
                StatusTransacao.PENDENTE,
                "categoria2",
                conta.getId(),
                false,
                Tipo.DESPESA,
                perfilRepository.obterPerfil("0").getId()
        );
        assertFalse(transacao.isAvulsa());
    }

    @When("o usuário efetiva essa transação")
    public void oUsuarioEfetivaEssaTransacao() {
        txService.efetivarTransacao(transacao.getId());
    }

    // -------------------------
    // Notificações de Vencimento
    // -------------------------

    @Given("existe uma transação pendente com vencimento para amanhã")
    public void existeUmaTransacaoPendenteComVencimentoParaAmanha() {
        perfilService.salvarPerfil(perfil);
        transacao = new Transacao(
                UUID.randomUUID().toString(),
                null,
                "Conta de Luz",
                new BigDecimal("100.00"),
                LocalDate.now().plusDays(1),
                StatusTransacao.PENDENTE,
                "categoria3",
                conta.getId(),
                true,
                Tipo.DESPESA,
                perfilRepository.obterPerfil("0").getId()
        );
        txService.salvarTransacao(transacao);
    }

    @When("o sistema verifica transações pendentes próximas do vencimento")
    public void oSistemaVerificaTransacoesPendentesProximasDoVencimento() {
        Transacao t = txService.obterTransacaoPorId(transacao.getId()).orElse(null);
        assertTrue(t.isProximaDoVencimento());
    }

    @Then("deve ser gerada uma notificação para o usuário")
    public void deveSerGeradaUmaNotificacaoParaOUsuario() {
        Transacao t = txService.obterTransacaoPorId(transacao.getId()).orElse(null);
        assertTrue(t.isProximaDoVencimento());
    }

    @Then("o sistema deve recusar a operação e exibir mensagem de erro {string}")
    public void oSistemaDeveRecusarAOperaçãoEExibirMensagemDeErro(String mensagem) {
        assertEquals(mensagem, erro.getMessage());
    }
    
}