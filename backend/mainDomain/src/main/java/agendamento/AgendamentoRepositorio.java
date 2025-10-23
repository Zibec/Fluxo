package agendamento;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;


public interface AgendamentoRepositorio {

    void salvar(Agendamento agendamento);

    Optional<Agendamento> obterAgendamento(String id);
}