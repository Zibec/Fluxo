package persistencia.jpa.usuario;

import org.springframework.data.jpa.repository.JpaRepository;
import usuario.Email;

public interface UsuarioJpaRepository extends JpaRepository<UsuarioJpa, String> {
    boolean existsByUsername(String username);

    Object findByUsername(String username);

    boolean findByUserEmail(String userEmail);

    boolean existsByUserEmail(String userEmail);

    ;
}
