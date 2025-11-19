package generics;

import java.math.BigDecimal;

public abstract class FormaPagamento {
    private BigDecimal saldo;

    public void realizarTransacao(BigDecimal valor) {
        if((saldo.subtract(valor)).compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Saldo insuficiente para realizar a transação.");
        }

        this.saldo = this.saldo.subtract(valor);
    }

    public void creditar(BigDecimal valor) {
        if (valor == null || valor.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Valor do crédito deve ser positivo.");
        }
        this.saldo = this.saldo.add(valor);
    }
}
