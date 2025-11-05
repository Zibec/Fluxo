package cartao;

import java.util.List;

import static org.apache.commons.lang3.Validate.notNull;

public class CartaoService {
    private final CartaoRepositorio cartaoRepositorio;

    public CartaoService(CartaoRepositorio repositorio) {
        this.cartaoRepositorio = repositorio;
    }

    public void salvar(Cartao cartao) {
        notNull(cartao, "O cartão não pode ser nulo");
        notNull(cartao.getNumero(), "O número do cartão não pode ser nulo");

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

    public List<Cartao> obterPorUsuarioId(String id) {
        notNull(id, "O ID do usuário não pode ser nulo");
        return cartaoRepositorio.obterCartaoPorUsarioId(id);
    }

    public List<Cartao> obterTodos() {
        return cartaoRepositorio.obterTodos();
    }

    public void deletarCartao(CartaoId cartaoId) {
        cartaoRepositorio.deletarCartao(cartaoId);
    }
}
