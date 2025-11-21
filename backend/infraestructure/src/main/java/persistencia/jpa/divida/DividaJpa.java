package persistencia.jpa.divida;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.math.BigDecimal;
import java.util.UUID;

import static org.apache.commons.lang3.Validate.notBlank;
import static org.apache.commons.lang3.Validate.notNull;

@Entity
@Table(name = "DIVIDA")
public class DividaJpa {
    @Id
    public String id;

    public String nome;
    public BigDecimal valorDevedor;
    public String usuarioId;
}
