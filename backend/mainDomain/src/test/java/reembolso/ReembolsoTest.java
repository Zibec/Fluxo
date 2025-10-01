package reembolso;

import io.cucumber.java.Before;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.junit.jupiter.api.Assertions;
import orcamento.OrcamentoService;
import orcamento.OrcamentoRepositorio;
import transacao.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.UUID;

public class ReembolsoTest {

    private TransacaoRepositorio transacaoRepo;
    private OrcamentoRepositorio orcamentoRepo;
    private TransacaoService transacaoService;
    private OrcamentoService orcamentoService;
    private ReembolsoService reembolsoService;

    private Exception excecaoCapturada;
    private Transacao reembolsoResultado;
    private BigDecimal totalReceitasAntes;

    @Before
    public void setup() {
        transacaoRepo = new TransacaoRepositorio();
        orcamentoRepo = new OrcamentoRepositorio();
        transacaoService = new TransacaoService(transacaoRepo);
        orcamentoService = new OrcamentoService(orcamentoRepo, transacaoService);
        reembolsoService = new ReembolsoService(transacaoRepo);
        excecaoCapturada = null;
        reembolsoResultado = null;
        totalReceitasAntes = null;
    }

    @Given("que registrei uma despesa original com ID {string} de R$ {double} na categoria {string}")
    public void que_registrei_uma_despesa_original_com_id_de_na_categoria(String id, Double valor, String categoria) {
        Transacao despesa = new Transacao(id, null, "Despesa Teste", BigDecimal.valueOf(valor), LocalDate.now(), StatusTransacao.EFETIVADA, categoria, Transacao.Tipo.DESPESA);
        transacaoRepo.salvar(despesa);
    }

    @Given("que estou na tela de registro de reembolso")
    public void que_estou_na_tela_de_registro_de_reembolso() {
    }

    @Given("que uma despesa original com ID {string} tem o valor de R$ {double} na categoria {string} em {string}")
    public void que_uma_despesa_original_com_id_tem_o_valor_de_r_na_categoria_em(String id, Double valor, String categoria, String mesAno) {
        LocalDate data = YearMonth.parse(mesAno, DateTimeFormatter.ofPattern("MM/yyyy")).atDay(1);
        Transacao despesa = new Transacao(id, null, "Despesa Original Teste", BigDecimal.valueOf(valor), data, StatusTransacao.EFETIVADA, categoria, Transacao.Tipo.DESPESA);
        transacaoRepo.salvar(despesa);
    }

    @Given("que outra despesa com ID {string} tem o valor de R$ {double} na categoria {string} em {string}")
    public void que_outra_despesa_com_id_tem_o_valor_de_r_na_categoria_em(String id, Double valor, String categoria, String mesAno) {
        que_uma_despesa_original_com_id_tem_o_valor_de_r_na_categoria_em(id, valor, categoria, mesAno);
    }

    @Given("que tenho um orçamento de R$ {double} para {string} em {string}")
    public void que_tenho_um_orcamento_de_r_para_em(Double limite, String categoria, String mesAno) {
        orcamentoService.criarOrcamentoMensal("usuario-teste", categoria, YearMonth.parse(mesAno, DateTimeFormatter.ofPattern("MM/yyyy")), BigDecimal.valueOf(limite));
    }

    @Given("que eu tenho uma despesa original com ID {string} de R$ {double}")
    public void que_eu_tenho_uma_despesa_original_com_id_de_r(String id, Double valor) {
        Transacao despesa = new Transacao(id, null, "Despesa Teste", BigDecimal.valueOf(valor), LocalDate.now(), StatusTransacao.EFETIVADA, "Categoria Teste", Transacao.Tipo.DESPESA);
        transacaoRepo.salvar(despesa);
    }

    @Given("que uma despesa original com ID {string} tem o valor de R$ {double} na categoria {string}")
    public void que_uma_despesa_original_com_id_tem_o_valor_de_r_na_categoria(String id, Double valor, String categoria) {
        que_registrei_uma_despesa_original_com_id_de_na_categoria(id, valor, categoria);
    }

    @Given("o total de receitas do usuário é de R$ {double}")
    public void o_total_de_receitas_do_usuario_e_de_r(Double totalReceitas) {
        this.totalReceitasAntes = BigDecimal.valueOf(totalReceitas);
        Transacao receita = new Transacao(UUID.randomUUID().toString(), null, "Salário", this.totalReceitasAntes, LocalDate.now(), StatusTransacao.EFETIVADA, "Salário", Transacao.Tipo.RECEITA);
        transacaoRepo.salvar(receita);
    }

    @When("eu registrar um reembolso de R$ {double} e o vincular à despesa com ID {string}")
    public void eu_registrar_um_reembolso_e_o_vincular_a_despesa_com_id(Double valorReembolso, String idDespesa) {
        try {
            this.reembolsoResultado = reembolsoService.registrarReembolso(BigDecimal.valueOf(valorReembolso), idDespesa);
        } catch (Exception e) {
            this.excecaoCapturada = e;
        }
    }

    @When("eu registrar um reembolso de R$ {double} vinculado à despesa com ID {string}")
    public void eu_registrar_um_reembolso_vinculado_a_despesa_com_id(Double valorReembolso, String idDespesa) {
        eu_registrar_um_reembolso_e_o_vincular_a_despesa_com_id(valorReembolso, idDespesa);
    }

    @When("eu tento registrar um reembolso de R$ {double} sem selecionar uma despesa original")
    public void eu_tento_registrar_um_reembolso_de_r_sem_selecionar_uma_despesa_original(Double valorReembolso) {
        try {
            reembolsoService.registrarReembolso(BigDecimal.valueOf(valorReembolso), null);
        } catch (Exception e) {
            this.excecaoCapturada = e;
        }
    }

    @When("eu receber um reembolso de R$ {double} vinculado à despesa com ID {string}")
    public void eu_receber_um_reembolso_de_r_vinculado_a_despesa_com_id(Double valorReembolso, String idDespesa) {
        eu_registrar_um_reembolso_e_o_vincular_a_despesa_com_id(valorReembolso, idDespesa);
    }

    @When("eu tento registrar um reembolso de R$ {double} para a despesa com ID {string}")
    public void eu_tento_registrar_um_reembolso_de_r_para_a_despesa_com_id(Double valorReembolso, String idDespesa) {
        try {
            // --- CORREÇÃO FINAL AQUI ---
            this.reembolsoResultado = reembolsoService.registrarReembolso(BigDecimal.valueOf(valorReembolso), idDespesa);
        } catch (Exception e) {
            this.excecaoCapturada = e;
        }
    }

    @Then("o sistema deve criar uma nova transação do tipo {string}")
    public void o_sistema_deve_criar_uma_nova_transacao_do_tipo(String tipo) {
        Assertions.assertNotNull(reembolsoResultado, "Nenhum reembolso foi criado.");
        Assertions.assertEquals(Transacao.Tipo.valueOf(tipo.toUpperCase()), reembolsoResultado.getTipo());
    }

    @Then("o reembolso criado deve estar vinculado à despesa com ID {string}")
    public void o_reembolso_criado_deve_estar_vinculado_a_despesa_com_id(String idDespesaOriginal) {
        Assertions.assertEquals(idDespesaOriginal, reembolsoResultado.getTransacaoOriginalId());
    }

    @Then("o sistema de reembolso deve exibir a mensagem de erro {string}")
    public void o_sistema_de_reembolso_deve_exibir_a_mensagem_de_erro(String mensagemEsperada) {
        Assertions.assertNotNull(excecaoCapturada, "Uma exceção era esperada, mas não foi lançada.");
        Assertions.assertEquals(mensagemEsperada, excecaoCapturada.getMessage());
    }

    @Then("o total de gastos na categoria {string} em {string} deve ser de R$ {double}")
    public void o_total_de_gastos_na_categoria_em_deve_ser_de_r(String categoria, String mesAnoStr, Double valorEsperado) {
        YearMonth mesAno = YearMonth.parse(mesAnoStr, DateTimeFormatter.ofPattern("MM/yyyy"));
        BigDecimal totalCalculado = transacaoService.calcularGastosConsolidadosPorCategoria(categoria, mesAno);
        Assertions.assertEquals(0, BigDecimal.valueOf(valorEsperado).compareTo(totalCalculado));
    }

    @Then("o valor disponível no meu orçamento de {string} em {string} deve ser de R$ {double}")
    public void o_valor_disponivel_no_meu_orcamento_de_em_deve_ser_de_r(String categoria, String mesAnoStr, Double valorEsperado) {
        YearMonth mesAno = YearMonth.parse(mesAnoStr, DateTimeFormatter.ofPattern("MM/yyyy"));
        BigDecimal saldoDisponivel = orcamentoService.saldoMensalTotal("usuario-teste", categoria, mesAno);
        Assertions.assertEquals(0, BigDecimal.valueOf(valorEsperado).compareTo(saldoDisponivel));
    }

    @Then("o total de receitas do usuário deve permanecer R$ {double}")
    public void o_total_de_receitas_do_usuario_deve_permanecer_r(Double valorReceitaEsperado) {
        List<Transacao> transacoes = transacaoRepo.listarTodas();
        BigDecimal totalReceitasAtual = transacoes.stream()
                .filter(t -> t.getTipo() == Transacao.Tipo.RECEITA)
                .map(Transacao::getValor)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        Assertions.assertEquals(0, BigDecimal.valueOf(valorReceitaEsperado).compareTo(totalReceitasAtual));
    }

    @Then("o valor disponível no meu orçamento de {string} em {string} não deve ser R$ {double}")
    public void o_valor_disponivel_no_meu_orcamento_de_em_nao_deve_ser_r(String categoria, String mesAnoStr, Double valorInesperado) {
        YearMonth mesAno = YearMonth.parse(mesAnoStr, DateTimeFormatter.ofPattern("MM/yyyy"));
        BigDecimal saldoDisponivel = orcamentoService.saldoMensalTotal("usuario-teste", categoria, mesAno);
        Assertions.assertNotEquals(0, BigDecimal.valueOf(valorInesperado).compareTo(saldoDisponivel));
    }
}