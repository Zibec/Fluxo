package investimento;

import java.math.BigDecimal;

public class Investimento {


    private final String id;
    private String nome;
    private String descricao;
    private BigDecimal valorAtual;



    public Investimento(String id, String nome, String descricao, BigDecimal valorAtual) {

        if (valorAtual == null || valorAtual.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Valor invalido");
        }

        this.id = id;
        this.descricao = descricao;
        this.valorAtual = valorAtual;
        this.nome = nome;
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

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public void atualizarValor(BigDecimal taxaSelic) {
        this.valorAtual = this.valorAtual.multiply(taxaSelic.add(new BigDecimal(1)));
    }



}
