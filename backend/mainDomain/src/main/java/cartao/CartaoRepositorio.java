package cartao;

import java.util.List;

import static org.apache.commons.lang3.Validate.notNull;

public interface CartaoRepositorio {

    void salvar(Cartao cartao);

    Cartao obterCartao(CartaoNumero numero);

    Cartao obterCartaoPorId(CartaoId cartaoId);

    List<Cartao> obterTodos();
}
