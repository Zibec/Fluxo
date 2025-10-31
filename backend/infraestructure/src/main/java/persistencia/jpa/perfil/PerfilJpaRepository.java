package persistencia.jpa.perfil;

import org.springframework.data.jpa.repository.JpaRepository;
import perfil.Perfil;

public interface PerfilJpaRepository extends JpaRepository<PerfilJpa, String> {
}
