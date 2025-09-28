package agendamento;

import java.util.Optional;

public interface AgendamentoRepositorio {
    void salvar(Agendamento a);
    Optional<Agendamento> obter(String id);
}