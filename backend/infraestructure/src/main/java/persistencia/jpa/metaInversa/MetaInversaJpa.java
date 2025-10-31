package persistencia.jpa.metaInversa;

import jakarta.persistence.*;
import metaInversa.MetaInversaStatus;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;

@Entity
@Table(name = "META_INVERSA")
public class MetaInversaJpa {

    @Id
    public String id;

    @Enumerated(EnumType.STRING)
    public MetaInversaStatus status;

    public String nome;
    public BigDecimal valorDivida;
    public BigDecimal valorAcumulado;
    public String contaAssociadaId;
    public LocalDate dataLimite;
}
