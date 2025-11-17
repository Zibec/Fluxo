package generics;

import java.math.BigDecimal;

public abstract class FormaPagamento {
    private String banco;
    private BigDecimal saldo;

    public String getBanco() {
        return banco;
    }

    public void setBanco(String banco) {
        this.banco = banco;
    }

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
