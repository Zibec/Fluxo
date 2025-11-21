package cartao;

import java.util.List;

import static org.apache.commons.lang3.Validate.notNull;

public class CartaoService {
    private final CartaoRepositorio cartaoRepositorio;
    private final FaturaRepositorio faturaRepositorio;

    public CartaoService(CartaoRepositorio repositorio,  FaturaRepositorio faturaRepositorio) {
        this.cartaoRepositorio = repositorio;
        this.faturaRepositorio = faturaRepositorio;
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

    public Cartao obterPorId(String cartaoId) {
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

    public Fatura obterFatura(String cartaoId) {
        return faturaRepositorio.obterFatura(cartaoId);
    }

    public void fecharFatura(String cartaoId) {
        Fatura fatura = faturaRepositorio.obterFatura(cartaoId);
        fatura.fecharFatura();
        faturaRepositorio.salvarFatura(fatura);
    }

    public void pagarFatura(String cartaoId) {
        Fatura fatura = faturaRepositorio.obterFatura(cartaoId);
        Cartao cartao = cartaoRepositorio.obterCartaoPorId(cartaoId);
        cartao.pagarFatura();
        fatura.pagarFatura();
        cartaoRepositorio.salvar(cartao);
        faturaRepositorio.deletarFatura(cartaoId);
    }

    public void deletarCartao(String cartaoId) {
        cartaoRepositorio.deletarCartao(cartaoId);
    }
}
