package transacao;

public abstract class FormaPagamentoId {
    private final String id;
    private String type;

    public FormaPagamentoId(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    };

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
