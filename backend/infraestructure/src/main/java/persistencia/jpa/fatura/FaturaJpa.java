package persistencia.jpa.fatura;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name="FATURA")
public class FaturaJpa {

    @Id
    public String cartaoId;

    public BigDecimal valorTotal;
    public LocalDate dataVencimento;
    public String status;
}
