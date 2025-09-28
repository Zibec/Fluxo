package transacao;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface TransacaoRepositorio {
    void salvar(Transacao t);
    Optional<Transacao> encontrarPorAgendamentoEData(String agendamentoId, LocalDate data);
    List<Transacao> listarTodas();
}
