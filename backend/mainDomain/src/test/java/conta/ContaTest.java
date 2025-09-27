package conta;

import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class ContaTest {
    private Conta conta;
    protected ContaService service = new ContaService(new ContaRepositorio());

    @And("acesso a página de gestão de contas")
    public void acessoAPáginaDeGestãoDeContas() {
        // assume que o usuário está na página
    }

    @When("eu cadastro uma conta com saldo inicial {string}")
    public void euCadastroUmaContaNoBancoAgênciaNúmeroComSaldoInicial(String saldoInicial) {
        Conta novaConta = new Conta();
        novaConta.setSaldo(new BigDecimal(saldoInicial));
        this.conta = novaConta;
        assertEquals(new BigDecimal(saldoInicial), novaConta.getSaldo());
    }

    @Then("o sistema deve salvar os dados da conta")
    public void oSistemaDeveSalvarOsDadosDaConta() {
        service.salvar(conta);
        Optional<Conta> contaEsperada = service.obter(conta.getId());
        assertEquals(conta.getId(), contaEsperada.get().getId());
    }

    @Given("que tenho uma conta com saldo {string}")
    public void queEuTenhoUmaContaComSaldo(String saldo) {
        Conta novaConta = new Conta();
        conta = novaConta;
        novaConta.setSaldo(new BigDecimal(saldo));
        service.salvar(novaConta);
        assertNotNull(conta.getId());
        assertEquals(new BigDecimal(saldo), this.conta.getSaldo());
    }

    @When("eu registro um depósito de {string}")
    public void euRegistroUmDepósitoDe(String valor) {
        conta.creditar(new java.math.BigDecimal(valor));
    }

    @Then("o saldo da conta deve ser atualizado para {string}")
    public void oSaldoDaContaDeveSerAtualizadoPara(String saldoEsperado) {
        assertEquals(new BigDecimal(saldoEsperado), conta.getSaldo());
    }

    @When("eu registro uma retirada de {string}")
    public void euRegistroUmaRetiradaDe(String valor) {
        conta.debitar(new java.math.BigDecimal(valor));
    }

    @When("eu tento registrar uma retirada de {string}")
    public void euTentoRegistrarUmaRetiradaDe(String valor) {
        try {
            conta.debitar(new java.math.BigDecimal(valor));
        } catch (IllegalArgumentException e) {
            // exceção esperada para saldo insuficiente
        }
    }

    @Then("o sistema deve impedir a operação")
    public void oSistemaDeveImpedirAOperação() {
        assertEquals(new BigDecimal("500"), conta.getSaldo());
    }


}
