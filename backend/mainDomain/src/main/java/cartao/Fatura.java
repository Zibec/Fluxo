package cartao;

import java.math.BigDecimal;
import java.time.LocalDate;

public class Fatura {
    private final Cartao cartao;

    private BigDecimal valorTotal;
    private LocalDate dataVencimento;
    private String status;

    public Fatura(Cartao cartao, LocalDate dataVencimento) {
        this.cartao = cartao;
        this.valorTotal = BigDecimal.ZERO;
        this.dataVencimento = dataVencimento;
        this.status = "ABERTA";
    }

    public Cartao getCartao() {
        return cartao;
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
    }

    public void setDataVencimento(LocalDate dataVencimento) {
        this.dataVencimento = dataVencimento;
    }
}
