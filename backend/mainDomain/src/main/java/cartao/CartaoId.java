package cartao;

import transacao.FormaPagamentoId;

public class CartaoId implements FormaPagamentoId {
    private final String id;

    public CartaoId(String id) {
        this.id = id;
    }

    @Override
    public String getId() {
        return id;
    }

}
