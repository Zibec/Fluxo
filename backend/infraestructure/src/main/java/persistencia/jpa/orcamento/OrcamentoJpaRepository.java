package persistencia.jpa.orcamento;

import orcamento.Orcamento;
import orcamento.OrcamentoChave;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrcamentoJpaRepository extends JpaRepository<OrcamentoJpa, OrcamentoChave> {
}
