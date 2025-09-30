package investimento;

import java.math.BigDecimal;

public class Investimento {


    private final String id;
    private String descricao;
    private BigDecimal valorAtual;
    private String tipo;



    public Investimento(String id, String descricao, BigDecimal valorAtual, String tipo) {

        if (valorAtual == null || valorAtual.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Valor invalido");
        }

        this.id = id;
        this.descricao = descricao;
        this.valorAtual = valorAtual;
        this.tipo = tipo;
    }

    public String getId() {
        return id;
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

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public void atualizarValor(Double taxaSelic) {
        this.valorAtual = this.valorAtual.multiply(new BigDecimal(1 + taxaSelic));
    }



}
