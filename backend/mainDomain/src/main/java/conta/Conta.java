package conta;

import java.math.BigDecimal;
import java.util.UUID;

public class Conta {

    private final String id;
    private BigDecimal saldo;

    public Conta() {
        this.id = UUID.randomUUID().toString();
        this.saldo = BigDecimal.ZERO;
    }

    public void debitar(BigDecimal valor) {
        if (!temSaldoSuficiente(valor)) {
            throw new IllegalArgumentException("Saldo insuficiente para realizar o débito.");
        }
        this.saldo = this.saldo.subtract(valor);
    }

    public void creditar(BigDecimal valor) {
        if (valor == null || valor.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Valor do crédito deve ser positivo.");
        }
        this.saldo = this.saldo.add(valor);
    }

    public boolean temSaldoSuficiente(BigDecimal valor) {
        return this.saldo.compareTo(valor) >= 0;
    }

    public String getId() {
        return id;
    }

    public BigDecimal getSaldo() {
        return saldo;
    }

    public void setSaldo(BigDecimal saldo) {
        this.saldo = saldo;
    }
}