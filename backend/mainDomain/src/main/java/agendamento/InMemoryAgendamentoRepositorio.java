package agendamento;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class InMemoryAgendamentoRepositorio implements AgendamentoRepositorio {
    private final Map<String, Agendamento> store = new ConcurrentHashMap<>();
    public void salvar(Agendamento a) { store.put(a.getId(), a); }
    public Optional<Agendamento> obter(String id) { return Optional.ofNullable(store.get(id)); }
}