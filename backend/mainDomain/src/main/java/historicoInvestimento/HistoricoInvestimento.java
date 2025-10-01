package historicoInvestimento;

import java.math.BigDecimal;
import java.time.LocalDate;

public class HistoricoInvestimento {

    private String investimentoId;
    private BigDecimal valorAtualizado;
    private LocalDate data;

    public HistoricoInvestimento(String investimentoId, BigDecimal valorAtualizado, LocalDate data) {
        this.investimentoId = investimentoId;
        this.valorAtualizado = valorAtualizado;
        this.data = data;
    }

    public String getInvestimentoId() {
        return investimentoId;
    }

    public BigDecimal getValorAtualizado() {
        return valorAtualizado;
    }

    public LocalDate getData() {
        return data;
    }
}

