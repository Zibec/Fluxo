package cartao;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Fatura {
    private Cartao cartao;

    private BigDecimal valorTotal;
    private LocalDate dataVencimento;
    private String status;
    private ArrayList<String> transacoes;

    public Fatura(Cartao cartao, LocalDate dataVencimento) {
        this.cartao = cartao;
        this.valorTotal = BigDecimal.ZERO;
        this.dataVencimento = dataVencimento;
        this.status = "ABERTA";
    }

    public Cartao getCartao() {
        return cartao;
    }

    public void setCartao(Cartao cartao) {
        this.cartao = cartao;
    }

    public BigDecimal getValorTotal() {
        return valorTotal;
    }

    public LocalDate getDataVencimento() {
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
        this.valorTotal = BigDecimal.ZERO;
        this.setTransacoes(new ArrayList<String>());
    }

    public void setDataVencimento(LocalDate dataVencimento) {
        this.dataVencimento = dataVencimento;
    }

    public List<String> getTransacoes() {
        return transacoes;
    }

    public void addTransacoes(String transacao) {
        if(this.transacoes == null) {
            this.transacoes = new ArrayList<String>();
        }

        this.transacoes.add(transacao);
    }

    public void setTransacoes(ArrayList<String> transacoes) {
        this.transacoes = transacoes;
    }
}
