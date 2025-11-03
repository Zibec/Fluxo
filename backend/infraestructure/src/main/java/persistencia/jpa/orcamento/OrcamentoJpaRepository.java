package persistencia.jpa.orcamento;

import orcamento.Orcamento;
import orcamento.OrcamentoChave;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrcamentoJpaRepository extends JpaRepository<OrcamentoJpa, String> {
    boolean existsByUsuarioIdCategoriaIdAnoMes(String usuarioId, String categoriaId, int ano, int mes);
}
