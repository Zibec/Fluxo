package persistencia.jpa.agendamento;

import agendamento.Frequencia;
import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Objects;

import static org.apache.commons.lang3.Validate.isTrue;
import static org.apache.commons.lang3.Validate.notBlank;

@Entity
@Table(name = "AGENDAMENTO")
public class AgendamentoJpa {

    @Id
    public String id;

    public LocalDate proximaData;
    public boolean ativo = true;
    public String perfilId;
    public String descricao;
    public BigDecimal valor;

    @Enumerated(EnumType.STRING)
    public Frequencia frequencia;

}
