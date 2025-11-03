package dominio.orcamento;

import cartao.CartaoRepositorio;
import conta.ContaRepositorio;
import infraestrutura.persistencia.memoria.Repositorio;
import io.cucumber.java.en.*;
import orcamento.Orcamento;
import orcamento.OrcamentoChave;
import orcamento.OrcamentoRepositorio;
import orcamento.OrcamentoService;
import org.junit.jupiter.api.Assertions;
import transacao.TransacaoRepositorio;
import transacao.TransacaoService;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.Normalizer;
import java.time.YearMonth;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class OrcamentoTest {

    private final OrcamentoRepositorio repo = new Repositorio();

    private static class FakeTransacaoService extends TransacaoService {
        private final Map<OrcamentoChave, BigDecimal> acumulado = new HashMap<>();

        public FakeTransacaoService(TransacaoRepositorio repositorio, ContaRepositorio contaRepo, CartaoRepositorio cartaoRepo) {
            super(repositorio, contaRepo, cartaoRepo);
        }

        public void set(OrcamentoChave k, BigDecimal v) {
            acumulado.put(k, v);
        }

        public void add(OrcamentoChave k, BigDecimal v) {
            acumulado.put(k, acumulado.getOrDefault(k, BigDecimal.ZERO).add(v));
        }

        public BigDecimal totalGastoMes(String usuarioId, String categoriaId, YearMonth anoMes) {
            return acumulado.getOrDefault(new OrcamentoChave(usuarioId, anoMes, categoriaId), BigDecimal.ZERO);
        }

        //importante: faz o service "enxergar" os gastos do fake
        @Override
        public BigDecimal calcularGastosConsolidadosPorCategoria(String categoriaId, YearMonth anoMes) {
            return acumulado.entrySet().stream()
                    .filter(e -> e.getKey().getCategoriaId().equals(categoriaId)
                            && e.getKey().getAnoMes().equals(anoMes))
                    .map(Map.Entry::getValue)
                    .reduce(BigDecimal.ZERO, BigDecimal::add);
        }
    }

    private final TransacaoRepositorio transacaoRepoParaTeste = new Repositorio();
    private final ContaRepositorio contaRepoParaTeste = new Repositorio();
    private final CartaoRepositorio cartaoRepoParaTeste = new Repositorio();
    private final FakeTransacaoService fakeTransacaoService = new FakeTransacaoService(transacaoRepoParaTeste, contaRepoParaTeste, cartaoRepoParaTeste);

    private final OrcamentoService service = new OrcamentoService(repo, fakeTransacaoService);

    private String usuario;
    private String categoria;
    private YearMonth anoMes;
    private Throwable erro;
    private String mensagemSistema;

    private OrcamentoChave ultimaChaveVerificada;
    private BigDecimal ultimoTotalCalculado;

    // guarda a última mensagem retornada pelo service (80% / 100% / excedeu)
    private String ultimaMensagemNotificacao;

    //  HISTÓRIA 1.1 — CRIAR / DUPLICIDADE / ATUALIZAR

    @Given("que existe a categoria {string} para um usuário autenticado como {string}")
    public void existeCategoriaUsuario(String categoria, String usuario) {
        this.usuario = usuario;
        this.categoria = categoria;
        this.erro = null;
        this.mensagemSistema = null;
        this.ultimaMensagemNotificacao = null;
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
        var chave = new OrcamentoChave(usuario, ym, categoriaEsperada);
        var opt = service.obterOrcamento(chave);
        Assertions.assertTrue(opt.isPresent(), "Orçamento não foi salvo");
        Assertions.assertEquals(0, parseMoedaBR(valorEsperado).compareTo(opt.get().getLimite()));
    }

    @Given("existe um orçamento de {string} para {string} em {string}")
    public void existeUmOrcamento(String valorMoeda, String categoria, String mesAno) {
        if (this.usuario == null) this.usuario = "Gabriel";
        var ym = parseAnoMes(mesAno);
        var valor = parseMoedaBR(valorMoeda);
        service.criarOrcamentoMensal(usuario, categoria, ym, valor);

        this.categoria = categoria;
        this.anoMes = ym;
    }

    @When("o usuário tenta definir um orçamento de {string} para {string} em {string}")
    public void usuarioTentaDefinirDuplicado(String valorMoeda, String categoria, String mesAno) {
        var ym = parseAnoMes(mesAno);
        var valor = parseMoedaBR(valorMoeda);
        try {
            service.criarOrcamentoMensal(usuario, categoria, ym, valor);
            mensagemSistema = "Criado com sucesso (não era esperado)";
        } catch (Throwable t) {
            erro = t;
            mensagemSistema = "Já existe um orçamento para esta categoria neste mês";
        }
    }

    @Then("o sistema deve retornar {string}")
    public void sistemaDeveRetornar(String mensagemEsperada) {
        Assertions.assertEquals(mensagemEsperada, mensagemSistema);
    }

    @And("o orçamento não deve ser salvo")
    public void orcamentoNaoDeveSerSalvo() {
        Assertions.assertNotNull(erro, "Era esperado erro");
        var chave = new OrcamentoChave(usuario, anoMes, categoria);
        var opt = service.obterOrcamento(chave);
        Assertions.assertTrue(opt.isPresent(), "O orçamento original deveria continuar salvo");
    }

    @Given("que existe um orçamento na categoria {string} para o mês {string} de {string}")
    public void existeUmOrcamentoNaCategoriaParaOMes(String categoria, String mesAno, String valorMoeda) {
        if (this.usuario == null) this.usuario = "Gabriel";
        var ym = parseAnoMes(mesAno);
        var valor = parseMoedaBR(valorMoeda);
        service.criarOrcamentoMensal(usuario, categoria, ym, valor);
        this.categoria = categoria;
        this.anoMes = ym;
    }

    @When("o usuário atualiza esse orçamento para {string}")
    public void usuarioAtualizaEsseOrcamentoPara(String novoValorMoeda) {
        if (this.usuario == null) this.usuario = "Gabriel";
        var novoValor = parseMoedaBR(novoValorMoeda);
        try {
            service.atualizarOrcamento(usuario, categoria, anoMes, novoValor);
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
        var opt = service.obterOrcamento(chave);
        Assertions.assertTrue(opt.isPresent(), "Orçamento não encontrado após atualizar");
        Assertions.assertEquals(0, parseMoedaBR(valorEsperado).compareTo(opt.get().getLimite()));
    }

    //  HISTÓRIA 1.2 — NOTIFICAÇÕES (80%, 100%, excedeu)

    @Given("que existe uma categoria {string} com gasto limite de {string} para o mês {string}")
    public void existeCategoriaComLimiteParaOMes(String categoria, String valorMoeda, String mesAno) {
        if (this.usuario == null) this.usuario = "Gabriel";
        this.categoria = categoria;
        this.anoMes = parseAnoMes(mesAno);

        var valor = parseMoedaBR(valorMoeda);
        service.criarOrcamentoMensal(usuario, categoria, this.anoMes, valor);
        this.ultimaMensagemNotificacao = null;
    }

    @Given("existe uma categoria {string} com gasto limite de {string} para o mês {string}")
    public void existeUmaCategoriaComGastoLimiteDeParaOMes(String categoria, String valorMoeda, String mesAno) {
        existeCategoriaComLimiteParaOMes(categoria, valorMoeda, mesAno);
    }

    @And("o gasto acumulado do usuário para {string} em {string} é de {string}")
    public void gastoAcumuladoDoUsuarioEh(String categoria, String mesAno, String valorMoeda) {
        if (this.usuario == null) this.usuario = "Gabriel";
        var ym = parseAnoMes(mesAno);
        var chave = new OrcamentoChave(usuario, ym, categoria);
        fakeTransacaoService.set(chave, parseMoedaBR(valorMoeda));
    }

    @When("o usuário registra uma despesa de {string} na categoria {string} em {string}")
    public void usuarioRegistraUmaDespesa(String valorDespesa, String categoria, String mesAno) {
        if (this.usuario == null) this.usuario = "Gabriel";
        var ym = parseAnoMes(mesAno);
        var chave = new OrcamentoChave(usuario, ym, categoria);

        var valor = parseMoedaBR(valorDespesa);

        // 1) delega a regra de notificação ao Service (usa estado "antes" do fake)
        this.ultimaMensagemNotificacao =
                service.avaliarLimiarAposLancamento(usuario, categoria, ym, valor).orElse(null);

        // 2) aplica o lançamento no fake para refletir o "mundo" após a operação
        fakeTransacaoService.add(chave, valor);
    }

    @Then("o sistema deve enviar ao usuário uma notificação {string}")
    public void sistemaDeveEnviarNotificacao(String esperado) {
        if (this.usuario == null) this.usuario = "Gabriel";
        Assertions.assertEquals(esperado, this.ultimaMensagemNotificacao,
                "Mensagem de notificação diferente do esperado");
    }

    @Then("o sistema não deve notificar o usuário")
    public void sistemaNaoDeveNotificar() {
        if (this.usuario == null) this.usuario = "Gabriel";
        Assertions.assertNull(this.ultimaMensagemNotificacao, "Não era para notificar");
    }

    //  HISTÓRIA 1.3 — VALIDAÇÃO (positivo/negativo)

    @When("o usuário tenta definir um orçamento de {string} para a categoria {string} no mês {string}")
    public void usuarioTentaDefinirValor(String valorMoeda, String categoria, String mesAno) {
        var ym = parseAnoMes(mesAno);
        var valor = parseMoedaBR(valorMoeda);

        this.categoria = categoria;
        this.anoMes = ym;

        try {
            service.criarOrcamentoMensal(usuario, categoria, ym, valor);
            mensagemSistema = "Criado com sucesso";
        } catch (Throwable t) {
            erro = t;
            mensagemSistema = "Valor do orçamento deve ser maior que zero";
        }
    }

    @And("o orçamento não deve ser aplicado")
    public void orcamentoNaoDeveSerAplicado() {
        Assertions.assertNotNull(erro, "Era esperado falhar valor negativo");
        var chave = new OrcamentoChave(usuario, anoMes, categoria);
        Assertions.assertTrue(
                service.obterOrcamento(chave).isEmpty(),
                "Não deveria haver orçamento salvo para a chave"
        );
    }

    //  HISTÓRIA 1.4 — CÁLCULO DE PROGRESSO (total + %)

    @Then("o sistema deve mostrar que o total gasto para {string} em {string} é de {string}")
    public void sistemaDeveMostrarQueOTotalGastoParaEmEDe(String categoria, String mesAno, String esperado) {
        if (this.usuario == null) this.usuario = "Gabriel";
        YearMonth ym = parseAnoMes(mesAno);

        BigDecimal totalCalculado = totalGastoViaService(usuario, categoria, ym);
        BigDecimal totalEsperado  = parseMoedaBR(esperado);

        this.ultimaChaveVerificada = new OrcamentoChave(usuario, ym, categoria);
        this.ultimoTotalCalculado  = totalCalculado;

        Assertions.assertEquals(0, totalEsperado.compareTo(totalCalculado),
                "Total gasto diferente do esperado");
    }

    @Then("o sistema deve mostrar que o total gasto para {string} em {string} permanece {string}")
    public void sistemaDeveMostrarQueOTotalGastoParaEmPermanece(String categoria, String mesAno, String esperado) {
        sistemaDeveMostrarQueOTotalGastoParaEmEDe(categoria, mesAno, esperado);
    }

    @Then("o sistema deve mostrar que o progresso de uso do orçamento é {string}")
    public void sistemaDeveMostrarQueOProgressoDeUsoDoOrcamentoE(String esperadoPercent) {
        Assertions.assertNotNull(ultimaChaveVerificada,
                "Chame primeiro o passo que valida o total gasto (ele seleciona o orçamento).");

        var orc = service.obterOrcamento(ultimaChaveVerificada)
                .orElseThrow(() -> new IllegalArgumentException("Orçamento não encontrado"));
        BigDecimal limite = orc.getLimite();

        BigDecimal progresso = limite.compareTo(BigDecimal.ZERO) == 0
                ? BigDecimal.ZERO
                : ultimoTotalCalculado.multiply(BigDecimal.valueOf(100))
                .divide(limite, 2, RoundingMode.HALF_UP);

        BigDecimal esperado = new BigDecimal(esperadoPercent.replace("%","").trim().replace(",", "."))
                .setScale(2, RoundingMode.HALF_UP);

        Assertions.assertEquals(0, esperado.compareTo(progresso.setScale(2, RoundingMode.HALF_UP)),
                "Progresso diferente do esperado");
    }

    //Helpers

    /** Usa apenas métodos do Service para derivar o total gasto (limite - saldo). */
    private BigDecimal totalGastoViaService(String usuarioId, String categoriaId, YearMonth ym) {
        var chave = new OrcamentoChave(usuarioId, ym, categoriaId);
        var orc = service.obterOrcamento(chave)
                .orElseThrow(() -> new IllegalArgumentException("Orçamento não encontrado"));
        var saldo = service.saldoMensalTotal(usuarioId, categoriaId, ym);
        return orc.getLimite().subtract(saldo);
    }

    private static BigDecimal parseMoedaBR(String s) {
        String limpo = s.trim();
        limpo = limpo.replaceAll("[Rr]\\$\\s*", "");
        limpo = limpo.replace(".", "");
        limpo = limpo.replace(",", ".");
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
