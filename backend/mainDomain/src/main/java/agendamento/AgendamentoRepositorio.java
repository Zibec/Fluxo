package agendamento;

import java.util.*;


public interface AgendamentoRepositorio {

    void salvar(Agendamento agendamento);

    Optional<Agendamento> obterAgendamento(String id);
}