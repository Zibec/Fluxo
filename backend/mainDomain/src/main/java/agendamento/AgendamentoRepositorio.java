package agendamento;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;


public class AgendamentoRepositorio {

    private final Map<String, Agendamento> agendamento = new ConcurrentHashMap<>();


    public void salvar(Agendamento a) {
        agendamento.put(a.getId(), a);
    }


    public Optional<Agendamento> obter(String id) {
        return Optional.ofNullable(agendamento.get(id));
    }
}