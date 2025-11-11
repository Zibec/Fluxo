package agendamento;

import java.util.*;


public interface AgendamentoRepositorio {

    void salvar(Agendamento agendamento);

    void deletarAgendamento(String id);

    void atualizarAgendamento(String id);

    Optional<Agendamento> obterAgendamento(String id);

    Iterable<Agendamento> buscarTodos(int pageSize);
}