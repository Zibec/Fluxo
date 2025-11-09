package persistencia.jpa.categoria;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CategoriaJpaRepository extends JpaRepository<CategoriaJpa, String> {
    CategoriaJpa findByNome(String nome);

    List<CategoriaJpa> findAllByUsuarioId(String usuarioId);
}
