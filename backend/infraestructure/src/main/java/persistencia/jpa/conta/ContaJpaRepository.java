package persistencia.jpa.conta;

import conta.Conta;
import conta.ContaId;
import org.springframework.data.jpa.repository.JpaRepository;


public interface ContaJpaRepository extends JpaRepository<ContaJpa, String> {


}

