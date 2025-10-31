package persistencia.jpa.historicoInvestimento;

import org.springframework.data.jpa.repository.JpaRepository;

public interface HistoricoInvestimentoJpaRepository extends JpaRepository<HistoricoInvestimentoJpa, String> {

    void deleteAllByInvestimentoId(String investimentoId);
}
