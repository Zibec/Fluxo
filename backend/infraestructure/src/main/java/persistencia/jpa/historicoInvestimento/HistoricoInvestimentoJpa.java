package persistencia.jpa.historicoInvestimento;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "HISTORICO_INVESTIMENTO")
public class HistoricoInvestimentoJpa {

    @Id
    public String investimentoId;
    public BigDecimal valorAtualizado;
    public LocalDate data;
}

