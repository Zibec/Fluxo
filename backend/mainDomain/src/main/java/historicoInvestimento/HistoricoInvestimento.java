package historicoInvestimento;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

public class HistoricoInvestimento {



    private final String historicoInvestimentoId;
    private String investimentoId;
    private BigDecimal valorAtualizado;
    private LocalDate data;


    public HistoricoInvestimento(String investimentoId, BigDecimal valorAtualizado, LocalDate data) {
        this.historicoInvestimentoId = UUID.randomUUID().toString();
        this.investimentoId = investimentoId;
        this.valorAtualizado = valorAtualizado;
        this.data = data;
    }

    public HistoricoInvestimento(String id, String investimentoId, BigDecimal valorAtualizado, LocalDate data) {
        this.historicoInvestimentoId = id;
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

    public String getHistoricoInvestimentoId() {
        return historicoInvestimentoId;
    }
}

