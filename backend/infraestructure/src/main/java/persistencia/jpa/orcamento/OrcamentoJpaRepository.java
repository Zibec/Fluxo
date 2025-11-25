package persistencia.jpa.orcamento;

import aj.org.objectweb.asm.commons.Remapper;
import orcamento.Orcamento;
import orcamento.OrcamentoChave;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface OrcamentoJpaRepository extends JpaRepository<OrcamentoJpa, String> {
    boolean existsByUsuarioIdAndCategoriaIdAndAnoAndMes(String usuarioId, String categoriaId, int ano, int mes);

    List<OrcamentoJpa> findAllByUsuarioId(String usuarioId);

    Optional<OrcamentoJpa> findByCategoriaId(String categoriaId);
}
