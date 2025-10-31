package persistencia.jpa.usuario;

import org.springframework.data.jpa.repository.JpaRepository;
import usuario.Email;

public interface UsuarioJpaRepository extends JpaRepository<UsuarioJpa, String> {
    boolean findByUserEmail(Email userEmail);

    boolean findByUsername(String username);
}
