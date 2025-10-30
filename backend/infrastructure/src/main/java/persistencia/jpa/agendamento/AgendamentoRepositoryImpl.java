package persistencia.jpa.agendamento;

import agendamento.Agendamento;
import agendamento.AgendamentoRepositorio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public class AgendamentoRepositoryImpl implements AgendamentoRepositorio {

    @Autowired
    private  AgendamentoJpaRepository repository;

    @Override
    public Agendamento salvar(Agendamento a) {
        return repository.save(a);
    }

    @Override
    public Optional<Agendamento> obterAgendamento(String id) {
        return repository.findById(id);
    }
}
