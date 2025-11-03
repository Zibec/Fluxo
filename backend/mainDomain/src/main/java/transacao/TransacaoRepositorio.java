package transacao;

import java.time.LocalDate;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;


public interface TransacaoRepositorio {

    void salvarTransacao(Transacao t);

    Optional<Transacao> encontrarTransacaoPorAgendamentoEData(String agendamentoId, LocalDate data);

    boolean existeTransacaoPorCategoriaId(String categoriaId);

    List<Transacao> listarTodasTransacoes();

    private static String chave(String agendamentoId, LocalDate data) {
        return agendamentoId + "#" + data;
    }

    Optional<Transacao> buscarTransacaoPorId(String id);

    void atualizarTransacao(Transacao t);

    void excluirTransacao(String id);

    Optional<Transacao> obterTransacaoPorId(String id);

    void limparTransacao();

}
