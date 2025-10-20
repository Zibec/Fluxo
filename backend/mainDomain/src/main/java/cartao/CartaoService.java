package cartao;

import static org.apache.commons.lang3.Validate.notNull;

public class CartaoService {
    private final CartaoRepositorio cartaoRepositorio;

    public CartaoService(CartaoRepositorio repositorio) {
        this.cartaoRepositorio = repositorio;
    }

    public void salvar(Cartao cartao) {
        notNull(cartao, "O cartão não pode ser nulo");
        cartaoRepositorio.salvar(cartao);
    }

    public Cartao obter(CartaoNumero numero) {
        notNull(numero, "O número do cartão não pode ser nulo");
        return cartaoRepositorio.obterCartao(numero);
    }

    public Cartao obterPorId(CartaoId cartaoId) {
        notNull(cartaoId, "O ID do cartão não pode ser nulo");
        return cartaoRepositorio.obterCartaoPorId(cartaoId);
    }
}
