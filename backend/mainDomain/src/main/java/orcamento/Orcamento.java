package orcamento;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

import static org.apache.commons.lang3.Validate.isTrue;
import static org.apache.commons.lang3.Validate.notNull;

public class Orcamento {
    private OrcamentoChave orcamentoChave;
    private final BigDecimal limite;
    private LocalDate dataLimite;

    public Orcamento(BigDecimal limite) {
        notNull(limite, "O limite não pode ser nulo");
        isTrue(limite.signum()>0, "Valor do orçamento deve ser maior que zero");
        this.limite = limite;
        this.dataLimite = LocalDate.now().plusMonths(1);
    }

    public Orcamento(OrcamentoChave chave, BigDecimal limite,  LocalDate dataLimite) {
        notNull(limite, "O limite não pode ser nulo");
        isTrue(limite.signum()>0, "Valor do orçamento deve ser maior que zero");
        this.limite = limite;
        this.dataLimite = dataLimite;
        this.orcamentoChave = chave;
    }

    public BigDecimal getLimite() {
        return limite;
    }
    public LocalDate getDataLimite() {
        return dataLimite;
    }
    public OrcamentoChave getOrcamentoChave() {
        return orcamentoChave;
    }
    public void setOrcamentoChave(OrcamentoChave orcamentoChave) {
        this.orcamentoChave = orcamentoChave;
    }
}
