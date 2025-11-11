package persistencia.jpa.agendamento;

import agendamento.Agendamento;

import java.util.Iterator;

public class AgendamentosIterable implements Iterable<Agendamento> {

    private final AgendamentoJpaRepository agRepo;
    private final int pageSize;

    public AgendamentosIterable(AgendamentoJpaRepository agRepo, int pageSize) {
        this.agRepo = agRepo;
        this.pageSize = pageSize;
    }

    @Override
    public Iterator<Agendamento> iterator() {
        return new PagedAgendamentoIterator(agRepo, pageSize);
    }
}
