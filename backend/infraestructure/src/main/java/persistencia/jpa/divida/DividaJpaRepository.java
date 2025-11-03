package persistencia.jpa.divida;

import divida.Divida;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DividaJpaRepository extends JpaRepository<DividaJpa, String> {
}
