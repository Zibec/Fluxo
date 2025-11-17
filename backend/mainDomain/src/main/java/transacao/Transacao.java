package transacao;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Objects;

import generics.FormaPagamento;

import static org.apache.commons.lang3.Validate.isTrue;
import static org.apache.commons.lang3.Validate.notBlank;

public class Transacao {

    private final String id;              // pode ser UUID
    private final String origemAgendamentoId; // pra rastrear de qual agendamento veio (idempotência/ auditoria)
    private final String descricao;
    private BigDecimal valor;
    private LocalDate data;
    private StatusTransacao status;
    private String categoriaId;
    private final Tipo tipo;
    private String transacaoOriginalId;
    private FormaPagamentoId pagamentoId;
    private boolean avulsa;

    private String perfilId;

    public Transacao(String id, String origemAgendamentoId, String descricao, BigDecimal valor, LocalDate data, StatusTransacao status, String categoriaId, FormaPagamentoId pagamentoId, boolean avulsa, Tipo tipo, String perfilId) {
        this.id = Objects.requireNonNull(id);
        this.origemAgendamentoId = origemAgendamentoId; // pode ser null se manual
        this.descricao = notBlank(descricao, "Descrição obrigatória");
        this.tipo = tipo;
        isTrue(valor != null && valor.signum() >= 0, "Valor deve ser positivo");
        this.valor = valor;
        this.data = Objects.requireNonNull(data);
        this.status = Objects.requireNonNull(status);
        this.categoriaId = categoriaId;
        this.pagamentoId = pagamentoId;
        this.avulsa = avulsa;
        this.perfilId = perfilId;
    }

    //Construtor sem categoria
    public Transacao(String id, String origemAgendamentoId, String descricao, BigDecimal valor, LocalDate data, StatusTransacao status, FormaPagamentoId pagamentoId, boolean avulsa,Tipo tipo, String perfilId) {
        this.id = Objects.requireNonNull(id);
        this.origemAgendamentoId = origemAgendamentoId; // pode ser null se manual
        this.descricao = notBlank(descricao, "Descrição obrigatória");
        isTrue(valor != null && valor.signum() >= 0, "Valor deve ser positivo");
        this.valor = valor;
        this.data = Objects.requireNonNull(data);
        this.status = Objects.requireNonNull(status);
        this.tipo = Objects.requireNonNull(tipo, "O tipo da transação é obrigatório");
        this.pagamentoId = pagamentoId;
        this.avulsa = avulsa;
        this.perfilId = perfilId;
    }

    public String getPerfilId() { return perfilId; }
    public void setPerfilId(String perfilId) { this.perfilId = perfilId; }
    public String getId() { return id; }
    public String getOrigemAgendamentoId() { return origemAgendamentoId; }
    public String getDescricao() { return descricao; }
    public BigDecimal getValor() { return valor; }
    public LocalDate getData() { return data; }
    public StatusTransacao getStatus() { return status; }
    public String getCategoriaId() { return categoriaId; }
    public Tipo getTipo() {return tipo;}
    public String getTransacaoOriginalId() {return transacaoOriginalId;}
    public void setTransacaoOriginalId(String transacaoOriginalId) {this.transacaoOriginalId = transacaoOriginalId;}
    public boolean isAvulsa() { return avulsa; }
    public FormaPagamentoId getPagamentoId() { return pagamentoId; }
    public void setCategoriaId(String categoriaId) { this.categoriaId = categoriaId; }
    public void setPagamentoId(FormaPagamentoId id) { this.pagamentoId = id; }


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

    public boolean isProximaDoVencimento() {
        if (data.equals(LocalDate.now().plusDays(1))) {
            return true;
        }
        return false;
    }
}

