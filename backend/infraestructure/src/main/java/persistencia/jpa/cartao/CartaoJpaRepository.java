package persistencia.jpa.cartao;

import cartao.Cartao;
import cartao.CartaoId;
import cartao.CartaoNumero;
import cartao.Fatura;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CartaoJpaRepository extends JpaRepository<CartaoJpa, String> {
    Object findByNumero(String numero);

    Object findByUsuarioId(String usuarioId);

    List<CartaoJpa> findAllByUsuarioId(String usuarioId);
}
