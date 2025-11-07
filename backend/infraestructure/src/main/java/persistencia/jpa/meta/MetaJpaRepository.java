package persistencia.jpa.meta;

import aj.org.objectweb.asm.commons.Remapper;
import meta.Meta;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MetaJpaRepository extends JpaRepository<MetaJpa, String> {
    MetaJpa findByDescricao(String descricao);

    List<MetaJpa> findAllByUsuarioId(String usuarioId);
}
