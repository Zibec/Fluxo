package persistencia.jpa.agendamento;

import agendamento.Agendamento;
import agendamento.AgendamentoRepositorio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import persistencia.jpa.Mapper;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Repository
public class AgendamentoRepositoryImpl implements AgendamentoRepositorio {

    private AgendamentoJpaRepository repository;
    private Mapper mapper;

    public AgendamentoRepositoryImpl(AgendamentoJpaRepository repository, Mapper mapper) {
        this. repository = repository;
        this.mapper = mapper;
    }

    @Override
    public Iterable<Agendamento> buscarTodos(int pageSize){
        return new AgendamentosIterable(repository, pageSize);
    }

    @Override
    public void atualizarAgendamento(String id){
        AgendamentoJpa atual = repository.findById(id).orElseThrow(()-> new NoSuchElementException("Agendamento não existe: " + id));
        repository.save(atual);
    }

    @Override
    public void deletarAgendamento(String id){
        if (!repository.existsById(id)) {
            throw new NoSuchElementException("Agendamento não encontrado: " + id);
        }
        repository.deleteById(id);
    }

    @Override
    public void salvar(Agendamento a) {
        AgendamentoJpa agendamento = mapper.map(a, AgendamentoJpa.class);
        repository.save(agendamento);
    }

    @Override
    public Optional<Agendamento> obterAgendamento(String id) {
        return repository.findById(id).map(AgendamentoJpa::toDomain);
    }
}
