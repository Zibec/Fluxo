package fatura;

import cartao.Cartao;

import java.math.BigDecimal;
import java.util.Date;

public class Fatura {
    private final String id;
    private final Cartao cartao;

    private BigDecimal valorTotal;
    private Date dataVencimento;
    private String status;

    public Fatura(String id, Cartao cartao, Date dataVencimento) {
        this.id = id;
        this.cartao = cartao;
        this.valorTotal = BigDecimal.ZERO;
        this.dataVencimento = dataVencimento;
        this.status = "ABERTA";
    }

    public String getId() {
        return id;
    }

    public Cartao getCartao() {
        return cartao;
    }

    public BigDecimal getValorTotal() {
        return valorTotal;
    }

    public Date getDataVencimento() {
        return dataVencimento;
    }

    public String getStatus() {
        return status;
    }

    public void adicionarValor(BigDecimal valor) {
        this.valorTotal = this.valorTotal.add(valor);
    }

    public void fecharFatura() {
        this.status = "FECHADA";
    }

    public void pagarFatura() {
        this.status = "PAGA";
    }
}
