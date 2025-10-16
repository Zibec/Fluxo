package orcamento;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.apache.commons.lang3.Validate.isTrue;
import static org.apache.commons.lang3.Validate.notNull;

public class Orcamento {
    private final BigDecimal limite;
    private LocalDate dataLimite;

    public Orcamento(BigDecimal limite) {
        notNull(limite, "O limite não pode ser nulo");
        isTrue(limite.signum()>0, "Valor do orçamento deve ser maior que zero");
        this.limite = limite;
        this.dataLimite = LocalDate.now().plusMonths(1);
    }

    public BigDecimal getLimite() {
        return limite;
    }
    public LocalDate getDataLimite() {
        return dataLimite;
    }
}
