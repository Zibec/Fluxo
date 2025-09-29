package investimento;

import java.math.BigDecimal;

public class Investimento {
    private String descricao;
    private BigDecimal valorAtual;
    private String tipo;



    public Investimento(String descricao, BigDecimal valorAtual, String tipo) {
        if (valorAtual == null || valorAtual.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Valor invalido");
        }
        this.descricao = descricao;
        this.valorAtual = valorAtual;
        this.tipo = tipo;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public BigDecimal getValorAtual() {
        return valorAtual;
    }

    public void setValorAtual(BigDecimal valorAtual) {
        this.valorAtual = valorAtual;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }
}
