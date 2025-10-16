package transacao;

import java.time.LocalDate;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class InMemoryTransacaoRepositorio implements TransacaoRepositorio {
    private final Map<String, Transacao> transacao = new ConcurrentHashMap<>();
    // índice auxiliar para idempotência por (agendamentoId, data)
    private final Map<String, String> idxAgendamentoData = new ConcurrentHashMap<>();

    @Override
    public void salvar(Transacao t) {

        if (t.getPerfilId() == null){
            throw new RuntimeException("É obrigatório a seleção de um perfil.");
        }

        transacao.put(t.getId(), t);
        if (t.getOrigemAgendamentoId() != null) {
            idxAgendamentoData.put(chave(t.getOrigemAgendamentoId(), t.getData()), t.getId());
        }
    }

    @Override
    public Optional<Transacao> encontrarPorAgendamentoEData(String agendamentoId, LocalDate data) {
        String id = idxAgendamentoData.get(chave(agendamentoId, data));
        return Optional.ofNullable(id).map(transacao::get);
    }

    @Override
    public List<Transacao> listarTodas() {
        return List.copyOf(transacao.values());
    }

    private static String chave(String agendamentoId, LocalDate data) {
        return agendamentoId + "#" + data;
    }
}
