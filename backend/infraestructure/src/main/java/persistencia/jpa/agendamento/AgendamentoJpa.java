package persistencia.jpa.agendamento;

import agendamento.Agendamento;
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
    @Column(name = "id", nullable = false, length = 160)
    public String id;

    @Column(name = "proxima_data", nullable = false)
    public LocalDate proximaData;

    @Column(nullable = false)
    public boolean ativo = true;

    @Column(name = "perfil_id", nullable = false)
    public String perfilId;

    @Column(name = "descricao")
    public String descricao;

    @Column(name = "valor", nullable = false, precision = 19, scale = 2)
    public BigDecimal valor;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    public Frequencia frequencia;

    public AgendamentoJpa() {
    }

    public Agendamento toDomain(){
        return new Agendamento(id, descricao, valor, frequencia, proximaData,perfilId);
    }
}
