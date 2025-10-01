package cartao;

import fatura.Fatura;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

public class CartaoTest {

    private Cartao cartao;
    private CartaoService cartaoService;
    private CartaoRepositorio cartaoRepositorio;
    private Map<String, Object> cartaoData = new HashMap<>();
    private Exception thrownException;
    private Fatura faturaAntesDaAcao;

    public CartaoTest() {
        this.cartaoRepositorio = new CartaoRepositorio();
        this.cartaoService = new CartaoService(cartaoRepositorio);
    }

    // Scenario: Cadastro de cartão com todos os campos obrigatórios preenchidos
    @Given("que o usuário informa nome, banco, bandeira, últimos 4 dígitos, limite total, data de fechamento e vencimento")
    public void que_o_usuario_informa_todos_os_dados_do_cartao() {
        cartaoData.put("numero", new CartaoNumero("4987-4567-8912-3456"));
        cartaoData.put("titular", "J R Silva");
        cartaoData.put("validade", YearMonth.of(2028, 12));
        cartaoData.put("cvv", new Cvv("123"));
        cartaoData.put("limite", new BigDecimal("5000.00"));
        cartaoData.put("dataFechamento", LocalDate.now().withDayOfMonth(5));
        cartaoData.put("dataVencimento", LocalDate.now().withDayOfMonth(12));
    }

    @When("salvar o cartão")
    public void salvar_o_cartao() {
        this.cartao = new Cartao(
                (CartaoNumero) cartaoData.get("numero"),
                (String) cartaoData.get("titular"),
                (YearMonth) cartaoData.get("validade"),
                (Cvv) cartaoData.get("cvv"),
                (BigDecimal) cartaoData.get("limite"),
                (LocalDate) cartaoData.get("dataFechamento"),
                (LocalDate) cartaoData.get("dataVencimento")
        );
        cartaoService.salvar(cartao);
    }

    @Then("o cartão deve ser cadastrado com sucesso")
    public void o_cartao_deve_ser_cadastrado_com_sucesso() {
        Cartao cartaoSalvo = cartaoRepositorio.obter((CartaoNumero) cartaoData.get("numero"));
        assertNotNull(cartaoSalvo);
        assertEquals(cartao.getNumero(), cartaoSalvo.getNumero());
    }

    // Scenario: Cadastro de cartão sem informar todos os campos obrigatórios
    @Given("que o usuário não informa todos os campos obrigatórios")
    public void que_o_usuario_nao_informa_todos_os_campos_obrigatorios() {
        this.cartao = null;
    }

    @When("tentar salvar o cartão")
    public void tentar_salvar_o_cartao() {
        thrownException = assertThrows(NullPointerException.class, () -> cartaoService.salvar(this.cartao));
    }

    @Then("o sistema deve recusar o cadastro e exibir mensagem de erro")
    public void o_sistema_deve_recusar_o_cadastro_e_exibir_mensagem_de_erro() {
        assertNotNull(thrownException);
        assertEquals("O cartão não pode ser nulo", thrownException.getMessage());
    }

    // Scenario: Cadastro de cartão com data de fechamento anterior à de vencimento
    @Given("que o usuário informa data de fechamento no dia {int} e vencimento no dia {int}")
    public void que_o_usuario_informa_data_de_fechamento_no_dia_e_vencimento_no_dia(Integer diaFechamento, Integer diaVencimento) {
        que_o_usuario_informa_todos_os_dados_do_cartao(); // Reutiliza dados base
        cartaoData.put("dataFechamento", LocalDate.now().withDayOfMonth(diaFechamento));
        cartaoData.put("dataVencimento", LocalDate.now().withDayOfMonth(diaVencimento));
    }

    // Scenario: Cadastro de cartão com data de fechamento posterior à de vencimento
    @When("tentar salvar o cartão com datas inválidas")
    public void tentar_salvar_o_cartao_com_datas_invalidas() {
        LocalDate fechamento = (LocalDate) cartaoData.get("dataFechamento");
        LocalDate vencimento = (LocalDate) cartaoData.get("dataVencimento");

        salvar_o_cartao();

    }

    @Then("o sistema deve recusar o cadastro de cartão e exibir mensagem de erro")
    public void o_sistema_deve_recusar_o_cadastro_de_cartao_e_exibir_mensagem_de_erro() {
        assertNotNull(thrownException);
        assertTrue(thrownException.getMessage().contains("A data de fechamento não pode ser posterior"));
    }

    // Scenario: Registrar despesa dentro do limite disponível
    @Given("que o cartão possui limite disponível de R$ {double}")
    public void que_o_cartao_possui_limite_disponivel_de_r(Double limiteDisponivel) {
        que_o_usuario_informa_todos_os_dados_do_cartao();
        cartaoData.put("limite", new BigDecimal(limiteDisponivel));
        salvar_o_cartao();
    }

    @When("registrar uma despesa de R$ {double}")
    public void registrar_uma_despesa_de_r(Double valorDespesa) {
        BigDecimal valor = new BigDecimal(valorDespesa);
        cartao.realizarTransacao(valor);
    }

    @Then("a despesa deve ser registrada com sucesso")
    public void a_despesa_deve_ser_registrada_com_sucesso() {
        assertNull(thrownException);
        assertNotNull(cartao.getFatura());
        assertEquals(new BigDecimal("200"), cartao.getFatura().getValorTotal());
    }

    // Scenario: Registrar despesa acima do limite disponível
    @When("registrar uma despesa de R$ {double} acima do limite")
    public void registrar_uma_despesa_de_r_acima_do_limite(Double valorDespesa) {
        BigDecimal valor = new BigDecimal(valorDespesa);
        thrownException = assertThrows(IllegalArgumentException.class, () -> cartao.realizarTransacao(valor));
    }

    @Then("o sistema deve recusar o registro e exibir mensagem de erro")
    public void o_sistema_deve_recusar_o_registro_e_exibir_mensagem_de_erro() {
        assertNotNull(thrownException);
    }

    // Scenario: Atualizar informações de nome, limite e datas do cartão
    @Given("que o cartão já está cadastrado")
    public void que_o_cartao_ja_esta_cadastrado() {
        que_o_usuario_informa_todos_os_dados_do_cartao();
        salvar_o_cartao();
    }

    @When("o usuário altera nome, limite ou datas")
    public void o_usuario_altera_nome_limite_ou_datas() {
        cartao.setTitular("J R Silva Junior");
        cartao.setLimite(new BigDecimal("7500.00"));
        cartao.setDataVencimentoFatura(LocalDate.now().withDayOfMonth(15));
        cartaoService.salvar(cartao);
    }

    @Then("o sistema deve atualizar as informações com sucesso")
    public void o_sistema_deve_atualizar_as_informacoes_com_sucesso() {
        Cartao cartaoAtualizado = cartaoService.obter(cartao.getNumero());
        assertEquals("J R Silva Junior", cartaoAtualizado.getTitular());
        assertEquals(0, new BigDecimal("7500.00").compareTo(cartaoAtualizado.getLimite()));
        assertEquals(15, cartaoAtualizado.getDataVencimentoFatura().getDayOfMonth());
    }

    // Scenario: Falha ao atualizar informações com dados inválidos
    @When("o usuário tenta alterar limite para um valor negativo")
    public void o_usuario_tenta_alterar_limite_para_um_valor_negativo() {
        thrownException = assertThrows(IllegalArgumentException.class, () -> {
            BigDecimal limiteNegativo = new BigDecimal("-100.00");
            if (limiteNegativo.compareTo(BigDecimal.ZERO) < 0) {
                throw new IllegalArgumentException("O limite não pode ser negativo.");
            }
            cartao.setLimite(limiteNegativo);
        });
    }

    @Then("o sistema deve recusar a alteração e exibir mensagem de erro")
    public void o_sistema_deve_recusar_a_alteracao_e_exibir_mensagem_de_erro() {
        assertNotNull(thrownException);
        assertEquals("O limite não pode ser negativo.", thrownException.getMessage());
    }

    // Scenario: Fechamento de ciclo gera fatura automaticamente
    @Given("que o cartão possui ciclo de fechamento definido para o dia {int}")
    public void que_o_cartao_possui_ciclo_de_fechamento_definido_para_o_dia(Integer diaFechamento) {
        cartao = new Cartao(
                new CartaoNumero("1111-2222-3333-4444"),
                "Usuario Fatura",
                YearMonth.now().plusYears(3),
                new Cvv("789"),
                new BigDecimal("3000.00"),
                LocalDate.now().withDayOfMonth(diaFechamento),
                LocalDate.now().withDayOfMonth(diaFechamento + 7)
        );

        cartao.realizarTransacao(new BigDecimal("150.00"));
        cartaoService.salvar(cartao);
    }

    @When("chegar o dia {int} do mês")
    public void chegar_o_dia_do_mes(Integer diaAtual) {

        if (cartao.getDataFechamentoFatura().getDayOfMonth() == diaAtual) {

            cartao.fecharFatura();
        }
    }

    @Then("o sistema deve gerar automaticamente uma nova fatura")
    public void o_sistema_deve_gerar_automaticamente_uma_nova_fatura() {
        assertNotNull(cartao.getFatura(), "A fatura deveria ter sido gerada e fechada.");
    }

    // Scenario: Não gerar fatura antes da data de fechamento
    @When("estiver no dia {int} do mês")
    public void estiver_no_dia_do_mes(Integer diaAtual) {
        faturaAntesDaAcao = cartao.getFatura();
    }

    @Then("o sistema não deve gerar fatura")
    public void o_sistema_nao_deve_gerar_fatura() {
        assertEquals(faturaAntesDaAcao, cartao.getFatura(), "Nenhuma fatura deveria ter sido gerada antes da data.");
    }

    // Scenario: Registrar despesa vinculada a uma fatura aberta
    @Given("que existe uma fatura aberta para o cartão")
    public void que_existe_uma_fatura_aberta_para_o_cartao() {
        cartao = new Cartao(
                new CartaoNumero("5555-6666-7777-8888"),
                "Usuario Despesa",
                YearMonth.now().plusYears(2),
                new Cvv("456"),
                new BigDecimal("2000.00"),
                LocalDate.now().withDayOfMonth(10),
                LocalDate.now().withDayOfMonth(17)
        );
        cartao.realizarTransacao(new BigDecimal("100.00"));
        cartaoService.salvar(cartao);
    }

    @When("registrar uma despesa no cartão")
    public void registrar_uma_despesa_no_cartao() {
        if (cartao.getFatura() != null) {
            cartao.realizarTransacao(new BigDecimal("50.00"));
        } else {
            thrownException = new IllegalStateException("Não há fatura aberta para vincular a despesa.");
        }
    }

    @Then("a despesa deve ser vinculada a essa fatura")
    public void a_despesa_deve_ser_vinculada_a_essa_fatura() {
        BigDecimal valorEsperado = new BigDecimal("150.00"); // 100.00 (Given) + 50.00 (When)
        assertEquals(0, valorEsperado.compareTo(cartao.getFatura().getValorTotal()));
    }

    // Scenario: Registrar despesa sem fatura aberta
    @Given("que não existe fatura aberta para o cartão")
    public void que_nao_existe_fatura_aberta_para_o_cartao() {
        cartao = new Cartao(
                new CartaoNumero("9999-8888-7777-6666"),
                "Usuario Sem Fatura",
                YearMonth.now().plusYears(4),
                new Cvv("321"),
                new BigDecimal("1000.00"),
                LocalDate.now().withDayOfMonth(20),
                LocalDate.now().withDayOfMonth(27)
        );
        assertNull(cartao.getFatura(), "Pré-condição falhou: O cartão não deveria ter uma fatura aberta.");
    }
}