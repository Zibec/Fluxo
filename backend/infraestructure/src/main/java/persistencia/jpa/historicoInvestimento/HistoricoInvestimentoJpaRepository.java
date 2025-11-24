package persistencia.jpa.historicoInvestimento;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface HistoricoInvestimentoJpaRepository extends JpaRepository<HistoricoInvestimentoJpa, String> {

    List<HistoricoInvestimentoJpa> findAllByInvestimentoId(String investimentoId);

    void deleteAllByInvestimentoId(String investimentoId);
}
