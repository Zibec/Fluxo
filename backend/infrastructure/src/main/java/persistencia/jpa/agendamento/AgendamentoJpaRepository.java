package persistencia.jpa.agendamento;

import agendamento.Agendamento;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AgendamentoJpaRepository extends JpaRepository<Agendamento, String> {
}
