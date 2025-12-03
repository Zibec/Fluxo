package persistencia.jpa.agendamento;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import java.time.LocalDate;
import java.util.List;

public interface AgendamentoJpaRepository extends JpaRepository<AgendamentoJpa, String> {

    List<AgendamentoJpa> findAllByUsuarioId(String usuarioId);

    List<AgendamentoJpa> findByAtivoTrueAndProximaDataLessThanEqual(LocalDate data);

    Page<AgendamentoJpa> findAllByUsuarioId(String usuarioId, Pageable pageable);
}
