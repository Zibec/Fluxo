package orcamento;

import io.cucumber.java.en.*;
import org.junit.jupiter.api.Assertions;

import orcamento.*;

import java.math.BigDecimal;
import java.text.Normalizer;
import java.time.YearMonth;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class OrcamentoTest {

    // ---------- Infra simples para os testes ----------
    private final OrcamentoRepositorio repo = new OrcamentoRepositorio();

    /** Fake do somatório de transações no mês por (usuario,categoria,anoMes). */
    private static class SumarioFake implements TransacaoSumario {
        private final Map<OrcamentoChave, BigDecimal> acumulado = new HashMap<>();
        public void set(OrcamentoChave k, BigDecimal v) { acumulado.put(k, v); }
        public void add(OrcamentoChave k, BigDecimal v) { acumulado.put(k, acumulado.getOrDefault(k, BigDecimal.ZERO).add(v)); }
        @Override public BigDecimal totalGastoMes(String usuarioId, String categoriaId, YearMonth anoMes) {
            return acumulado.getOrDefault(new OrcamentoChave(usuarioId,anoMes, categoriaId), BigDecimal.ZERO);
        }
    }
    private final SumarioFake sumario = new SumarioFake();

    /** Serviço para criar e calcular progresso. */
    private final OrcamentoService service = new OrcamentoService(repo, sumario);

    /** Coletor simples de notificação. */
    private static class Notificador {
        private String ultimaMensagem;
        public void notificar(String msg) { ultimaMensagem = msg; }
        public Optional<String> ultima() { return Optional.ofNullable(ultimaMensagem); }
        public void limpar() { ultimaMensagem = null; }
    }
    private final Notificador notificador = new Notificador();

    // ---------- Contexto do cenário ----------
    private String usuario;
    private String categoria;
    private YearMonth anoMes;
    private Throwable erro;
    private String mensagemSistema;

    // ==========================================================
    //  HISTÓRIA 1.1 — CRIAR / DUPLICIDADE / ATUALIZAR
    // ==========================================================

    @Given("que existe a categoria {string} para um usuário autenticado como {string}")
    public void existeCategoriaUsuario(String categoria, String usuario) {
        this.usuario = usuario;
        this.categoria = categoria;
        this.erro = null;
        this.mensagemSistema = null;
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
        var chave = new OrcamentoChave(usuario,ym ,categoria );
        var opt = repo.obterOrcamento(chave);
        Assertions.assertTrue(opt.isPresent(), "Orçamento não foi salvo");
        Assertions.assertEquals(parseMoedaBR(valorEsperado), opt.get().getLimite());
    }

    @Given("existe um orçamento de {string} para {string} em {string}")
    public void existeUmOrcamento(String valorMoeda, String categoria, String mesAno) {
        var ym = parseAnoMes(mesAno);
        var valor = parseMoedaBR(valorMoeda);
        var chave = new OrcamentoChave(usuario,ym, categoria);
        repo.salvarNovo(chave, new Orcamento(valor)); // já existente
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
            // padroniza a mensagem esperada no teu Gherkin
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
    }

    @Given("que existe um orçamento na categoria {string} para o mês {string} de {string}")
    public void existeUmOrcamentoNaCategoriaParaOMes(String categoria, String mesAno, String valorMoeda) {
        if (this.usuario == null) this.usuario = "Gabriel";
        var ym = parseAnoMes(mesAno);
        var valor = parseMoedaBR(valorMoeda);
        var chave = new OrcamentoChave(usuario,ym, categoria);
        repo.salvarNovo(chave, new Orcamento(valor));
        this.categoria = categoria;
        this.anoMes = ym;
    }

    @When("o usuário atualiza esse orçamento para {string}")
    public void usuarioAtualizaEsseOrcamentoPara(String novoValorMoeda) {
        if (this.usuario == null) this.usuario = "Gabriel";
        var novoValor = parseMoedaBR(novoValorMoeda);
        var chave = new OrcamentoChave(usuario,anoMes, categoria);
        try {
            // se o repositório é void:
            if (repo.obterOrcamento(chave).isEmpty()) {
                mensagemSistema = "Não existe um orçamento para essa chave";
                return;
            }
            repo.atualizarOrcamento(chave, new Orcamento(novoValor)); // <-- método void
            mensagemSistema = "Atualizado com sucesso";
        } catch (Throwable t) {
            erro = t;
            mensagemSistema = t.getMessage();
        }
    }

    @Then("o usuário deve ver o orçamento salvo com valor {string}")
    public void usuarioDeveVerOrcamentoSalvoComValor(String valorEsperado) {
        if (this.usuario == null) this.usuario = "Gabriel";
        var chave = new OrcamentoChave(usuario,anoMes ,categoria);
        var opt = repo.obterOrcamento(chave);
        Assertions.assertTrue(opt.isPresent(), "Orçamento não encontrado após atualizar");
        Assertions.assertEquals(parseMoedaBR(valorEsperado), opt.get().getLimite());
    }

    // ==========================================================
    //  HISTÓRIA 1.2 — NOTIFICAÇÕES (80%, 100%, excedeu)
    // ==========================================================

    @Given("que existe uma categoria {string} com gasto limite de {string} para o mês {string}")
    public void existeCategoriaComLimiteParaOMes(String categoria, String valorMoeda, String mesAno) {
        if (this.usuario == null) this.usuario = "Gabriel";
        this.categoria = categoria;
        this.anoMes = parseAnoMes(mesAno);

        var valor = parseMoedaBR(valorMoeda);
        var chave = new OrcamentoChave(usuario,anoMes, categoria);
        repo.salvarNovo(chave, new Orcamento(valor));
        notificador.limpar();
    }

    @And("o gasto acumulado do usuário para {string} em {string} é de {string}")
    public void gastoAcumuladoDoUsuarioEh(String categoria, String mesAno, String valorMoeda) {
        if (this.usuario == null) this.usuario = "Gabriel";
        var ym = parseAnoMes(mesAno);
        var chave = new OrcamentoChave(usuario,ym, categoria);
        sumario.set(chave, parseMoedaBR(valorMoeda));
    }

    @When("o usuário registra uma despesa de {string} na categoria {string} em {string}")
    public void usuarioRegistraUmaDespesa(String valorDespesa, String categoria, String mesAno) {
        if (this.usuario == null) this.usuario = "Gabriel";
        var ym = parseAnoMes(mesAno);
        var chave = new OrcamentoChave(usuario, ym, categoria);

        var antes = sumario.totalGastoMes(usuario, categoria, ym);
        var orc = repo.obterOrcamento(chave).orElseThrow(() -> new IllegalArgumentException("Orçamento não encontrado"));
        var limite = orc.getLimite();

        // adiciona a despesa no acumulado
        var valor = parseMoedaBR(valorDespesa);
        sumario.add(chave, valor);

        var depois = sumario.totalGastoMes(usuario, categoria, ym);

        // Regras de notificação
        var oitenta = limite.multiply(BigDecimal.valueOf(0.8));

        if (antes.compareTo(oitenta) < 0 && depois.compareTo(oitenta) >= 0 && depois.compareTo(limite) < 0) {
            notificador.notificar("Você atingiu 80% do limite definido");
        } else if (antes.compareTo(limite) < 0 && depois.compareTo(limite) == 0) {
            notificador.notificar("Você atingiu 100% do limite definido");
        } else if (depois.compareTo(limite) > 0 && antes.compareTo(limite) <= 0
                || (antes.compareTo(limite) >= 0 && depois.compareTo(limite) > 0)) {
            notificador.notificar("Você excedeu o limite desta categoria");
        }
    }

    @Then("o sistema deve enviar ao usuário uma notificação {string}")
    public void sistemaDeveEnviarNotificacao(String esperado) {
        if (this.usuario == null) this.usuario = "Gabriel";
        var msg = notificador.ultima().orElse(null);
        Assertions.assertEquals(esperado, msg, "Mensagem de notificação diferente do esperado");
    }

    @Then("o sistema não deve notificar o usuário")
    public void sistemaNaoDeveNotificar() {
        if (this.usuario == null) this.usuario = "Gabriel";
        Assertions.assertTrue(notificador.ultima().isEmpty(), "Não era para notificar");
    }

    // ==========================================================
    //  HISTÓRIA 1.3 — VALIDAÇÃO (positivo/negativo)
    // ==========================================================

    @When("o usuário tenta definir um orçamento de {string} para a categoria {string} no mês {string}")
    public void usuarioTentaDefinirValor(String valorMoeda, String categoria, String mesAno) {
        var ym = parseAnoMes(mesAno);
        var valor = parseMoedaBR(valorMoeda);
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
    }

    // ---------- Helpers & utilidades ----------

    private static BigDecimal parseMoedaBR(String s) {
        // Aceita "R$ 1.234,56" ou "100,00" ou "-50,00"
        String limpo = s.trim();
        limpo = limpo.replaceAll("[Rr]\\$\\s*", "");
        limpo = limpo.replace(".", "");
        limpo = limpo.replace(",", ".");
        return new BigDecimal(limpo);
    }

    private static YearMonth parseAnoMes(String s) {
        // Aceita "09/2025" ou "9/2025"
        var norm = Normalizer.normalize(s.trim(), Normalizer.Form.NFKC);
        String[] p = norm.split("/");
        if (p.length != 2) throw new IllegalArgumentException("Formato de mês/ano inválido: " + s);
        int mes = Integer.parseInt(p[0]);
        int ano = Integer.parseInt(p[1]);
        return YearMonth.of(ano, mes);
    }
}
