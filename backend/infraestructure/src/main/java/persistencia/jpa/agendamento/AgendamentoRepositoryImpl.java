package persistencia.jpa.agendamento;

import agendamento.Agendamento;
import agendamento.AgendamentoRepositorio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import persistencia.jpa.Mapper;

import java.util.Optional;

@Repository
public class AgendamentoRepositoryImpl implements AgendamentoRepositorio {

    private AgendamentoJpaRepository repository;

    @Autowired
    private Mapper mapper;

    public AgendamentoRepositoryImpl(AgendamentoJpaRepository repository) {
    }

    @Override
    public void salvar(Agendamento a) {
        AgendamentoJpa agendamento = mapper.map(a, AgendamentoJpa.class);
        repository.save(agendamento);
    }

    @Override
    public Optional<Agendamento> obterAgendamento(String id) {
        var agendamento = repository.findById(id);
        return Optional.of(mapper.map(agendamento, Agendamento.class));
    }
}
