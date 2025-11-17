package persistencia.jpa.transacao;

import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface TransacaoJpaRepository extends JpaRepository<TransacaoJpa, String> {
    Optional<TransacaoJpa> findByOrigemAgendamentoIdAndData(String origemAgendamentoId, LocalDate data);

    List<TransacaoJpa> findAllByPagamentoId(String usuarioId);

    List<TransacaoJpa> findAllByOrigemAgendamentoId(String origemAgendamentoId);
}
