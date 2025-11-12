package generics;

import java.math.BigDecimal;

public abstract class FormaPagamento {
    private BigDecimal saldo;

    public BigDecimal getSaldo() {
        return saldo;
    }

    public void setSaldo(BigDecimal saldo) {
        this.saldo = saldo;
    }

    public void realizarTransacao(BigDecimal valor) {
        this.saldo = this.saldo.subtract(valor);
    }
}
