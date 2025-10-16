package transacao;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Objects;

import static org.apache.commons.lang3.Validate.isTrue;
import static org.apache.commons.lang3.Validate.notBlank;

public class Transacao {
    private final String id;              // pode ser UUID
    private final String origemAgendamentoId; // pra rastrear de qual agendamento veio (idempotência/ auditoria)
    private final String descricao;
    private BigDecimal valor;
    private LocalDate data;
    private StatusTransacao status;

    private String perfilId;

    public Transacao(String id, String origemAgendamentoId, String descricao, BigDecimal valor, LocalDate data, StatusTransacao status, String perfilId) {
        this.id = Objects.requireNonNull(id);
        this.origemAgendamentoId = origemAgendamentoId; // pode ser null se manual
        this.descricao = notBlank(descricao, "Descrição obrigatória");
        isTrue(valor != null && valor.signum() >= 0, "Valor deve ser positivo");
        this.valor = valor;
        this.data = Objects.requireNonNull(data);
        this.status = Objects.requireNonNull(status);
        this.perfilId = perfilId;
    }

    public String getPerfilId() {
        return perfilId;
    }
    public String getId() { return id; }
    public String getOrigemAgendamentoId() { return origemAgendamentoId; }
    public String getDescricao() { return descricao; }
    public BigDecimal getValor() { return valor; }
    public LocalDate getData() { return data; }
    public StatusTransacao getStatus() { return status; }

    /** Só pode atualizar enquanto PENDENTE */
    public void atualizarValor(BigDecimal novoValor) {
        if (novoValor == null || novoValor.signum() < 0) {
            throw new IllegalArgumentException("O valor deve ser positivo e não nulo");
        }
        if (this.status != StatusTransacao.PENDENTE) {
            throw new IllegalStateException("Só é possível atualizar valor enquanto a transação estiver PENDENTE");
        }
        this.valor = novoValor;
    }

    /** Efetiva a transação (ex: foi paga). */
    public void efetivar() {
        if (this.status != StatusTransacao.PENDENTE) {
            throw new IllegalStateException("Só é possível efetivar transações pendentes");
        }
        this.status = StatusTransacao.EFETIVADA;
    }

    /** Cancela a transação (ex: usuário cancelou antes de pagar). */
    public void cancelar() {
        if (this.status != StatusTransacao.PENDENTE) {
            throw new IllegalStateException("Só é possível cancelar transações pendentes");
        }
        this.status = StatusTransacao.CANCELADA;
    }

    public void reagendarPara(LocalDate novaData, LocalDate hoje) {
        Objects.requireNonNull(novaData, "Data não pode ser nula");
        Objects.requireNonNull(hoje, "Data de hoje não pode ser nula");

        if (this.status != StatusTransacao.PENDENTE) {
            throw new IllegalStateException("Só é possível reagendar transações PENDENTES");
        }
        if (novaData.isBefore(hoje)) {
            throw new IllegalArgumentException("Nova data não pode ser no passado");
        }

        this.data = novaData;
    }
}

