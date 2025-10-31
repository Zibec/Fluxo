package persistencia.jpa.agendamento;

import agendamento.Agendamento;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

public interface AgendamentoJpaRepository extends JpaRepository<AgendamentoJpa, String> {
}
