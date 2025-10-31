package persistencia.jpa.meta;

import meta.Meta;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MetaJpaRepository extends JpaRepository<MetaJpa, String> {
    MetaJpa findByDescricao(String descricao);
}
