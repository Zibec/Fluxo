package conta;

import generics.FormaPagamento;
import transacao.FormaPagamentoId;

public class ContaId implements FormaPagamentoId {
    private final String id;

    public ContaId(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }
}
