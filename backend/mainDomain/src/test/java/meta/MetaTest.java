package meta;

import conta.Conta;
import conta.ContaRepositorio;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.cucumber.java.en.And;

import java.math.BigDecimal;
import java.time.LocalDate;
import static org.junit.jupiter.api.Assertions.*;

public class MetaTest {

    private MetaRepositorio metaRepositorio = new MetaRepositorio();
    private ContaRepositorio contaRepositorio = new ContaRepositorio();
    private MetaService metaService = new MetaService(metaRepositorio, contaRepositorio);

    private Meta meta;
    private Conta contaPrincipal;
    private Exception excecaoCapturada;

    @Given("que eu tenho uma meta de poupança {string} com saldo atual de R$ {double}")
    public void queEuTenhoUmaMetaDePoupancaComSaldoAtualDe(String nomeMeta, Double saldoInicialMeta) {
        this.meta = new Meta("1",TipoMeta.POUPANCA, nomeMeta, new BigDecimal("5000.00"), LocalDate.now().plusYears(1));
        this.meta.setSaldoAcumulado(new BigDecimal(saldoInicialMeta)); // Ajusta o saldo inicial para o teste
        metaService.salvar(this.meta);
    }

    @And("o saldo da minha Conta principal é de R$ {double}")
    public void oSaldoDaMinhaContaPrincipalEDe(Double saldoConta) {
        this.contaPrincipal = new Conta();
        this.contaPrincipal.setSaldo(new BigDecimal(saldoConta));
        // Em um sistema real, buscaríamos a conta do usuário, aqui criamos para o teste
    }

    @When("eu faco um aporte de R$ {double} para a meta {string}")
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
}