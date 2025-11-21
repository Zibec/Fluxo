package persistencia.jpa.transacao;

import jakarta.persistence.*;
import transacao.FormaPagamentoId;
import transacao.StatusTransacao;
import transacao.Tipo;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Objects;

import static org.apache.commons.lang3.Validate.isTrue;
import static org.apache.commons.lang3.Validate.notBlank;


@Entity
@Table(name = "TRANSACAO")
public class TransacaoJpa {
    @Id
    public String id;

    public String origemAgendamentoId;
    public String descricao;
    public BigDecimal valor;
    public LocalDate data;

    @Enumerated(EnumType.STRING)
    public StatusTransacao status;
    public String categoriaId;

    @Enumerated(EnumType.STRING)
    public Tipo tipo;
    public String tipoPagamento;

    public String transacaoOriginalId;

    public String usuarioId;

    public String pagamentoId;
    public boolean avulsa;
    public String perfilId;

    public String getCategoriaId() {
        return categoriaId;
    }

    public void setCategoriaId(String categoriaId) {
        this.categoriaId = categoriaId;
    }

    public String getPerfilId() {
        return perfilId;
    }

    public void setPerfilId(String perfilId) {
        this.perfilId = perfilId;
    }
}

