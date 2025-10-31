package cartao;

import generics.FormaPagamento;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.UUID;

import static org.apache.commons.lang3.Validate.notNull;

public class Cartao implements FormaPagamento {
    private CartaoId id;
    private final CartaoNumero numero;
    private String titular;
    private YearMonth validade;
    private Cvv cvv;

    private BigDecimal limite;
    private BigDecimal saldo;
    private Fatura fatura = null;
    private LocalDate dataFechamentoFatura;
    private LocalDate dataVencimentoFatura;

    public Cartao(CartaoNumero numero, String titular, YearMonth validade, Cvv cvv, BigDecimal limite, LocalDate dataFechamentoFatura, LocalDate dataVencimentoFatura) {
        this.id = new CartaoId(UUID.randomUUID().toString());
        notNull(numero);
        this.numero = numero;
        notNull(titular);
        this.titular = titular;
        notNull(validade);
        this.validade = validade;
        notNull(cvv);
        this.cvv = cvv;
        notNull(limite);
        this.limite = limite;
        this.saldo = BigDecimal.ZERO;
        this.dataFechamentoFatura = dataFechamentoFatura;
        this.dataVencimentoFatura = dataVencimentoFatura;
    }

    public Cartao(CartaoNumero numero, String titular, YearMonth validade, Cvv cvv, BigDecimal limite, LocalDate dataFechamentoFatura, LocalDate dataVencimentoFatura, BigDecimal saldo) {
        this.id = new CartaoId(UUID.randomUUID().toString());
        notNull(numero);
        this.numero = numero;
        notNull(titular);
        this.titular = titular;
        notNull(validade);
        this.validade = validade;
        notNull(cvv);
        this.cvv = cvv;
        notNull(limite);
        this.limite = limite;
        this.saldo = saldo;
        this.dataFechamentoFatura = dataFechamentoFatura;
        this.dataVencimentoFatura = dataVencimentoFatura;
    }

    public Cartao(CartaoId id, CartaoNumero numero, String titular, YearMonth validade, Cvv cvv, BigDecimal limite, LocalDate dataFechamentoFatura, LocalDate dataVencimentoFatura, BigDecimal saldo) {
        this.id = id;
        notNull(numero);
        this.numero = numero;
        notNull(titular);
        this.titular = titular;
        notNull(validade);
        this.validade = validade;
        notNull(cvv);
        this.cvv = cvv;
        notNull(limite);
        this.limite = limite;
        this.saldo = saldo;
        this.dataFechamentoFatura = dataFechamentoFatura;
        this.dataVencimentoFatura = dataVencimentoFatura;
    }

    public CartaoId getId() {
        return id;
    }
    public CartaoNumero getNumero() {
        return numero;
    }
    public String getTitular() {
        return titular;
    }
    public YearMonth getValidade() {
        return validade;
    }
    public Cvv getCvv() {
        return cvv;
    }
    public BigDecimal getLimite() {
        return limite;
    }
    public BigDecimal getSaldo() {
        return saldo;
    }
    public LocalDate getDataFechamentoFatura() {
        return dataFechamentoFatura;
    }
    public LocalDate getDataVencimentoFatura() {
        return dataVencimentoFatura;
    }
    public void setTitular(String titular) {
        this.titular = titular;
    }
    public void setValidade(YearMonth validade) {
        this.validade = validade;
    }
    public void setCvv(Cvv cvv) {
        this.cvv = cvv;
    }
    public void setLimite(BigDecimal limite) {
        this.limite = limite;
    }
    public void setSaldo(BigDecimal saldo) {
        this.saldo = saldo;
    }
    public void setDataFechamentoFatura(LocalDate dataFechamentoFatura) {
        if (dataFechamentoFatura.isAfter(this.dataVencimentoFatura) && this.dataVencimentoFatura != null) {
            throw new IllegalArgumentException("A data de fechamento não pode ser posterior à data de vencimento.");
        }
        this.dataFechamentoFatura = dataFechamentoFatura;
    }
    public void setDataVencimentoFatura(LocalDate dataVencimentoFatura) {
        this.dataVencimentoFatura = dataVencimentoFatura;
    }
    public BigDecimal getLimiteDisponivel() {
        if (fatura == null) {
            return limite;
        }
        return limite.subtract(fatura.getValorTotal());
    }
    public Fatura getFatura() {
        return fatura;
    }
    public void setFatura(Fatura fatura) {
        this.fatura = fatura;
    }

    public void realizarTransacao(BigDecimal valor) {
        if (fatura == null) {
            fatura = new Fatura(this, dataVencimentoFatura);
        }

        if(getLimiteDisponivel().compareTo(valor) < 0) {
            throw new IllegalArgumentException("Limite indisponível para esta transação.");
        }

        fatura.adicionarValor(valor);
    }

    public void fecharFatura() {
        notNull(fatura, "Não há fatura aberta para fechar.");
        fatura.fecharFatura();
        saldo = saldo.subtract(fatura.getValorTotal());

    }

    public void pagarFatura() {
        notNull(fatura, "Não há fatura aberta para pagar.");
        saldo = saldo.subtract(fatura.getValorTotal());
        fatura.pagarFatura();
        fatura = null;
    }


}
