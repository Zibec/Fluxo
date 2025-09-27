package meta;

import conta.Conta;
import conta.ContaRepositorio;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.cucumber.java.en.And;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

public class MetaTest {

    private MetaRepositorio metaRepositorio = new MetaRepositorio();
    private ContaRepositorio contaRepositorio = new ContaRepositorio();
    private MetaService metaService = new MetaService(metaRepositorio, contaRepositorio);

    private Meta meta;
    private Conta contaPrincipal;
    private Exception excecaoCapturada;

    //cenario 1
    @Given("que eu tenho uma meta de poupança {string} com saldo atual de R$ {double}")
    public void queEuTenhoUmaMetaDePoupancaComSaldoAtualDe(String nomeMeta, Double saldoInicialMeta) {
        String id = UUID.randomUUID().toString();

        this.meta = new Meta(id,TipoMeta.POUPANCA, nomeMeta, new BigDecimal("5000.00"), LocalDate.now().plusYears(1));
        this.meta.setSaldoAcumulado(new BigDecimal(saldoInicialMeta)); // Ajusta o saldo inicial para o teste
        metaService.salvar(this.meta);
    }

    @And("o saldo da minha Conta principal é de R$ {double}")
    public void oSaldoDaMinhaContaPrincipalEDe(Double saldoConta) {
        this.contaPrincipal = new Conta();
        this.contaPrincipal.setSaldo(new BigDecimal(saldoConta));
        // Em um sistema real, buscaríamos a conta do usuário, aqui criamos para o teste
    }

    @When("eu faço um aporte de R$ {double} para a meta {string}")
    public void euFacoUmAporteDeParaAMeta(Double valorAporte, String nomeMeta) {
        try {
            metaService.realizarAporte(this.meta.getId(), new BigDecimal(valorAporte), this.contaPrincipal);
        } catch (Exception e) {
            this.excecaoCapturada = e;
        }
    }

    @Then("o saldo da meta {string} deve ser R$ {double}")
    public void oSaldoDaMetaDeveSer(String nomeMeta, Double saldoFinalMeta) {
        Meta metaAtualizada = metaService.obter(this.meta.getId()).get();
        assertEquals(0, new BigDecimal(saldoFinalMeta).compareTo(metaAtualizada.getSaldoAcumulado()), "O saldo da meta não está correto.");
    }

    @And("o saldo da minha Conta principal deve ser R$ {double}")
    public void oSaldoDaMinhaContaPrincipalDeveSer(Double saldoFinalConta) {
        assertEquals(0, new BigDecimal(saldoFinalConta).compareTo(this.contaPrincipal.getSaldo()), "O saldo da conta principal não foi debitado corretamente.");
    }

    //cenario 2
    @When("eu tento fazer um aporte de R$ {double} para a meta {string}")
    public void euTentoFazerUmAporteDeParaAMeta(Double valorAporte, String nomeMeta) {
        try {
            metaService.realizarAporte(this.meta.getId(), new BigDecimal(valorAporte), this.contaPrincipal);
        } catch (Exception e) {
            this.excecaoCapturada = e;
        }
    }

    @Then("o sistema deve exibir a mensagem de erro {string}")
    public void oSistemaDeveExibirAMensagemDeErro(String mensagemEsperada) {
        assertNotNull(excecaoCapturada, "Uma exceção era esperada, mas não ocorreu.");

        assertEquals(mensagemEsperada, excecaoCapturada.getMessage());
    }

    @And("o saldo da meta {string} deve permanecer R$ {double}")
    public void oSaldoDaMetaDevePermanecer(String nomeMeta, Double saldoMeta) {
        Meta metaNaoAlterada = metaRepositorio.obter(this.meta.getId()).get();

        assertEquals(0, new BigDecimal(saldoMeta).compareTo(metaNaoAlterada.getSaldoAcumulado()),
                "O saldo da meta não deveria ter mudado, mas mudou.");
    }

    //cenario 3
    @Given("que não existe uma meta de poupança chamada {string}")
    public void que_nao_existe_uma_meta_de_poupanca_chamada(String nomeMeta) {
        assertTrue(metaRepositorio.obterPorNome(nomeMeta).isEmpty(), "Pré-condição falhou: Uma meta com este nome já existe.");
    }

    @When("o usuário cria uma nova meta de poupança chamada {string} com valor alvo de R$ {double} e prazo de {int} meses")
    public void o_usuario_cria_uma_nova_meta_de_poupanca_chamada(String nomeMeta, Double valorAlvo, int prazoEmMeses) {
        String id = UUID.randomUUID().toString();
        this.meta = new Meta(
                id,
                TipoMeta.POUPANCA,
                nomeMeta,
                new BigDecimal(valorAlvo),
                LocalDate.now().plusMonths(prazoEmMeses)
        );
        metaService.salvar(this.meta);
    }

    @Then("uma meta chamada {string} deve existir no sistema")
    public void uma_meta_chamada_deve_existir_no_sistema(String nomeMeta) {
        Meta metaSalva = metaRepositorio.obter(this.meta.getId())
                .orElseThrow(() -> new AssertionError("A meta deveria ter sido salva, mas não foi encontrada."));

        assertEquals(nomeMeta, metaSalva.getDescricao());
    }
}