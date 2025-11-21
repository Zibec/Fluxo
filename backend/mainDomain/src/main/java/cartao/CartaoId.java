package cartao;

import transacao.FormaPagamentoId;

public class CartaoId extends FormaPagamentoId {
    public CartaoId(String id) {
        super(id);
        super.setType("CARTAO");
    }

    @Override
    public String getId() {
        return super.getId();
    }

}
