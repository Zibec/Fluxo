package cartao;

import static org.apache.commons.lang3.Validate.notNull;

public interface CartaoRepositorio {

    Cartao salvar(Cartao cartao);

    Cartao obterCartao(CartaoNumero numero);

    Cartao obterCartaoPorId(CartaoId cartaoId);
}
