package persistencia.jpa.agendamento;

import agendamento.Agendamento;

import java.util.Iterator;

public class AgendamentosIterable implements Iterable<Agendamento> {

    private final AgendamentoJpaRepository agRepo;
    private final int pageSize;
    private final String perfilId;

    public AgendamentosIterable(AgendamentoJpaRepository agRepo, int pageSize) {
        this.agRepo = agRepo;
        this.pageSize = pageSize;
        this.perfilId = null;
    }

    public AgendamentosIterable(AgendamentoJpaRepository agRepo, String perfilId, int pageSize) {
        this.agRepo = agRepo;
        this.pageSize = pageSize;
        this.perfilId = perfilId;
    }

    @Override
    public Iterator<Agendamento> iterator() {
        if(perfilId == null){
            return new PagedAgendamentoIterator(agRepo, pageSize);
        }

        return new PagedAgendamentoIterator(agRepo, perfilId, pageSize);

    }
}
