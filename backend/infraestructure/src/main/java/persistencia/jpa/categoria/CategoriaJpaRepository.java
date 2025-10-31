package persistencia.jpa.categoria;

import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoriaJpaRepository extends JpaRepository<CategoriaJpa, String> {
    CategoriaJpa findByNome(String nome);
}
