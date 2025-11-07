package persistencia.jpa.meta;

import jakarta.persistence.*;
import meta.MetaStatus;
import meta.TipoMeta;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "META")
public class MetaJpa {

    @Id
    public String id;

    @Enumerated(EnumType.STRING)
    public TipoMeta tipo;

    @Enumerated(EnumType.STRING)
    public MetaStatus status;

    public String descricao;
    public BigDecimal valorAlvo;
    public BigDecimal saldoAcumulado = BigDecimal.ZERO;
    public LocalDate prazo;

    public BigDecimal getSaldoAcumulado() {
        return saldoAcumulado;
    }
    public void setSaldoAcumulado(BigDecimal saldoAcumulado) {
        this.saldoAcumulado = saldoAcumulado;
    }

}