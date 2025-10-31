package persistencia.jpa.investimento;

import investimento.Investimento;
import org.springframework.data.jpa.repository.JpaRepository;

public interface InvestimentoJpaRepository extends JpaRepository<InvestimentoJpa, String> {
}
