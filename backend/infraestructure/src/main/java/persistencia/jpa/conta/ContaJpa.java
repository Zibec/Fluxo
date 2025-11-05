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
    public String id;

    public String usuarioId;

    public String nome;
    public BigDecimal saldo;
    public String tipo;
    public String banco;
}
