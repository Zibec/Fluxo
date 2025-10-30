package persistencia.jpa.cartao;

import cartao.Cartao;
import cartao.CartaoId;
import cartao.CartaoNumero;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CartaoJpaRepository extends JpaRepository<Cartao, CartaoId> {
    Cartao findByNumero(CartaoNumero numero);
}
