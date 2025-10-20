package cartao;

import java.util.HashMap;
import java.util.Map;

import static org.apache.commons.lang3.Validate.notNull;

public class CartaoRepositorio {

    private Map<CartaoNumero, Cartao> cartoes = new HashMap<>();

    public void salvar(Cartao cartao) {
        notNull(cartao, "O cartão não pode ser nulo");
        cartoes.put(cartao.getNumero(), cartao);
    }

    public Cartao obter(CartaoNumero numero) {
        notNull(numero, "O número do cartão não pode ser nulo");

        var cartao = cartoes.get(numero);
        return cartao;
    }

    public Cartao obterPorId(CartaoId cartaoId) {
        notNull(cartaoId, "O ID do cartão não pode ser nulo");

        for (Cartao cartao : cartoes.values()) {
            if (cartao.getId().equals(cartaoId)) {
                return cartao;
            }
        }
        return null;
    }
}
