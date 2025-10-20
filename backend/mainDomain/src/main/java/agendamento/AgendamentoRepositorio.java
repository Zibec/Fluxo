package agendamento;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;


public interface AgendamentoRepositorio {

    void salvar(Agendamento a);

    Optional<Agendamento> obterAgendamento(String id);
}