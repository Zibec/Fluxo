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
        if (t.getPerfilId() == null){
            throw new RuntimeException("É obrigatório a seleção de um perfil.");
        }
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

    public boolean existePorCategoriaId(String categoriaId) {
        Objects.requireNonNull(categoriaId, "ID da Categoria não pode ser nulo");
        // Percorre a lista de transações e para no primeiro que encontrar com o ID
        return transacao.values().stream()
                .anyMatch(transacao -> categoriaId.equals(transacao.getCategoriaId()));
    }

    /** Lista todas. */
    public List<Transacao> listarTodas() {
        return List.copyOf(transacao.values());
    }

    private static String chave(String agendamentoId, LocalDate data) {
        return agendamentoId + "#" + data;
    }

    public Optional<Transacao> obterPorId(String id) {
        Objects.requireNonNull(id, "O ID da transação não pode ser nulo");
        // 'transacao' é o nome do seu Map principal, então usamos ele para buscar pelo ID.
        return Optional.ofNullable(transacao.get(id));
    }

    public void limpar() {
        transacao.clear();
        idxAgendamentoData.clear();
    }
}
