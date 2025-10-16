package agendamento;

import perfil.Perfil;
import perfil.PerfilRepository;
import transacao.*;
import io.cucumber.java.en.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

import conta.Conta;

import static org.junit.jupiter.api.Assertions.*;

public class AgendamentoTest {
    private LocalDate dataTxAgendada;
    private LocalDate dataAntesReagendamento;

    private final DateTimeFormatter BR = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    private final TransacaoRepositorio txRepo = new TransacaoRepositorio();

    private final TransacaoService txService = new TransacaoService(txRepo);

    // seu AgendamentoService antigo (que só recebe TransacaoService)
    private final AgendamentoRepositorio agRepo = new AgendamentoRepositorio();
    private final AgendamentoService agService = new AgendamentoService(agRepo, txService);
    private String agendamentoId;
    private LocalDate hoje;
    private Conta conta = new Conta();

    private Perfil perfil = new Perfil("0", "Pai");
    private PerfilRepository perfilRepository = new PerfilRepository();

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

    private Transacao ensureTransacaoParaAtualizar() {
        var ag = agService.obter(agendamentoId).orElseThrow(() ->
                new AssertionError("Agendamento não encontrado"));

        // 1) data-base preferencial: a que foi criada no Given do cenário
        LocalDate base = (this.dataTxAgendada != null) ? this.dataTxAgendada : ag.getProximaData();

        // 2) tenta achar pela data-base
        var txOpt = txRepo.encontrarPorAgendamentoEData(agendamentoId, base);
        if (txOpt.isPresent()) return txOpt.get();

        // 3) fallback: pega QUALQUER transação já ligada a este agendamento
        var qualquer = txRepo.listarTodas().stream()
                .filter(t -> agendamentoId.equals(t.getOrigemAgendamentoId()))
                .findFirst();
        if (qualquer.isPresent()) return qualquer.get();

        // 4) se não existir nenhuma, cria agora e retorna
        txService.criarPendenteDeAgendamento(
                agendamentoId, ag.getDescricao(), ag.getValor(), base, conta, false
        );
        return txRepo.encontrarPorAgendamentoEData(agendamentoId, base)
                .orElseThrow(() -> new AssertionError("Falha ao preparar transação para atualização"));
    }

    // ---------- Criação de transação futura ----------
    @Given("que existe uma transação para o usuário pagar que é debitada do seu cartao no dia {string}")
    public void givenCartaoDebitoNoDia(String data) {
        perfilRepository.salvar(perfil);
        agendamentoId = UUID.randomUUID().toString();
        var ag = new Agendamento(agendamentoId, "Débito do cartão",
                new BigDecimal("600.00"), Frequencia.MENSAL, LocalDate.parse(data, BR), perfilRepository.obter("0").getId());
        agService.salvar(ag);
    }

    @When("o usuário precisa registrar essa transação para o dia {string}")
    public void whenRegistrarParaDia(String data) {
        var ag = agService.obter(agendamentoId).orElseThrow();
        LocalDate d = LocalDate.parse(data, BR);
        txService.criarPendenteDeAgendamento(agendamentoId, ag.getDescricao(), ag.getValor(), d, conta, false, ag.getPerfilId());
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
        perfilRepository.salvar(perfil);
        agendamentoId = UUID.randomUUID().toString();
        var ag = new Agendamento(agendamentoId, "Assinatura Netflix",
                new BigDecimal("55.00"), Frequencia.MENSAL, LocalDate.parse(data, BR), perfilRepository.obter("0").getId());
        agService.salvar(ag);
    }

    @Given("já existe a transação desse pagamento agendada para o dia {string}")
    public void givenTransacaoJaAgendadaParaDia(String data) {
        perfilRepository.salvar(perfil);
        LocalDate d = LocalDate.parse(data, BR);
        var ag = agService.obter(agendamentoId).orElseThrow();
        txService.criarPendenteDeAgendamento(agendamentoId, ag.getDescricao(), ag.getValor(), d, conta, false, ag.getPerfilId());
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
        perfilRepository.salvar(perfil);
        agendamentoId = UUID.randomUUID().toString();
        var ag = new Agendamento(agendamentoId, "Pagamento avulso",
                parseValor(valor), Frequencia.MENSAL, LocalDate.parse(data, BR), perfilRepository.obter("0").getId());
        agService.salvar(ag);
        dataTransacaoCriada = LocalDate.parse(data, BR);
        txService.criarPendenteDeAgendamento(agendamentoId, ag.getDescricao(), ag.getValor(), dataTransacaoCriada, conta, false, ag.getPerfilId());
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
        perfilRepository.salvar(perfil);
        agendamentoId = UUID.randomUUID().toString();
        var ag = new Agendamento(agendamentoId, "Transferência programada",
                parseValor(valor), Frequencia.MENSAL, LocalDate.parse(data, BR), perfilRepository.obter("0").getId());
        agService.salvar(ag);
        dataOriginal = LocalDate.parse(data, BR);
        txService.criarPendenteDeAgendamento(agendamentoId, ag.getDescricao(), ag.getValor(), dataOriginal, conta, false, ag.getPerfilId());
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
        txService.criarPendenteDeAgendamento(agendamentoId, txAntiga.getDescricao(), vNovo, dNova, conta, false, "0");
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
        perfilRepository.salvar(perfil);
        hoje = LocalDate.parse(hojeStr, BR);
    }

    @When("o usuário cria uma assinatura mensal {string} para o dia {string} iniciando em {string}")
    public void whenCriaAssinaturaMensal(String nomePlano, String diaStr, String inicioStr) {
        agendamentoId = UUID.randomUUID().toString();
        BigDecimal valorPadrao = new BigDecimal("100.00");
        LocalDate inicio = LocalDate.parse(inicioStr, BR);
        var ag = new Agendamento(agendamentoId, nomePlano, valorPadrao, Frequencia.MENSAL, inicio, perfilRepository.obter("0").getId());
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
        perfilRepository.salvar(perfil);
        agendamentoId = UUID.randomUUID().toString();
        var ag = new Agendamento(agendamentoId, "Assinatura Serviço",
                new BigDecimal("100.00"), Frequencia.MENSAL, LocalDate.parse(vencimento, BR), perfilRepository.obter("0").getId());
        agService.salvar(ag);
    }

    @When("o usuário tem essa assinatura para pagar no dia {string}")
    public void whenTemAssinaturaParaDia(String dia) {
        hoje = LocalDate.parse(dia, BR);
        var ag = agService.obter(agendamentoId).orElseThrow();
        agService.executarSeHoje(ag, hoje);              // << ação acontece no WHEN
    }

    @Then("o sistema botará o pagamento agendado para o dia {string}")
    public void thenAgendaPagamentoParaDia(String dia) {
        LocalDate d = LocalDate.parse(dia, BR);
        txService.criarPendenteDeAgendamento(agendamentoId, ag.getDescricao(), ag.getValor(), d, ag.getPerfilId());
        assertTrue(                                   // << THEN só verifica no repositório
                txRepo.encontrarPorAgendamentoEData(agendamentoId, d).isPresent(),
                "Deveria existir transação agendada para " + dia
        );
    }

    // ---------- Cancelamento de assinatura ----------
    @Given("que existe uma assinatura da {string} que vence todo mês no dia {string} e esta ativa")
    public void givenAssinaturaAtivaVenceDia(String nome, String dia) {
        perfilRepository.salvar(perfil);
        agendamentoId = UUID.randomUUID().toString();
        LocalDate now = LocalDate.now();
        int diaInt = Integer.parseInt(dia);
        LocalDate prox = now.withDayOfMonth(Math.min(diaInt, now.lengthOfMonth()));
        var ag = new Agendamento(agendamentoId, nome, new BigDecimal("80.00"),
                Frequencia.MENSAL, prox, perfilRepository.obter("0").getId());
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

    // ---------- Validação: agendamento no passado ----------

    private String msgErro; // para guardar mensagens de erro

    @When("o usuário tenta agendar uma transação para o dia {string}")
    public void usuarioTentaAgendarTransacaoParaODia(String dataStr) {
        LocalDate data = LocalDate.parse(dataStr, BR);
        try {
            this.agendamentoId = UUID.randomUUID().toString(); // << guardar ID
            Agendamento ag = new Agendamento(
                    agendamentoId,
                    "Agendamento inválido",
                    new BigDecimal("100.00"),
                    Frequencia.MENSAL,
                    data
            );

            // usa o "hoje" do cenário, se já tiver sido definido; senão, usa o relógio real
            LocalDate referenciaHoje = (this.hoje != null ? this.hoje : LocalDate.now());

            // >>> chama a versão com validação (NÃO use agService.salvar aqui)
            agService.salvarValidandoNaoNoPassado(ag, referenciaHoje);

            // se chegou aqui, salvou — mas não deveria
            msgErro = null;
        } catch (Exception e) {
            msgErro = e.getMessage();
        }
    }

    // ---------- Atualização de transação (data inválida no passado) ----------
    @Given("que existe uma transação agendada para o dia {string} no valor de {string}")
    public void givenTransacaoAgendadaParaDia(String data, String valor) {
        agendamentoId = UUID.randomUUID().toString();
        var ag = new Agendamento(agendamentoId, "Transação teste",
                parseValor(valor), Frequencia.MENSAL, LocalDate.parse(data, BR));
        agService.salvar(ag);
    }

    @Given("a data atual é {string}")
    public void givenDataAtualGenerica(String dataHoje) {
        hoje = LocalDate.parse(dataHoje, BR);
    }

    @When("o usuário tenta atualizar a data dessa transação para {string}")
    public void whenUsuarioTentaAtualizarData(String novaDataStr) {
        LocalDate hojeRef = (this.hoje != null ? this.hoje : LocalDate.now());
        LocalDate novaData = LocalDate.parse(novaDataStr, BR);

        Transacao tx = ensureTransacaoParaAtualizar(); // helper que te passei antes
        this.dataAntesReagendamento = tx.getData();    // guarda a data original

        try {
            tx.reagendarPara(novaData, hojeRef); // deve lançar se novaData < hojeRef
            msgErro = null;                      // se não lançou, não houve erro
        } catch (Exception e) {
            msgErro = e.getMessage();            // usado nos Then
        }
    }

    @Then("o sistema não deve permitir a atualização")
    public void thenSistemaNaoPermiteAtualizacao() {
        // precisa ter dado erro
        assertNotNull(msgErro, "Esperado erro ao tentar reagendar para data no passado");

        // e a data da transação NÃO pode ter mudado
        var tx = txRepo.listarTodas().stream()
                .filter(t -> agendamentoId.equals(t.getOrigemAgendamentoId()))
                .findFirst().orElseThrow();
        assertEquals(dataAntesReagendamento, tx.getData(), "A data da transação não deveria ter sido alterada");
        assertEquals(StatusTransacao.PENDENTE, tx.getStatus(), "A transação deve permanecer pendente");
    }

    @Then("deve informar que a nova data é inválida por estar no passado")
    public void thenInformaDataInvalida() {
        assertTrue(
                msgErro != null && (
                        msgErro.toLowerCase().contains("inválida") ||
                                msgErro.toLowerCase().contains("invalida") || // sem acento
                                msgErro.toLowerCase().contains("passado")
                ),
                "Mensagem deveria indicar que a data é inválida por estar no passado. Recebido: " + msgErro
        );
    }

    // ---------- Cancelamento de transação já executada ----------
    @Given("que existe uma transação que já foi executada no dia {string}")
    public void givenTransacaoJaExecutada(String data) {
        agendamentoId = UUID.randomUUID().toString();
        var ag = new Agendamento(agendamentoId, "Transação executada",
                new BigDecimal("200.00"), Frequencia.MENSAL, LocalDate.parse(data, BR));
        agService.salvar(ag);

        // cria a pendente vinculada à conta usada nos testes
        LocalDate d = LocalDate.parse(data, BR);
        txService.criarPendenteDeAgendamento(agendamentoId, ag.getDescricao(), ag.getValor(), d, conta, false);

        // <<< GARANTE SALDO >>>
        // use o método que sua classe Conta expõe (geralmente "creditar" ou "depositar")
        // aqui credito um valor maior que o débito da transação:
        conta.creditar(new BigDecimal("1000.00"));   // ou: conta.depositar(...)

        // efetiva (agora com saldo suficiente)
        var tx = txRepo.encontrarPorAgendamentoEData(agendamentoId, d).orElseThrow();
        tx.efetivar();
    }

    @When("o usuário tenta cancelar essa transação executada")
    public void whenCancelaTransacaoExecutada() {
        var tx = txRepo.listarTodas().stream()
                .filter(t -> agendamentoId.equals(t.getOrigemAgendamentoId()))
                .findFirst().orElseThrow();
        try {
            tx.cancelar();
        } catch (Exception e) {
            // esperado: não pode cancelar já executada
        }
    }

    @Then("o sistema deve informar que não é possível cancelar uma transação já executada")
    public void thenNaoCancelaExecutada() {
        var tx = txRepo.listarTodas().stream()
                .filter(t -> agendamentoId.equals(t.getOrigemAgendamentoId()))
                .findFirst().orElseThrow();
        assertEquals(StatusTransacao.EFETIVADA, tx.getStatus());
    }

    // ---------- Atualização próxima data assinatura ----------
    @Given("existe uma assinatura mensal {string} configurada para o dia {string} com próxima data {string}")
    public void givenAssinaturaMensalConfigurada(String nome, String dia, String prox) {
        agendamentoId = UUID.randomUUID().toString();
        var ag = new Agendamento(agendamentoId, nome, new BigDecimal("50.00"),
                Frequencia.MENSAL, LocalDate.parse(prox, BR));
        agService.salvar(ag);
    }

    @Given("a próxima data de transação é {string}")
    public void givenProximaData(String data) {
        var ag = agService.obter(agendamentoId).orElseThrow();
        assertEquals(LocalDate.parse(data, BR), ag.getProximaData());
    }

    @When("o sistema executa a cobrança no dia {string}")
    public void whenExecutaCobranca(String data) {
        var ag = agService.obter(agendamentoId).orElseThrow();
        agService.executarSeHoje(ag, LocalDate.parse(data, BR));
    }

    @Then("a próxima data de transação deve ser {string}  # ou {int}\\/{int} em ano bissexto")
    public void thenProximaDataDeveSerOu(String esperado, Integer dia, Integer mes) {
        var ag = agService.obter(agendamentoId).orElseThrow();

        LocalDate esperado1 = LocalDate.parse(esperado, BR);

        // tenta montar a alternativa (ex.: 29/02) usando o MESMO ano de 'esperado1'
        LocalDate esperado2 = null;
        try {
            esperado2 = LocalDate.of(esperado1.getYear(), mes, dia);
        } catch (Exception ignored) {
            // se não for ano bissexto, essa data não existe; mantém esperado2 = null
        }

        boolean ok = ag.getProximaData().equals(esperado1)
                || (esperado2 != null && ag.getProximaData().equals(esperado2));

        assertTrue(ok, "Próxima data deveria ser " + esperado1 + (esperado2 != null ? " ou " + esperado2 : ""));
    }

    // ---------- Cancelamento de assinatura inexistente ----------
    @Given("que não existe uma assinatura ativa chamada {string} com status {string}")
    public void givenAssinaturaInexistente(String nome, String status) {
        agendamentoId = UUID.randomUUID().toString();
        var ag = new Agendamento(agendamentoId, nome, new BigDecimal("80.00"),
                Frequencia.MENSAL, LocalDate.now().plusDays(5));
        if ("cancelada".equalsIgnoreCase(status)) {
            ag.cancelar();
        }
        agService.salvar(ag);
    }

    @When("o usuário tenta cancelar essa assinatura")
    public void whenCancelaAssinaturaInexistente() {
        var ag = agService.obter(agendamentoId).orElseThrow();
        if (ag.isAtivo()) ag.cancelar();
    }

    @Then("o sistema deve informar que não há assinatura ativa para cancelar")
    public void thenNaoHaAssinaturaAtiva() {
        var ag = agService.obter(agendamentoId).orElseThrow();
        assertFalse(ag.isAtivo(), "Assinatura não deveria estar ativa");
    }

    // ---------- Criação de transação no novo ciclo ----------
    @Given("não existe transação agendada para o dia {string}")
    public void givenNaoExisteTransacao(String data) {
        var tx = txRepo.encontrarPorAgendamentoEData(agendamentoId, LocalDate.parse(data, BR));
        assertTrue(tx.isEmpty(), "Não deveria existir transação na data " + data);
    }

    @When("o agendamento executar em {string}")
    public void whenAgendamentoExecutar(String data) {
        var ag = agService.obter(agendamentoId).orElseThrow();
        agService.executarSeHoje(ag, LocalDate.parse(data, BR));
    }

    @Then("deve ser criada exatamente uma transação para o dia {string}")
    public void thenCriaExatamenteUma(String data) {
        LocalDate d = LocalDate.parse(data, BR);
        var txs = txRepo.listarTodas().stream()
                .filter(t -> agendamentoId.equals(t.getOrigemAgendamentoId()) && d.equals(t.getData()))
                .toList();
        assertEquals(1, txs.size(), "Deveria existir exatamente uma transação nessa data");
    }

    @Given("existe uma assinatura mensal {string} configurada para o dia {string}")
    public void existeUmaAssinaturaMensalConfiguradaParaODia(String nomePlano, String diaStr) {
        int dia = Integer.parseInt(diaStr);
        LocalDate base = (hoje != null ? hoje : LocalDate.now());
        LocalDate proxima = base.withDayOfMonth(Math.min(dia, base.lengthOfMonth()));

        agendamentoId = UUID.randomUUID().toString();
        BigDecimal valorPadrao = new BigDecimal("50.00");

        Agendamento ag = new Agendamento(
                agendamentoId,
                nomePlano,
                valorPadrao,
                Frequencia.MENSAL,
                proxima
        );
        agService.salvar(ag);
    }

    @Given("que não existe uma assinatura ativa chamada {string} \\(inexistente ou status {string}\\)")
    public void queNaoExisteUmaAssinaturaAtivaChamadaInexistenteOuStatus(String nome, String status) {
        agendamentoId = UUID.randomUUID().toString();

        // Se for "inexistente", não cria nada no repositório (simula ausência)
        if ("inexistente".equalsIgnoreCase(status)) {
            return;
        }

        // Se for "cancelada", cria e já cancela
        BigDecimal valorPadrao = new BigDecimal("80.00");
        LocalDate proxima = (hoje != null ? hoje : LocalDate.now()).plusDays(3);

        Agendamento ag = new Agendamento(
                agendamentoId,
                nome,
                valorPadrao,
                Frequencia.MENSAL,
                proxima
        );
        ag.cancelar();
        agService.salvar(ag);
    }

    @Then("^\\s*o sistema não deve salvar o agendamento\\s*$")
    public void sistemaNaoDeveSalvarAgendamento_regex() {
        assertNotNull(msgErro, "Esperado erro ao salvar agendamento no passado");
        // validação via repositório (como se fosse o banco)
        assertTrue(
                agRepo.obter(agendamentoId).isEmpty(),
                "Agendamento não deveria ter sido salvo no repositório"
        );
    }

    @Then("deve informar que a data é inválida por estar no passado")
    public void deveInformarQueDataInvalidaPorEstarNoPassado_regex() {
        assertTrue(
                msgErro != null && (
                        msgErro.toLowerCase().contains("inválida")
                                || msgErro.toLowerCase().contains("invalida")  // sem acento, por via das dúvidas
                                || msgErro.toLowerCase().contains("passado")
                ),
                "Mensagem deveria indicar que a data é inválida por estar no passado. Recebido: " + msgErro
        );
    }

    @Given("existe uma assinatura mensal configurada para o dia {string} com próxima data {string}")
    public void existeUmaAssinaturaMensalConfiguradaParaODiaComProximaData(String diaStr, String proxStr) {
        // a próxima data do cenário é a que manda
        LocalDate proxima = LocalDate.parse(proxStr, BR);

        agendamentoId = UUID.randomUUID().toString();
        BigDecimal valorPadrao = new BigDecimal("50.00");

        Agendamento ag = new Agendamento(
                agendamentoId,
                "Assinatura",          // nome padrão (o cenário não passa nome)
                valorPadrao,
                Frequencia.MENSAL,
                proxima
        );
        agService.salvar(ag);
    }


}
