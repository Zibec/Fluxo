package persistencia.jpa.conta;

import conta.ContaId;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.math.BigDecimal;


@Entity
@Table(name = "CONTA")
public class ContaJpa {

    @Id
    private ContaId id;

    private BigDecimal saldo;


}
