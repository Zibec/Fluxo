package persistencia.jpa.categoria;

import categoria.Categoria;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoriaJpaRepository extends JpaRepository<Categoria, String> {
}
