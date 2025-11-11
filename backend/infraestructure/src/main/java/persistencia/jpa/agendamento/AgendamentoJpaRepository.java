package persistencia.jpa.agendamento;

import org.springframework.data.jpa.repository.JpaRepository;
import java.time.LocalDate;
import java.util.List;

public interface AgendamentoJpaRepository extends JpaRepository<AgendamentoJpa, String> {

    List<AgendamentoJpa> findByPerfilId(String perfilId);

    List<AgendamentoJpa> findByAtivoTrueAndProximaDataLessThanEqual(LocalDate data);

}
