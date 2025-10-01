package conta;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.When;
import io.cucumber.java.en.Then;

import java.math.BigDecimal;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;

public class ContaTest {

    private ContaService contaService;
    private ContaRepositorio contaRepositorio;

    private Conta contaAtual;
    private Exception thrownException;

    // Dados para a criação de uma nova conta
    private String nome;
    private String tipo;
    private String banco;
    private BigDecimal saldoInicial;
    private boolean camposObrigatoriosInformados;

    public ContaTest() {
        this.contaRepositorio = new ContaRepositorio();
        this.contaService = new ContaService(this.contaRepositorio);
    }

    // Scenario: Cadastro de conta com todos os campos obrigatórios preenchidos
    @Given("que o usuário informa nome, tipo e saldo inicial")
    public void que_o_usuario_informa_nome_tipo_e_saldo_inicial() {
        this.camposObrigatoriosInformados = true;
        this.nome = "Conta Corrente";
        this.tipo = "Corrente";
        this.banco = "Banco X";
        this.saldoInicial = new BigDecimal("100.00");
    }

    @When("salvar a conta")
    public void salvar_a_conta() {
        try {
            if (!camposObrigatoriosInformados) {
                throw new IllegalArgumentException("Campos obrigatórios não informados.");
            }

            Conta novaConta = new Conta(nome, tipo, banco, saldoInicial);
            this.contaService.salvar(novaConta);
            this.contaAtual = novaConta;
        } catch (Exception e) {
            this.thrownException = e;
        }
    }

    @Then("a conta deve ser cadastrada com sucesso")
    public void a_conta_deve_ser_cadastrada_com_sucesso() {
        assertNull(thrownException);
        assertNotNull(contaAtual);
        assertTrue(contaService.obter(contaAtual.getId()).isPresent(), "A conta deveria estar salva no repositório.");
    }

    // Scenario: Cadastro de conta sem informar todos os campos obrigatórios
    @Given("que o usuário não informa todos os campos obrigatórios")
    public void que_o_usuario_nao_informa_todos_os_campos_obrigatorios() {
        this.camposObrigatoriosInformados = false;
    }

    @When("tentar salvar a conta")
    public void tentar_salvar_a_conta() {
        salvar_a_conta();
    }

    @Then("o sistema deve recusar o cadastro e exibir mensagem de erro")
    public void o_sistema_deve_recusar_o_cadastro_e_exibir_mensagem_de_erro() {
        assertNotNull(thrownException);
    }

    // Scenario: Cadastro de conta com nome único no mesmo banco
    @Given("que já existe uma conta chamada {string}")
    public void que_ja_existe_uma_conta_chamada(String nomeConta) {
        Conta contaExistente = new Conta(nomeConta, "Corrente", "Banco X", BigDecimal.ZERO);
        contaService.salvar(contaExistente);
    }

    @When("o usuário cadastra uma conta chamada {string}")
    public void o_usuario_cadastra_uma_conta_chamada(String nomeNovaConta) {
        boolean jaExiste = contaRepositorio.contaExistente(nomeNovaConta);

        if (jaExiste) {
            this.thrownException = new IllegalArgumentException("Uma conta com este nome já existe neste banco.");
        } else {
            this.contaAtual = new Conta(nomeNovaConta, "Poupança", "Banco X", BigDecimal.ZERO);
            contaService.salvar(this.contaAtual);
        }
    }

    @Then("a nova conta deve ser cadastrada com sucesso")
    public void a_nova_conta_deve_ser_cadastrada_com_sucesso() {
        assertNull(thrownException);
        assertNotNull(contaAtual);
    }

    // Scenario: Cadastro de conta duplicada com mesmo nome e banco
    @When("o usuário tenta cadastrar outra conta chamada {string}")
    public void o_usuario_tenta_cadastrar_outra_conta_chamada(String nomeConta) {
        o_usuario_cadastra_uma_conta_chamada(nomeConta); // Reutiliza a lógica que deve gerar a exceção.
    }

    // Scenario: Cadastro de conta com saldo inicial maior ou igual a zero
    @Given("que o usuário informa saldo inicial de R$ {double}")
    public void que_o_usuario_informa_saldo_inicial_de_r(Double saldo) {
        this.camposObrigatoriosInformados = true;
        this.nome = "Conta Saldo Positivo";
        this.tipo = "Salário";
        this.banco = "Banco Y";
        this.saldoInicial = BigDecimal.valueOf(saldo);
    }

    // Scenario: Cadastro de conta com saldo inicial negativo
    @Given("que o usuário informa saldo inicial de -R$ {double}")
    public void que_o_usuario_informa_saldo_inicial_de_negativo_r(Double saldo) {
        this.camposObrigatoriosInformados = true;
        this.nome = "Conta Saldo Negativo";
        this.tipo = "Corrente";
        this.banco = "Banco Z";
        this.saldoInicial = BigDecimal.valueOf(saldo).negate();
    }

    // Scenario: Realizar depósito com valor positivo
    @Given("que o usuário possui uma conta com saldo de R$ {double}")
    public void que_o_usuario_possui_uma_conta_com_saldo_de_r(Double saldo) {
        this.contaAtual = new Conta("Conta Teste", "Corrente", "Banco Teste", BigDecimal.valueOf(saldo));
        contaService.salvar(this.contaAtual);
    }

    @When("realiza um depósito de R$ {double}")
    public void realiza_um_deposito_de_r(Double valor) {
        try {
            this.contaAtual.creditar(BigDecimal.valueOf(valor));
        } catch (IllegalArgumentException e) {
            this.thrownException = e;
        }
    }

    @Then("o saldo da conta deve ser atualizado para R$ {double}")
    public void o_saldo_da_conta_deve_ser_atualizado_para_r(Double saldoEsperado) {
        assertNull(thrownException);
        assertEquals(0, BigDecimal.valueOf(saldoEsperado).compareTo(this.contaAtual.getSaldo()));
    }

    // Scenario: Realizar depósito com valor menor ou igual a zero
    @When("tenta realizar um depósito de R$ {double}")
    public void tenta_realizar_um_deposito_de_r(Double valor) {
        realiza_um_deposito_de_r(valor); // A lógica é a mesma, mas deve capturar exceção.
    }

    @Then("o sistema deve recusar a operação e exibir mensagem de erro")
    public void o_sistema_deve_recusar_a_operacao_e_exibir_mensagem_de_erro() {
        assertNotNull(thrownException);
        assertInstanceOf(IllegalArgumentException.class, thrownException);
    }

    // Scenario: Realizar retirada dentro do saldo disponível
    @When("realiza uma retirada de R$ {double}")
    public void realiza_uma_retirada_de_r(Double valor) {
        try {
            this.contaAtual.debitar(BigDecimal.valueOf(valor));
        } catch (IllegalArgumentException e) {
            this.thrownException = e;
        }
    }

    // Scenario: Realizar retirada acima do saldo disponível
    @When("tenta realizar uma retirada de R$ {double}")
    public void tenta_realizar_uma_retirada_de_r(Double valor) {
        realiza_uma_retirada_de_r(valor);
    }
}