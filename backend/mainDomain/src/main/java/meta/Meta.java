package meta;

import java.math.BigDecimal;
import java.time.LocalDate;

public class Meta {

    private final String id;
    private TipoMeta tipo;
    private String descricao;
    private BigDecimal valorAlvo;
    private BigDecimal saldoAcumulado;
    private LocalDate prazo;

    public Meta(String id, TipoMeta tipo, String descricao, BigDecimal valorAlvo, LocalDate prazo) {
        if (valorAlvo == null || valorAlvo.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("O valor alvo deve ser positivo.");
        }
        if (id == null || id.trim().isEmpty()) {
            throw new IllegalArgumentException("O ID não pode ser nulo ou vazio.");
        }
        this.id = id;
        this.tipo = tipo;
        this.descricao = descricao;
        this.valorAlvo = valorAlvo;
        this.prazo = prazo;
        this.saldoAcumulado = BigDecimal.ZERO;
    }

    public void realizarAporte(BigDecimal valorDoAporte) {
        if (this.tipo != TipoMeta.POUPANCA) {
            throw new IllegalStateException("Aportes só podem ser feitos em metas de poupança.");
        }
        if (valorDoAporte == null || valorDoAporte.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Valor do aporte deve ser positivo.");
        }
        this.saldoAcumulado = this.saldoAcumulado.add(valorDoAporte);
    }

    public String getId() {
        return id;
    }

    public TipoMeta getTipo() {
        return tipo;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public BigDecimal getValorAlvo() {
        return valorAlvo;
    }

    public void setValorAlvo(BigDecimal valorAlvo) {
        this.valorAlvo = valorAlvo;
    }

    public BigDecimal getSaldoAcumulado() {
        return saldoAcumulado;
    }

    public LocalDate getPrazo() {
        return prazo;
    }

    public void setPrazo(LocalDate prazo) {
        this.prazo = prazo;
    }
}