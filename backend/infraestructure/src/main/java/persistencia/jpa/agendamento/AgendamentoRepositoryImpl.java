package persistencia.jpa.agendamento;

import agendamento.Agendamento;
import agendamento.AgendamentoRepositorio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import persistencia.jpa.Mapper;

import java.math.BigDecimal;
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
    public Iterable<Agendamento> buscarTodosPorPerfilId(String perfilId, int pageSize) {
        return new AgendamentosIterable(repository, pageSize);
    }

    @Override
    public void atualizarAgendamento(String id, BigDecimal valor){
        var jpa = repository.findById(id).orElseThrow(()-> new NoSuchElementException("Agendamento não existe: " + id));
        if (valor == null) {
            throw new IllegalArgumentException("Valor obrigatório");
        }
        var dominio = jpa.toDomain();
        dominio.setValor(valor);
        repository.save(mapper.map(dominio, AgendamentoJpa.class));

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
        agendamento.setCategoriaId(a.getCategoriaId());
        repository.save(agendamento);
    }

    @Override
    public Optional<Agendamento> obterAgendamento(String id) {
        return repository.findById(id).map(AgendamentoJpa::toDomain);
    }
}
