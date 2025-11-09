package persistencia.jpa.perfil;

import org.springframework.data.jpa.repository.JpaRepository;
import perfil.Perfil;

import java.util.List;

public interface PerfilJpaRepository extends JpaRepository<PerfilJpa, String> {
    List<PerfilJpa> findAllByUsuarioId(String usuarioId);
}
