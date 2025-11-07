package persistencia.jpa.historicoInvestimento;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface HistoricoInvestimentoJpaRepository extends JpaRepository<HistoricoInvestimentoJpa, String> {

    void deleteAllByInvestimentoId(String investimentoId);
    List<HistoricoInvestimentoJpa> findAllByInvestimentoId(String investimentoId);
}
