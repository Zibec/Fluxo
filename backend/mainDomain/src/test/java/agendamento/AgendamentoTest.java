package agendamento;

import transacao.*;
import io.cucumber.java.en.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

public class AgendamentoTest {

    private final DateTimeFormatter BR = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    private final InMemoryTransacaoRepositorio txRepo = new InMemoryTransacaoRepositorio();
    private final TransacaoService txService = new TransacaoService(txRepo);
    private final AgendamentoService agService = new AgendamentoService(txService);

    private String agendamentoId;
    private LocalDate hoje;

    // ----------------- Helpers -----------------
    private static BigDecimal parseValor(String raw) {
        if (raw == null) throw new IllegalArgumentException("valor nulo");
        String s = raw.trim().replaceAll("[^0-9,.-]", "");
        if (s.contains(",")) {
            s = s.replace(".", "").replace(",", ".");
        }
        return new BigDecimal(s);
    }

    private StatusTransacao mapStatus(String s) {
        if (s == null) return StatusTransacao.PENDENTE;
        var txt = s.trim().toLowerCase();
        return switch (txt) {
            case "agendada", "pendente" -> StatusTransacao.PENDENTE;
            case "efetivada", "paga" -> StatusTransacao.EFETIVADA;
            case "cancelada" -> StatusTransacao.CANCELADA;
            default -> StatusTransacao.PENDENTE;
        };
    }

    // ---------- Criação de transação futura ----------
    @Given("que existe uma transação para o usuário pagar que é debitada do seu cartao no dia {string}")
    public void givenCartaoDebitoNoDia(String data) {
        agendamentoId = UUID.randomUUID().toString();
        var ag = new Agendamento(agendamentoId, "Débito do cartão",
                new BigDecimal("600.00"), Frequencia.MENSAL, LocalDate.parse(data, BR));
        agService.salvar(ag);
    }

    @When("o usuário precisa registrar essa transação para o dia {string}")
    public void whenRegistrarParaDia(String data) {
        var ag = agService.obter(agendamentoId).orElseThrow();
        LocalDate d = LocalDate.parse(data, BR);
        txService.criarPendenteDeAgendamento(agendamentoId, ag.getDescricao(), ag.getValor(), d);
    }

    @And("o dia atual é {string}")
    public void andHojeEh(String dataHoje) {
        hoje = LocalDate.parse(dataHoje, BR);
    }

    @Then("o usuário verá que o sistema salvou como {string} para o dia {string} uma transferência que será realizada")
    public void thenTransacaoSalvaComo(String statusEsperado, String data) {
        var d = LocalDate.parse(data, BR);
        var txOpt = txRepo.encontrarPorAgendamentoEData(agendamentoId, d);
        assertTrue(txOpt.isPresent(), "Transação deveria existir para a data agendada");
        assertEquals(mapStatus(statusEsperado), txOpt.get().getStatus());
    }

    // ---------- Evita duplicidade ----------
    private int qtdAntes;

    @Given("que o próximo dia de pagamento é {string}")
    public void givenProximoDiaPagamento(String data) {
        agendamentoId = UUID.randomUUID().toString();
        var ag = new Agendamento(agendamentoId, "Assinatura Netflix",
                new BigDecimal("55.00"), Frequencia.MENSAL, LocalDate.parse(data, BR));
        agService.salvar(ag);
    }

    @Given("já existe a transação desse pagamento agendada para o dia {string}")
    public void givenTransacaoJaAgendadaParaDia(String data) {
        LocalDate d = LocalDate.parse(data, BR);
        var ag = agService.obter(agendamentoId).orElseThrow();
        txService.criarPendenteDeAgendamento(agendamentoId, ag.getDescricao(), ag.getValor(), d);
        qtdAntes = (int) txRepo.listarTodas().stream()
                .filter(t -> agendamentoId.equals(t.getOrigemAgendamentoId())).count();
        assertEquals(1, qtdAntes, "Deveria existir exatamente 1 transação já criada");
    }

    @When("o agendamento tentar executar novamente em {int}\\/{int}\\/{int}")
    public void whenExecutarNovamenteEm(Integer dia, Integer mes, Integer ano) {
        LocalDate dataTentativa = LocalDate.of(ano, mes, dia);
        var ag = agService.obter(agendamentoId).orElseThrow();
        agService.executarSeHoje(ag, dataTentativa);
    }

    @Then("nenhuma nova transação deve ser criada")
    public void thenNaoCriaNovaTransacao() {
        int qtdDepois = (int) txRepo.listarTodas().stream()
                .filter(t -> agendamentoId.equals(t.getOrigemAgendamentoId())).count();
        assertEquals(qtdAntes, qtdDepois, "Não deveria criar nova transação no mesmo dia");
    }

    // ---------- Cancelamento de transação ----------
    private LocalDate dataTransacaoCriada;

    @Given("que existe uma transação para o usuário efetuar no dia {string} no valor de {string}")
    public void givenTransacaoParaEfetuarNoDia(String data, String valor) {
        agendamentoId = UUID.randomUUID().toString();
        var ag = new Agendamento(agendamentoId, "Pagamento avulso",
                parseValor(valor), Frequencia.MENSAL, LocalDate.parse(data, BR));
        agService.salvar(ag);
        dataTransacaoCriada = LocalDate.parse(data, BR);
        txService.criarPendenteDeAgendamento(agendamentoId, ag.getDescricao(), ag.getValor(), dataTransacaoCriada);
        assertTrue(txRepo.encontrarPorAgendamentoEData(agendamentoId, dataTransacaoCriada).isPresent());
    }

    @When("o usuário quer cancelar essa transação que iria ser paga no dia {string}")
    public void whenCancelarTransacaoDoDia(String data) {
        var tx = txRepo.encontrarPorAgendamentoEData(agendamentoId, LocalDate.parse(data, BR)).orElseThrow();
        tx.cancelar();
    }

    @When("não existe mais essa transação")
    public void whenVerificaNaoExisteMaisTransacao() {
        var tx = txRepo.encontrarPorAgendamentoEData(agendamentoId, dataTransacaoCriada).orElseThrow();
        assertEquals(StatusTransacao.CANCELADA, tx.getStatus(), "Pagamento deveria estar cancelado");
    }

    @Then("o sistema irá excluir esse pagamento")
    public void thenPagamentoExcluido() {
        var tx = txRepo.encontrarPorAgendamentoEData(agendamentoId, dataTransacaoCriada).orElseThrow();
        assertEquals(StatusTransacao.CANCELADA, tx.getStatus());
    }

    // ---------- Atualização (dia/valor) ----------
    private LocalDate dataOriginal;

    @Given("que existe uma transação para o usuário pagar no dia {string} no valor de {string}")
    public void givenTransacaoParaPagarNoDia(String data, String valor) {
        agendamentoId = UUID.randomUUID().toString();
        var ag = new Agendamento(agendamentoId, "Transferência programada",
                parseValor(valor), Frequencia.MENSAL, LocalDate.parse(data, BR));
        agService.salvar(ag);
        dataOriginal = LocalDate.parse(data, BR);
        txService.criarPendenteDeAgendamento(agendamentoId, ag.getDescricao(), ag.getValor(), dataOriginal);
        assertTrue(txRepo.encontrarPorAgendamentoEData(agendamentoId, dataOriginal).isPresent());
    }

    @When("o usuario tem essa transação que será paga no dia {string} no valor de {string}")
    public void whenUsuarioTemTransacaoNoDiaValor(String data, String valor) {
        var tx = txRepo.encontrarPorAgendamentoEData(agendamentoId, LocalDate.parse(data, BR));
        assertTrue(tx.isPresent(), "Transação esperada não encontrada");
        assertEquals(parseValor(valor), tx.get().getValor());
    }

    @When("necessita atualizar o dia e\\/ou o valor da transaferência para {string} no novo valor de {string}")
    public void whenAtualizaDiaOuValorPara(String novaData, String novoValor) {
        var txAntiga = txRepo.encontrarPorAgendamentoEData(agendamentoId, dataOriginal)
                .orElseThrow(() -> new AssertionError("Transação original não encontrada"));
        txAntiga.cancelar();
        LocalDate dNova = LocalDate.parse(novaData, BR);
        var vNovo = parseValor(novoValor);
        txService.criarPendenteDeAgendamento(agendamentoId, txAntiga.getDescricao(), vNovo, dNova);
        dataOriginal = dNova;
    }

    @Then("o sistema irá atualizar esse pagamento")
    public void thenPagamentoAtualizado() {
        var txNova = txRepo.encontrarPorAgendamentoEData(agendamentoId, dataOriginal).orElseThrow();
        assertEquals(StatusTransacao.PENDENTE, txNova.getStatus());
        assertNotEquals(StatusTransacao.EFETIVADA, txNova.getStatus());
    }

    // ---------- Criação de assinatura mensal ----------
    private LocalDate dataCriada;

    @Given("que a data atual é {string}")
    public void givenDataAtual(String hojeStr) {
        hoje = LocalDate.parse(hojeStr, BR);
    }

    @When("o usuário cria uma assinatura mensal {string} para o dia {string} iniciando em {string}")
    public void whenCriaAssinaturaMensal(String nomePlano, String diaStr, String inicioStr) {
        agendamentoId = UUID.randomUUID().toString();
        BigDecimal valorPadrao = new BigDecimal("100.00");
        LocalDate inicio = LocalDate.parse(inicioStr, BR);
        var ag = new Agendamento(agendamentoId, nomePlano, valorPadrao, Frequencia.MENSAL, inicio);
        agService.salvar(ag);
        agService.executarSeHoje(ag, inicio);
        dataCriada = inicio;
    }

    @Then("deve ser criada uma data de transação no dia {string}")
    public void thenCriaTransacaoNaData(String dataEsperada) {
        LocalDate d = LocalDate.parse(dataEsperada, BR);
        var tx = txRepo.encontrarPorAgendamentoEData(agendamentoId, d);
        assertTrue(tx.isPresent(), "Deveria existir a transação da data inicial");
    }

    @Then("a próxima data de transação deve ser {string}")
    public void thenProximaDataDeveSer(String proximaEsperada) {
        var ag = agService.obter(agendamentoId).orElseThrow();
        assertEquals(LocalDate.parse(proximaEsperada, BR), ag.getProximaData());
    }

    // ---------- Assinatura em mãos ----------
    @Given("que existe uma data de vencimento de assinatura para o dia {string}")
    public void givenVencimentoAssinaturaNoDia(String vencimento) {
        agendamentoId = UUID.randomUUID().toString();
        var ag = new Agendamento(agendamentoId, "Assinatura Serviço",
                new BigDecimal("100.00"), Frequencia.MENSAL, LocalDate.parse(vencimento, BR));
        agService.salvar(ag);
    }

    @When("o usuário tem essa assinatura para pagar no dia {string}")
    public void whenTemAssinaturaParaDia(String dia) {
        hoje = LocalDate.parse(dia, BR);
    }

    @Then("o sistema botará o pagamento agendado para o dia {string}")
    public void thenAgendaPagamentoParaDia(String dia) {
        var ag = agService.obter(agendamentoId).orElseThrow();
        LocalDate d = LocalDate.parse(dia, BR);
        txService.criarPendenteDeAgendamento(agendamentoId, ag.getDescricao(), ag.getValor(), d);
        assertTrue(txRepo.encontrarPorAgendamentoEData(agendamentoId, d).isPresent());
    }

    // ---------- Cancelamento de assinatura ----------
    @Given("que existe uma assinatura da {string} que vence todo mês no dia {string} e esta ativa")
    public void givenAssinaturaAtivaVenceDia(String nome, String dia) {
        agendamentoId = UUID.randomUUID().toString();
        LocalDate now = LocalDate.now();
        int diaInt = Integer.parseInt(dia);
        LocalDate prox = now.withDayOfMonth(Math.min(diaInt, now.lengthOfMonth()));
        var ag = new Agendamento(agendamentoId, nome, new BigDecimal("80.00"),
                Frequencia.MENSAL, prox);
        agService.salvar(ag);
        assertTrue(ag.isAtivo());
    }

    @When("o usuário quer cancelar essa assinatura")
    public void whenCancelaAssinatura() {
        var ag = agService.obter(agendamentoId).orElseThrow();
        ag.cancelar();
    }

    @Then("o status deve ser {string}")
    public void thenStatusDeveSer(String esperado) {
        var ag = agService.obter(agendamentoId).orElseThrow();
        boolean ativoEsperado = !"cancelada".equalsIgnoreCase(esperado);
        assertEquals(ativoEsperado, ag.isAtivo());
    }

    @Then("não deve existir próxima data de execução")
    public void thenSemProximaData() {
        var ag = agService.obter(agendamentoId).orElseThrow();
        assertNull(ag.getProximaData(), "Após cancelar, a próxima data deve ser nula");
    }
}
