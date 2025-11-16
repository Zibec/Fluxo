package persistencia.jpa.transacao;

import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface TransacaoJpaRepository extends JpaRepository<TransacaoJpa, String> {
    Object findByOrigemAgendamentoIdAndData(String origemAgendamentoId, LocalDate data);
    List<TransacaoJpa> findAllByPagamentoId(String usuarioId);
}
