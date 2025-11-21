package cartao;

import java.util.List;

import static org.apache.commons.lang3.Validate.notNull;

public interface CartaoRepositorio {

    void salvar(Cartao cartao);

    Cartao obterCartao(CartaoNumero numero);

    Cartao obterCartaoPorId(String cartaoId);

    List<Cartao> obterTodos();

    List<Cartao> obterCartaoPorUsarioId(String id);

    void deletarCartao(String id);
}
