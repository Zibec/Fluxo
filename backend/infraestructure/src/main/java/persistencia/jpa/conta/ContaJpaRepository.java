package persistencia.jpa.conta;

import conta.Conta;
import conta.ContaId;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;


public interface ContaJpaRepository extends JpaRepository<ContaJpa, String> {


    Object findByUsuarioId(String usuarioId);

    List<ContaJpa> findAllByUsuarioId(String usuarioId);
}

