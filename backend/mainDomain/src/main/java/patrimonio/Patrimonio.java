package patrimonio;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

public class Patrimonio {
    private final String id;
    private final LocalDate data;
    private final BigDecimal valor;

    public Patrimonio(LocalDate data, BigDecimal valor) {
        this.id = UUID.randomUUID().toString();
        this.data = data;
        this.valor = valor;
    }

    public Patrimonio(String id, LocalDate data, BigDecimal valor) {
        this.id = id;
        this.data = data;
        this.valor = valor;
    }

    // Getters
    public String getId() { return id; }
    public LocalDate getData() { return data; }
    public BigDecimal getValor() { return valor; }
}
