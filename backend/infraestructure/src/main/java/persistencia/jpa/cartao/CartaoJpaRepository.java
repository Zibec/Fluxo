package persistencia.jpa.cartao;

import cartao.Cartao;
import cartao.CartaoId;
import cartao.CartaoNumero;
import cartao.Fatura;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CartaoJpaRepository extends JpaRepository<CartaoJpa, String> {
    CartaoJpa findByNumero(CartaoNumero numero);
}
