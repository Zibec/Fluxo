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

    public Optional<Transacao> buscarPorId(String id) {
        Objects.requireNonNull(id, "ID da transação não pode ser nulo");
        return Optional.ofNullable(transacao.get(id));
    }

    public void atualizar(Transacao t) {
        salvar(t); // Reutiliza o método salvar
    }

    public void excluir(String id) {
        Objects.requireNonNull(id, "O ID da transação não pode ser nulo");

        Transacao removida = transacao.remove(id);
        if (removida == null) {
            throw new NoSuchElementException("Transação com ID " + id + " não encontrada para exclusão.");
        }

        // Remove também do índice auxiliar, se existir
        if (removida.getOrigemAgendamentoId() != null) {
            idxAgendamentoData.remove(chave(removida.getOrigemAgendamentoId(), removida.getData()));
        }

    }

}
