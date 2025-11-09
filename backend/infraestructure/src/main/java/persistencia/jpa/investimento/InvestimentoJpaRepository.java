package persistencia.jpa.investimento;

import investimento.Investimento;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface InvestimentoJpaRepository extends JpaRepository<InvestimentoJpa, String> {
    List<InvestimentoJpa> findAllByUsuarioId(String usuarioId);
}
