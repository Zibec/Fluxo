package persistencia.jpa.cartao;

import cartao.Cartao;
import cartao.CartaoId;
import cartao.CartaoNumero;
import cartao.CartaoRepositorio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class CartaoRepositoryImpl implements CartaoRepositorio {

    @Autowired
    private CartaoJpaRepository repository;

    @Override
    public Cartao salvar(Cartao cartao) {
        return repository.save(cartao);
    }

    @Override
    public Cartao obterCartao(CartaoNumero numero) {
        return repository.findByNumero(numero);
    }

    @Override
    public Cartao obterCartaoPorId(CartaoId cartaoId) {
        return repository.findById(cartaoId).orElse(null);
    }
}
