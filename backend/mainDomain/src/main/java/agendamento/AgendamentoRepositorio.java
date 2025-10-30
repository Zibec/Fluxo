package agendamento;

import java.util.*;


public interface AgendamentoRepositorio {

    Agendamento salvar(Agendamento a);

    Optional<Agendamento> obterAgendamento(String id);
}