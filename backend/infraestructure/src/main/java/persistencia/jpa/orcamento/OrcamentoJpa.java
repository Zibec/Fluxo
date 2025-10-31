package persistencia.jpa.orcamento;

import jakarta.persistence.*;
import orcamento.OrcamentoChave;
import persistencia.jpa.Mapper;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.apache.commons.lang3.Validate.isTrue;
import static org.apache.commons.lang3.Validate.notNull;

@Entity
@Table(name = "ORCAMENTO")
public class OrcamentoJpa {
    @Id
    public String chave;
    public BigDecimal limite;
    public LocalDate dataLimite;
}
