package transacao;

import java.time.LocalDate;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;


public class TransacaoRepositorio {

    // Armazenamento principal
    private final Map<String, Transacao> transacao = new ConcurrentHashMap<>();

    // Índice auxiliar para idempotência: (agendamentoId#data) -> id da transação
    private final Map<String, String> idxAgendamentoData = new ConcurrentHashMap<>();

    /** Salva (insere/atualiza) uma transação. */
    public void salvar(Transacao t) {
        transacao.put(t.getId(), t);
        if (t.getOrigemAgendamentoId() != null) {
            idxAgendamentoData.put(chave(t.getOrigemAgendamentoId(), t.getData()), t.getId());
        }
    }

    /** Busca idempotente por (agendamentoId, data). */
    public Optional<Transacao> encontrarPorAgendamentoEData(String agendamentoId, LocalDate data) {
        String id = idxAgendamentoData.get(chave(agendamentoId, data));
        return Optional.ofNullable(id).map(transacao::get);
    }

    /** (Opcional) Busca por id. */
    public Optional<Transacao> encontrarPorId(String id) {
        return Optional.ofNullable(transacao.get(id));
    }

    /** Lista todas (cópia imutável). */
    public List<Transacao> listarTodas() {
        return List.copyOf(transacao.values());
    }

    /** Limpa tudo (útil em testes). */
    public void limpar() {
        transacao.clear();
        idxAgendamentoData.clear();
    }

    private static String chave(String agendamentoId, LocalDate data) {
        return agendamentoId + "#" + data;
    }
}
