package patrimonio;

import java.math.BigDecimal;
import java.util.UUID;
import static org.apache.commons.lang3.Validate.notBlank;
import static org.apache.commons.lang3.Validate.notNull;

public class Divida {
    private final String id;
    private final String nome;
    private BigDecimal valorDevedor;

    public Divida(String nome, BigDecimal valorDevedor) {
        notBlank(nome, "O nome da dívida não pode ser vazio.");
        notNull(valorDevedor, "O valor devedor da dívida não pode ser nulo.");

        this.id = UUID.randomUUID().toString();
        this.nome = nome;
        this.valorDevedor = valorDevedor;
    }

    // Getters
    public String getId() { return id; }
    public String getNome() { return nome; }
    public BigDecimal getValorDevedor() { return valorDevedor; }
}
