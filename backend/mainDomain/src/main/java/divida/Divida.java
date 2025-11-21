package divida;

import java.math.BigDecimal;
import java.util.UUID;
import static org.apache.commons.lang3.Validate.notBlank;
import static org.apache.commons.lang3.Validate.notNull;

public class Divida {

    private String id;
    private String nome;
    private BigDecimal valorDevedor;
    private String usuarioId; // --- CAMPO NOVO NECESSÁRIO ---

    public Divida() {
        this.id = UUID.randomUUID().toString();
    }

    public Divida(String nome, BigDecimal valorDevedor) {
        notBlank(nome, "O nome da dívida não pode ser vazio.");
        notNull(valorDevedor, "O valor devedor da dívida não pode ser nulo.");

        this.id = UUID.randomUUID().toString();
        this.nome = nome;
        this.valorDevedor = valorDevedor;
    }

    public Divida(String id, String nome, BigDecimal valorDevedor) {
        notBlank(nome, "O nome da dívida não pode ser vazio.");
        notNull(valorDevedor, "O valor devedor da dívida não pode ser nulo.");

        this.id = id;
        this.nome = nome;
        this.valorDevedor = valorDevedor;
    }


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public BigDecimal getValorDevedor() {
        return valorDevedor;
    }

    public void setValorDevedor(BigDecimal valorDevedor) {
        this.valorDevedor = valorDevedor;
    }

    public String getUsuarioId() {
        return usuarioId;
    }

    public void setUsuarioId(String usuarioId) {
        this.usuarioId = usuarioId;
    }
}