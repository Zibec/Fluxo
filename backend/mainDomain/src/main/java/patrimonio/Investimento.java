package patrimonio;

import java.math.BigDecimal;
import java.util.UUID;
import static org.apache.commons.lang3.Validate.notBlank;
import static org.apache.commons.lang3.Validate.notNull;

public class Investimento {
    private final String id;
    private final String nome;
    private BigDecimal valorAtual;

    public Investimento(String nome, BigDecimal valorAtual) {
        notBlank(nome, "O nome do investimento não pode ser vazio.");
        notNull(valorAtual, "O valor atual do investimento não pode ser nulo.");

        this.id = UUID.randomUUID().toString();
        this.nome = nome;
        this.valorAtual = valorAtual;
    }

    // Getters
    public String getId() { return id; }
    public String getNome() { return nome; }
    public BigDecimal getValorAtual() { return valorAtual; }
}