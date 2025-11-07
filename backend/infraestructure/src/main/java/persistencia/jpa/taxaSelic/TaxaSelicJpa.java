package persistencia.jpa.taxaSelic;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.math.BigDecimal;

@Entity
@Table(name = "TAXA_SELIC")
public class TaxaSelicJpa {

    @Id
    public String id;

    public BigDecimal valor;
}
