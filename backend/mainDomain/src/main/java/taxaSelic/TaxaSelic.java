package taxaSelic;

import java.math.BigDecimal;
import java.util.UUID;

public class TaxaSelic {

    public String getId() {
        return id;
    }

    private final String id;

    private BigDecimal valor;

    public TaxaSelic(BigDecimal valor) {
        this.id = UUID.randomUUID().toString();
        this.valor = valor;
    }

    public TaxaSelic(String id, BigDecimal valor){
        this.id = id;
        this.valor = valor;
    }

    public BigDecimal getValor() {
        return valor;
    }

    public void setValor(BigDecimal valor) {
        this.valor = valor;
    }
}
