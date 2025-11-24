package persistencia.jpa.investimento;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.math.BigDecimal;

@Entity
@Table(name = "INVESTIMENTO")
public class InvestimentoJpa {

    @Id
    public String id;
    public String usuarioId;
    public String contaId;
    public String nome;
    public String descricao;
    public BigDecimal valorAtual;


}
