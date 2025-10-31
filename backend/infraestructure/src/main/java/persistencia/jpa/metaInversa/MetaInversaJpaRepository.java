package persistencia.jpa.metaInversa;

import metaInversa.MetaInversa;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MetaInversaJpaRepository extends JpaRepository<MetaInversaJpa, String> {
    Object findByNome(String nome);
}
