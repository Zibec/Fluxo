package persistencia.jpa.agendamento;

import agendamento.Agendamento;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

import java.util.Iterator;
import java.util.NoSuchElementException;

final class PagedAgendamentoIterator implements Iterator<Agendamento> {
    private final AgendamentoJpaRepository agRepo;
    private final int pageSize;
    private Page<AgendamentoJpa> page;
    private int index = 0;

    public PagedAgendamentoIterator(AgendamentoJpaRepository agRepo, int pageSize) {
        this.agRepo = agRepo;
        this.pageSize = pageSize;
        var pageable = PageRequest.of(0, pageSize, Sort.by("proximaData"));
        this.page = agRepo.findAll(pageable);
    }

    @Override
    public boolean hasNext(){
        if (page == null) return false;
        if (index < page.getContent().size()) return true;

        while (page.hasNext()) {
            page = agRepo.findAll(page.nextPageable());
            index = 0;
            if (!page.getContent().isEmpty()) return true;
        }
        return false;
    }

    @Override
    public Agendamento next(){
        if(!hasNext()) throw new NoSuchElementException();
        return page.getContent().get(index++).toDomain();
    }
}
