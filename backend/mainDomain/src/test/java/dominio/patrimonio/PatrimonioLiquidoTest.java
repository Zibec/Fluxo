package dominio.patrimonio;

import conta.Conta;
import conta.ContaRepositorio;
import conta.ContaService;
import divida.Divida;
import divida.DividaRepositorio;
import infraestrutura.persistencia.memoria.Repositorio;
import investimento.Investimento;
import investimento.InvestimentoRepositorio;
import investimento.InvestimentoService;
import io.cucumber.datatable.DataTable;
import io.cucumber.java.Before;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.junit.jupiter.api.Assertions;
import patrimonio.Patrimonio;
import patrimonio.PatrimonioRepositorio;
import patrimonio.PatrimonioService;
import divida.DividaService;
import historicoInvestimento.HistoricoInvestimentoRepositorio;
import taxaSelic.TaxaSelicRepository;

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
    private InvestimentoService  investimentoService;
    private ContaService contaService;
    private DividaService dividaService;
    private HistoricoInvestimentoRepositorio historicoInvestimentoRepositorio;
    private TaxaSelicRepository taxaSelicRepository;

    private BigDecimal resultadoPatrimonio;
    private List<Patrimonio> historicoResultado;
    private String mensagemDeErro;
    private LocalDate dataDoTeste;
    private Exception excecaoCapturada;

    @Before
    public void setup() {
        contaRepositorio = new Repositorio();
        investimentoRepositorio = new Repositorio();
        dividaRepositorio = new Repositorio();
        snapshotRepositorio = new Repositorio();
        taxaSelicRepository = new Repositorio();
        historicoInvestimentoRepositorio = new Repositorio();

        contaService = new ContaService(contaRepositorio);
        investimentoService = new InvestimentoService(investimentoRepositorio, taxaSelicRepository, historicoInvestimentoRepositorio);
        dividaService = new DividaService(dividaRepositorio);

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
        excecaoCapturada = null;
    }

    @Given("que eu tenho uma {string} com saldo de R$ {double}")
    public void que_eu_tenho_uma_conta_com_saldo(String nomeConta, Double saldo) {
        Conta conta = new Conta(nomeConta, "Corrente", "Banco Teste", BigDecimal.ZERO);
        conta.setSaldo(BigDecimal.valueOf(saldo));
        contaService.salvar(conta);
    }

    @Given("tenho uma {string} com saldo de R$ {double}")
    public void tenho_uma_conta_com_saldo_de_r$(String nomeConta, Double saldo) {
        que_eu_tenho_uma_conta_com_saldo(nomeConta, saldo);
    }

    @Given("tenho um {string} com valor de R$ {double}")
    public void tenho_um_investimento_com_valor_de(String nomeInvestimento, Double valor) {
        Investimento investimento = new Investimento("0",nomeInvestimento,"teste", BigDecimal.valueOf(valor));
        investimentoService.salvar(investimento);
    }

    @Given("um {string} com valor não definido \\(nulo)")
    public void um_investimento_com_valor_nao_definido_nulo(String nomeInvestimento) {
        try {
            Investimento investimentoInvalido = new Investimento("invNuloId", nomeInvestimento, "teste nulo", null);
            investimentoService.salvar(investimentoInvalido);
        } catch (IllegalArgumentException e) {
            this.excecaoCapturada = e;
            this.mensagemDeErro = e.getMessage();
        }
    }


    @Given("tenho um {string} com uma dívida de R$ {double}")
    public void tenho_uma_divida_de(String nomeDivida, Double valor) {
        Divida divida = new Divida(nomeDivida, BigDecimal.valueOf(valor));
        dividaService.salvar(divida);
    }

    @Given("meu patrimônio líquido atual é de R$ {double}")
    public void meu_patrimonio_liquido_atual_e_de(Double valorPatrimonio) {
        contaService.limparConta();
        investimentoService.limparInvestimento();
        dividaService.limparDivida();
        que_eu_tenho_uma_conta_com_saldo("Conta Única", valorPatrimonio);
    }

    @Given("que hoje é o último dia do mês")
    public void que_hoje_e_o_ultimo_dia_do_mes() {
        this.dataDoTeste = YearMonth.now().atEndOfMonth();
    }

    @Given("que eu sou um novo usuário sem nenhum snapshot de patrimônio")
    public void que_eu_sou_um_novo_usuario_sem_nenhum_snapshot_de_patrimonio() {
        // Nada a fazer
    }

    @Given("que hoje não é o último dia do mês")
    public void que_hoje_nao_e_o_ultimo_dia_do_mes() {
        this.dataDoTeste = YearMonth.now().atDay(1);
        if (this.dataDoTeste.equals(YearMonth.now().atEndOfMonth())) {
            this.dataDoTeste = this.dataDoTeste.minusDays(1);
        }
    }

    @Given("que eu tenho os seguintes snapshots de patrimônio:")
    public void que_eu_tenho_os_seguintes_snapshots(DataTable dataTable) {
        List<Map<String, String>> linhas = dataTable.asMaps(String.class, String.class);
        for (Map<String, String> linha : linhas) {
            LocalDate data = LocalDate.parse(linha.get("Data"));
            String valorLimpo = linha.get("Valor").replaceAll("[^\\d,.-]", "").replace(".", "").replace(",", ".");
            BigDecimal valor = new BigDecimal(valorLimpo);
            patrimonioService.salvarSnapshot(new Patrimonio(data, valor));
        }
    }

    @When("eu solicitar o meu patrimônio líquido")
    public void eu_solicitar_o_meu_patrimonio_liquido() {
        this.resultadoPatrimonio = patrimonioService.calcularPatrimonioLiquido("");
    }

    @When("eu tentar solicitar o meu patrimônio líquido")
    public void eu_tentar_solicitar_o_meu_patrimonio_liquido() {
        if (this.excecaoCapturada == null) {
            try {
                this.resultadoPatrimonio = patrimonioService.calcularPatrimonioLiquido("");
            } catch (Exception e) {
                this.excecaoCapturada = e;
                this.mensagemDeErro = e.getMessage() != null ? e.getMessage() : "Erro inesperado ao calcular patrimônio.";
            }
        }
    }


    @When("o processo automático de snapshot for executado")
    public void o_processo_automatico_de_snapshot_for_executado() {
        try {
            patrimonioService.gerarEsalvarSnapshot(this.dataDoTeste, "");
        } catch (Exception e) {
            this.excecaoCapturada = e; // Guarda caso algo inesperado ocorra
            this.mensagemDeErro = "Erro inesperado ao tentar gerar snapshot: " + e.getMessage();
        }
    }

    @When("eu acessar a tela de evolução do patrimônio")
    public void eu_acessar_a_tela_de_evolucao_do_patrimonio() {
        try {
            this.historicoResultado = patrimonioService.obterHistoricoDePatrimonio();
        } catch (Exception e) {
            this.excecaoCapturada = e; // Guarda caso algo inesperado ocorra
            this.mensagemDeErro = "Erro inesperado ao obter histórico: " + e.getMessage();
        }
    }

    @Then("o valor total do patrimônio deve ser R$ {double}")
    public void o_valor_total_do_patrimonio_deve_ser(Double valorEsperado) {
        Assertions.assertNotNull(resultadoPatrimonio, "O resultado do patrimônio não deveria ser nulo neste cenário.");
        Assertions.assertEquals(0, BigDecimal.valueOf(valorEsperado).compareTo(this.resultadoPatrimonio),
                "Valor do patrimônio esperado: " + valorEsperado + " mas foi: " + this.resultadoPatrimonio);
    }

    @Then("um registro histórico do patrimônio deve ser salvo com a data de hoje e o valor de R$ {double}")
    public void um_registro_historico_do_patrimonio_deve_ser_salvo(Double valorEsperado) {
        List<Patrimonio> historico = patrimonioService.obterHistoricoDePatrimonio();
        Assertions.assertFalse(historico.isEmpty(), "Nenhum snapshot foi salvo, mas era esperado um.");
        Assertions.assertEquals(1, historico.size(), "Era esperado apenas 1 snapshot, mas foram encontrados: " + historico.size());

        Patrimonio snapshotSalvo = historico.get(0);
        Assertions.assertEquals(this.dataDoTeste, snapshotSalvo.getData(), "A data do snapshot salvo está incorreta.");
        Assertions.assertEquals(0, BigDecimal.valueOf(valorEsperado).compareTo(snapshotSalvo.getValor()),
                "Valor do snapshot esperado: " + valorEsperado + " mas foi: " + snapshotSalvo.getValor());
    }

    @Then("o sistema deve exibir a mensagem {string}")
    public void o_sistema_deve_exibir_a_mensagem(String mensagemEsperada) {
        if (historicoResultado != null && historicoResultado.isEmpty() && excecaoCapturada == null) {
            this.mensagemDeErro = "O histórico de patrimônio ainda não foi gerado";
        }
        Assertions.assertEquals(mensagemEsperada, this.mensagemDeErro, "A mensagem de erro exibida está incorreta.");
    }

    @Then("o sistema de patrimônio deve lançar uma exceção com a mensagem {string}")
    public void o_sistema_de_patrimonio_deve_lancar_uma_excecao_com_a_mensagem(String mensagemEsperada) {
        Assertions.assertNotNull(excecaoCapturada, "Uma exceção era esperada, mas não foi lançada.");
        Assertions.assertEquals(mensagemEsperada, this.mensagemDeErro, "A mensagem da exceção capturada está incorreta.");
    }

    @Then("nenhum registro histórico de patrimônio deve ser salvo")
    public void nenhum_registro_historico_de_patrimonio_deve_ser_salvo() {
        Assertions.assertTrue(patrimonioService.obterHistoricoDePatrimonio().isEmpty(), "Um snapshot foi salvo, mas não era esperado.");
    }

    @Then("um gráfico de linhas deve ser exibido com os dados do histórico")
    public void um_grafico_de_linhas_deve_ser_exibido_com_os_dados_do_historico() {
        Assertions.assertNotNull(this.historicoResultado, "O resultado do histórico não deveria ser nulo.");
        Assertions.assertFalse(this.historicoResultado.isEmpty(), "Era esperado histórico para exibir o gráfico, mas a lista está vazia.");
    }
}