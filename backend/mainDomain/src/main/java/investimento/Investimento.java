package investimento;

import java.math.BigDecimal;
import java.util.UUID;

public class Investimento {


    private final String id;
    private String nome;
    private String descricao;
    private BigDecimal valorAtual;

    private String usuarioId;

    public Investimento() {
        this.id = UUID.randomUUID().toString();
    }

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

    public void resgatarValor(BigDecimal valor){
        this.valorAtual = this.valorAtual.subtract(valor);
    }

    public void setValorAtual(BigDecimal valorAtual) {
        this.valorAtual = valorAtual;
    }

    public String getUsuarioId() {
        return usuarioId;
    }
    public void setUsuarioId(String usuarioId) {
        this.usuarioId = usuarioId;
    }


}
