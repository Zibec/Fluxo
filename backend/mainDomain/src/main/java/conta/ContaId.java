package conta;

import generics.FormaPagamento;
import transacao.FormaPagamentoId;

public class ContaId extends FormaPagamentoId {
    public ContaId(String id) {
        super(id);
        super.setType("CONTA");
    }

    public String getId() {
        return super.getId();
    }
}
