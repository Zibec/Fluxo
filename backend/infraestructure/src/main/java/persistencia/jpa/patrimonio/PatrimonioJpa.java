package persistencia.jpa.patrimonio;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;


@Entity
@Table(name = "PATRIMONIO")
public class PatrimonioJpa {

    @Id
    public String id;
    public LocalDate data;
    public BigDecimal valor;
}
