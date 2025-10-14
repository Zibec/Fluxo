package taxaSelic;

import java.math.BigDecimal;

public class TaxaSelic {

    private BigDecimal valor;

    public TaxaSelic(BigDecimal valor) {
        this.valor = valor;
    }

    public BigDecimal getValor() {
        return valor;
    }

    public void setValor(BigDecimal valor) {
        this.valor = valor;
    }
}
