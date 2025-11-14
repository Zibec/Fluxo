package agendamento;

import java.math.BigDecimal;
import java.util.*;


public interface AgendamentoRepositorio {

    void salvar(Agendamento agendamento);

    void deletarAgendamento(String id);

    void atualizarAgendamento(String id, BigDecimal valor);

    Optional<Agendamento> obterAgendamento(String id);

    Iterable<Agendamento> buscarTodos(int pageSize);

    Iterable<Agendamento> buscarTodosPorPerfilId(String perfilId, int pageSize);
}