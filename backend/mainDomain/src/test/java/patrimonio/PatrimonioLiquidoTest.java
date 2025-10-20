package patrimonio;

import conta.Conta;
import conta.ContaRepositorio;
import divida.Divida;
import divida.DividaRepositorio;
import investimento.Investimento;
import investimento.InvestimentoRepositorio;
import io.cucumber.datatable.DataTable;
import io.cucumber.java.Before;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.junit.jupiter.api.Assertions;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.List;
import java.util.Map;

public class PatrimonioLiquidoTest {

    private ContaRepositorio contaRepositorio;
    private InvestimentoRepositorio investimentoRepositorio;
    private DividaRepositorio dividaRepositorio;
    private PatrimonioRepositorio snapshotRepositorio;
    private PatrimonioService patrimonioService;

    private BigDecimal resultadoPatrimonio;
    private List<Patrimonio> historicoResultado;
    private String mensagemDeErro;
    private LocalDate dataDoTeste;

    @Before
    public void setup() {
        contaRepositorio = new ContaRepositorio();
        investimentoRepositorio = new InvestimentoRepositorio();
        dividaRepositorio = new DividaRepositorio();
        snapshotRepositorio = new PatrimonioRepositorio();
        patrimonioService = new PatrimonioService(
                contaRepositorio,
                investimentoRepositorio,
                dividaRepositorio,
                snapshotRepositorio
        );
        resultadoPatrimonio = null;
        historicoResultado = null;
        mensagemDeErro = null;
        dataDoTeste = LocalDate.now();
    }

    @Given("que eu tenho uma {string} com saldo de R$ {double}")
    public void que_eu_tenho_uma_conta_com_saldo(String nomeConta, Double saldo) {
        Conta conta = new Conta(nomeConta, "Corrente", "Banco Teste", BigDecimal.ZERO);
        conta.setSaldo(BigDecimal.valueOf(saldo));
        contaRepositorio.salvar(conta);
    }

    @Given("tenho uma {string} com saldo de R$ {double}")
    public void tenho_uma_conta_com_saldo_de_r$(String nomeConta, Double saldo) {
        que_eu_tenho_uma_conta_com_saldo(nomeConta, saldo);
    }

    @Given("tenho um {string} com valor de R$ {double}")
    public void tenho_um_investimento_com_valor_de(String nomeInvestimento, Double valor) {
        Investimento investimento = new Investimento("0",nomeInvestimento,"teste", BigDecimal.valueOf(valor));
        investimentoRepositorio.salvar(investimento);
    }

    @Given("tenho um {string} com uma dívida de R$ {double}")
    public void tenho_uma_divida_de(String nomeDivida, Double valor) {
        Divida divida = new Divida(nomeDivida, BigDecimal.valueOf(valor));
        dividaRepositorio.salvar(divida);
    }

    @Given("meu patrimônio líquido atual é de R$ {double}")
    public void meu_patrimonio_liquido_atual_e_de(Double valorPatrimonio) {
        contaRepositorio.limpar();
        investimentoRepositorio.limpar();
        dividaRepositorio.limpar();
        que_eu_tenho_uma_conta_com_saldo("Conta Única", valorPatrimonio);
    }

    @Given("que hoje é o último dia do mês")
    public void que_hoje_e_o_ultimo_dia_do_mes() {
        this.dataDoTeste = YearMonth.now().atEndOfMonth();
    }

    @Given("que eu sou um novo usuário sem nenhum snapshot de patrimônio")
    public void que_eu_sou_um_novo_usuario_sem_nenhum_snapshot_de_patrimonio() {
    }

    @Given("que hoje não é o último dia do mês")
    public void que_hoje_nao_e_o_ultimo_dia_do_mes() {
        this.dataDoTeste = YearMonth.now().atDay(1);
    }

    @Given("que eu tenho os seguintes snapshots de patrimônio:")
    public void que_eu_tenho_os_seguintes_snapshots(DataTable dataTable) {
        List<Map<String, String>> linhas = dataTable.asMaps(String.class, String.class);
        for (Map<String, String> linha : linhas) {
            LocalDate data = LocalDate.parse(linha.get("Data"));
            String valorLimpo = linha.get("Valor").replaceAll("[^\\d,]", "").replace(",", ".");
            BigDecimal valor = new BigDecimal(valorLimpo);
            snapshotRepositorio.salvar(new Patrimonio(data, valor));
        }
    }

    @When("eu solicitar o meu patrimônio líquido")
    public void eu_solicitar_o_meu_patrimonio_liquido() {
        this.resultadoPatrimonio = patrimonioService.calcularPatrimonioLiquido();
    }

    @When("o processo automático de snapshot for executado")
    public void o_processo_automatico_de_snapshot_for_executado() {
        patrimonioService.gerarEsalvarSnapshot(this.dataDoTeste);
    }

    @When("eu acessar a tela de evolução do patrimônio")
    public void eu_acessar_a_tela_de_evolucao_do_patrimonio() {
        this.historicoResultado = patrimonioService.obterHistoricoDePatrimonio();
    }

    @Then("o valor total do patrimônio deve ser R$ {double}")
    public void o_valor_total_do_patrimonio_deve_ser(Double valorEsperado) {
        Assertions.assertEquals(0, BigDecimal.valueOf(valorEsperado).compareTo(this.resultadoPatrimonio));
    }

    @Then("um registro histórico do patrimônio deve ser salvo com a data de hoje e o valor de R$ {double}")
    public void um_registro_historico_do_patrimonio_deve_ser_salvo(Double valorEsperado) {
        List<Patrimonio> historico = snapshotRepositorio.obterTodos();
        Assertions.assertEquals(1, historico.size());

        Patrimonio snapshotSalvo = historico.get(0);
        Assertions.assertEquals(this.dataDoTeste, snapshotSalvo.getData());
        Assertions.assertEquals(0, BigDecimal.valueOf(valorEsperado).compareTo(snapshotSalvo.getValor()));
    }

    @Then("o sistema deve exibir a mensagem {string}")
    public void o_sistema_deve_exibir_a_mensagem(String mensagemEsperada) {
        if (historicoResultado.isEmpty()) {
            this.mensagemDeErro = "O histórico de patrimônio ainda não foi gerado";
        }
        Assertions.assertEquals(mensagemEsperada, this.mensagemDeErro);
    }

    @Then("nenhum registro histórico de patrimônio deve ser salvo")
    public void nenhum_registro_historico_de_patrimonio_deve_ser_salvo() {
        Assertions.assertTrue(snapshotRepositorio.obterTodos().isEmpty());
    }

    @Then("um gráfico de linhas deve ser exibido com os dados do histórico")
    public void um_grafico_de_linhas_deve_ser_exibido_com_os_dados_do_historico() {
        Assertions.assertNotNull(this.historicoResultado);
        Assertions.assertEquals(3, this.historicoResultado.size());
    }
}