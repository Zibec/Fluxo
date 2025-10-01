package orcamento;

import io.cucumber.java.Before;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.cucumber.java.en.And;
import org.junit.jupiter.api.Assertions;
import transacao.Transacao;
import transacao.TransacaoRepositorio;
import transacao.TransacaoService;
import transacao.StatusTransacao;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.Normalizer;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.Optional;
import java.util.UUID;

public class OrcamentoTest {

    private OrcamentoRepositorio repo;
    private TransacaoRepositorio transacaoRepo;
    private TransacaoService transacaoService;
    private OrcamentoService service;
    private Notificador notificador;

    private String usuario;
    private String categoria;
    private YearMonth anoMes;
    private Throwable erro;
    private String mensagemSistema;
    private OrcamentoChave ultimaChaveVerificada;
    private BigDecimal ultimoTotalCalculado;

    private static class Notificador {
        private String ultimaMensagem;
        public void notificar(String msg) { ultimaMensagem = msg; }
        public Optional<String> ultima() { return Optional.ofNullable(ultimaMensagem); }
        public void limpar() { ultimaMensagem = null; }
    }

    @Before
    public void setup() {
        repo = new OrcamentoRepositorio();
        transacaoRepo = new TransacaoRepositorio();
        transacaoService = new TransacaoService(transacaoRepo);
        service = new OrcamentoService(repo, transacaoService);
        notificador = new Notificador();
        erro = null;
        mensagemSistema = null;
    }

    @Given("que existe a categoria {string} para um usuário autenticado como {string}")
    public void existeCategoriaUsuario(String categoria, String usuario) {
        this.usuario = usuario;
        this.categoria = categoria;
    }

    @When("o usuário define um orçamento de {string} para a categoria {string} no mês {string}")
    public void defineOrcamento(String valorMoeda, String categoria, String mesAno) {
        var valor = parseMoedaBR(valorMoeda);
        var ym = parseAnoMes(mesAno);
        this.categoria = categoria;
        this.anoMes = ym;
        try {
            service.criarOrcamentoMensal(usuario, categoria, ym, valor);
            mensagemSistema = "Criado com sucesso";
        } catch (Throwable t) {
            erro = t;
            mensagemSistema = t.getMessage();
        }
    }

    @Then("o usuário deve ver o orçamento salvo para {string} em {string} com valor {string}")
    public void deveVerOrcamentoSalvoParaCategoriaMesValor(String categoriaEsperada, String mesAno, String valorEsperado) {
        var ym = parseAnoMes(mesAno);
        var chave = new OrcamentoChave(usuario, ym, categoria);
        var opt = repo.obterOrcamento(chave);
        Assertions.assertTrue(opt.isPresent(), "Orçamento não foi salvo");
        Assertions.assertEquals(0, parseMoedaBR(valorEsperado).compareTo(opt.get().getLimite()));
    }

    @Given("existe um orçamento de {string} para {string} em {string}")
    public void existeUmOrcamento(String valorMoeda, String categoria, String mesAno) {
        var ym = parseAnoMes(mesAno);
        var valor = parseMoedaBR(valorMoeda);
        var chave = new OrcamentoChave(usuario, ym, categoria);
        repo.salvarNovo(chave, new Orcamento(valor));
    }

    @When("o usuário tenta definir um orçamento de {string} para {string} em {string}")
    public void usuarioTentaDefinirDuplicado(String valorMoeda, String categoria, String mesAno) {
        var ym = parseAnoMes(mesAno);
        var valor = parseMoedaBR(valorMoeda);
        try {
            service.criarOrcamentoMensal(usuario, categoria, ym, valor);
        } catch (Throwable t) {
            erro = t;
            mensagemSistema = t.getMessage();
        }
    }

    @When("o usuário tenta definir um orçamento de {string} para a categoria {string} no mês {string}")
    public void usuarioTentaDefinirComValidacao(String valorMoeda, String categoria, String mesAno) {
        var ym = parseAnoMes(mesAno);
        var valor = parseMoedaBR(valorMoeda);
        try {
            service.criarOrcamentoMensal(usuario, categoria, ym, valor);
        } catch (Throwable t) {
            erro = t;
            mensagemSistema = t.getMessage();
        }
    }

    @Then("o sistema deve retornar {string}")
    public void sistemaDeveRetornar(String mensagemEsperada) {
        Assertions.assertEquals(mensagemEsperada, mensagemSistema);
    }

    @And("o orçamento não deve ser salvo")
    public void orcamentoNaoDeveSerSalvo() {
        Assertions.assertNotNull(erro, "Era esperado erro");
    }

    @Given("que existe um orçamento na categoria {string} para o mês {string} de {string}")
    public void existeUmOrcamentoNaCategoriaParaOMes(String categoria, String mesAno, String valorMoeda) {
        if (this.usuario == null) this.usuario = "Gabriel";
        var ym = parseAnoMes(mesAno);
        var valor = parseMoedaBR(valorMoeda);
        var chave = new OrcamentoChave(usuario, ym, categoria);
        repo.salvarNovo(chave, new Orcamento(valor));
        this.categoria = categoria;
        this.anoMes = ym;
    }

    @When("o usuário atualiza esse orçamento para {string}")
    public void usuarioAtualizaEsseOrcamentoPara(String novoValorMoeda) {
        if (this.usuario == null) this.usuario = "Gabriel";
        var novoValor = parseMoedaBR(novoValorMoeda);
        try {
            service.atualizarOrcamentoMensal(usuario, categoria, anoMes, novoValor);
            mensagemSistema = "Atualizado com sucesso";
        } catch (Throwable t) {
            erro = t;
            mensagemSistema = t.getMessage();
        }
    }

    @Then("o usuário deve ver o orçamento salvo com valor {string}")
    public void usuarioDeveVerOrcamentoSalvoComValor(String valorEsperado) {
        if (this.usuario == null) this.usuario = "Gabriel";
        var chave = new OrcamentoChave(usuario, anoMes, categoria);
        var opt = repo.obterOrcamento(chave);
        Assertions.assertTrue(opt.isPresent(), "Orçamento não encontrado após atualizar");
        Assertions.assertEquals(0, parseMoedaBR(valorEsperado).compareTo(opt.get().getLimite()));
    }

    @Given("que existe uma categoria {string} com gasto limite de {string} para o mês {string}")
    public void existeCategoriaComLimiteParaOMes(String categoria, String valorMoeda, String mesAno) {
        if (this.usuario == null) this.usuario = "Gabriel";
        this.categoria = categoria;
        this.anoMes = parseAnoMes(mesAno);
        var valor = parseMoedaBR(valorMoeda);
        var chave = new OrcamentoChave(usuario, anoMes, categoria);
        repo.salvarNovo(chave, new Orcamento(valor));
        notificador.limpar();
    }

    @Given("existe uma categoria {string} com gasto limite de {string} para o mês {string}")
    public void existeUmaCategoriaComGastoLimiteDeParaOMes(String categoria, String valorMoeda, String mesAno) {
        existeCategoriaComLimiteParaOMes(categoria, valorMoeda, mesAno);
    }

    @And("o gasto acumulado do usuário para {string} em {string} é de {string}")
    public void gastoAcumuladoDoUsuarioEh(String categoria, String mesAno, String valorMoeda) {
        if (this.usuario == null) this.usuario = "Gabriel";
        var ym = parseAnoMes(mesAno);
        Transacao despesa = new Transacao(UUID.randomUUID().toString(), null, "Gasto acumulado", BigDecimal.valueOf(Double.parseDouble(valorMoeda.replace("R$ ", "").replace(",", "."))), ym.atDay(1), StatusTransacao.EFETIVADA, categoria, Transacao.Tipo.DESPESA);
        transacaoRepo.salvar(despesa);
    }

    @When("o usuário registra uma despesa de {string} na categoria {string} em {string}")
    public void usuarioRegistraUmaDespesa(String valorDespesa, String categoria, String mesAno) {
        if (this.usuario == null) this.usuario = "Gabriel";
        var ym = parseAnoMes(mesAno);
        var chave = new OrcamentoChave(usuario, ym, categoria);
        var antes = transacaoService.calcularGastosConsolidadosPorCategoria(categoria, ym);
        var orc = repo.obterOrcamento(chave).orElseThrow(() -> new IllegalArgumentException("Orçamento não encontrado"));
        var limite = orc.getLimite();
        Transacao novaDespesa = new Transacao(UUID.randomUUID().toString(), null, "Nova despesa", parseMoedaBR(valorDespesa), ym.atDay(15), StatusTransacao.EFETIVADA, categoria, Transacao.Tipo.DESPESA);
        transacaoRepo.salvar(novaDespesa);
        var depois = transacaoService.calcularGastosConsolidadosPorCategoria(categoria, ym);
        var oitenta = limite.multiply(BigDecimal.valueOf(0.8));
        if (antes.compareTo(oitenta) < 0 && depois.compareTo(oitenta) >= 0 && depois.compareTo(limite) < 0) {
            notificador.notificar("Você atingiu 80% do limite definido");
        } else if (antes.compareTo(limite) < 0 && depois.compareTo(limite) == 0) {
            notificador.notificar("Você atingiu 100% do limite definido");
        } else if (depois.compareTo(limite) > 0) {
            notificador.notificar("Você excedeu o limite desta categoria");
        }
    }

    @Then("o sistema deve enviar ao usuário uma notificação {string}")
    public void sistemaDeveEnviarNotificacao(String esperado) {
        var msg = notificador.ultima().orElse(null);
        Assertions.assertEquals(esperado, msg, "Mensagem de notificação diferente do esperado");
    }

    @Then("o sistema não deve notificar o usuário")
    public void sistemaNaoDeveNotificar() {
        Assertions.assertTrue(notificador.ultima().isEmpty(), "Não era para notificar");
    }

    @And("o orçamento não deve ser aplicado")
    public void orcamentoNaoDeveSerAplicado() {
        Assertions.assertNotNull(erro, "Era esperado falhar valor negativo");
    }

    @Then("o sistema deve mostrar que o total gasto para {string} em {string} é de {string}")
    public void sistemaDeveMostrarQueOTotalGastoParaEmEDe(String categoria, String mesAno, String esperado) {
        if (this.usuario == null) this.usuario = "Gabriel";
        YearMonth ym = parseAnoMes(mesAno);
        BigDecimal totalCalculado = transacaoService.calcularGastosConsolidadosPorCategoria(categoria, ym);
        BigDecimal totalEsperado  = parseMoedaBR(esperado);
        this.ultimaChaveVerificada = new OrcamentoChave(usuario, ym, categoria);
        this.ultimoTotalCalculado  = totalCalculado;
        Assertions.assertEquals(0, totalEsperado.compareTo(totalCalculado), "Total gasto diferente do esperado");
    }

    @Then("o sistema deve mostrar que o total gasto para {string} em {string} permanece {string}")
    public void sistemaDeveMostrarQueOTotalGastoParaEmPermanece(String categoria, String mesAno, String esperado) {
        sistemaDeveMostrarQueOTotalGastoParaEmEDe(categoria, mesAno, esperado);
    }

    @Then("o sistema deve mostrar que o progresso de uso do orçamento é {string}")
    public void sistemaDeveMostrarQueOProgressoDeUsoDoOrcamentoE(String esperadoPercent) {
        Assertions.assertNotNull(ultimaChaveVerificada, "Chame primeiro o passo que valida o total gasto.");
        var orc = repo.obterOrcamento(ultimaChaveVerificada).orElseThrow(() -> new IllegalArgumentException("Orçamento não encontrado"));
        BigDecimal limite = orc.getLimite();
        BigDecimal progresso = limite.compareTo(BigDecimal.ZERO) == 0 ? BigDecimal.ZERO : ultimoTotalCalculado.multiply(BigDecimal.valueOf(100)).divide(limite, 2, RoundingMode.HALF_UP);
        BigDecimal esperado = new BigDecimal(esperadoPercent.replace("%","").trim().replace(",", ".")).setScale(2, RoundingMode.HALF_UP);
        Assertions.assertEquals(0, esperado.compareTo(progresso.setScale(2, RoundingMode.HALF_UP)), "Progresso diferente do esperado");
    }

    private static BigDecimal parseMoedaBR(String s) {
        String limpo = s.trim().replaceAll("[Rr]\\$\\s*", "").replace(".", "").replace(",", ".");
        return new BigDecimal(limpo);
    }

    private static YearMonth parseAnoMes(String s) {
        var norm = Normalizer.normalize(s.trim(), Normalizer.Form.NFKC);
        String[] p = norm.split("/");
        if (p.length != 2) throw new IllegalArgumentException("Formato de mês/ano inválido: " + s);
        int mes = Integer.parseInt(p[0]);
        int ano = Integer.parseInt(p[1]);
        return YearMonth.of(ano, mes);
    }
}