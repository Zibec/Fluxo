package cartao;

import fatura.Fatura;

import java.math.BigDecimal;
import java.time.YearMonth;
import java.util.Date;

import static org.apache.commons.lang3.Validate.notNull;

public class Cartao {
    private final CartaoNumero numero;
    private String titular;
    private YearMonth validade;
    private Cvv cvv;

    private BigDecimal limite;
    private BigDecimal saldo;

    private static int contador = 0;
    private Fatura fatura = null;
    private Date dataFechamentoFatura;
    private Date dataVencimentoFatura;

    public Cartao(CartaoNumero numero, String titular, YearMonth validade, Cvv cvv, BigDecimal limite, Date dataFechamentoFatura, Date dataVencimentoFatura) {
        this.numero = numero;
        this.titular = titular;
        this.validade = validade;
        this.cvv = cvv;
        this.limite = limite;
        this.saldo = BigDecimal.ZERO;
        this.dataFechamentoFatura = dataFechamentoFatura;
        this.dataVencimentoFatura = dataVencimentoFatura;
    }

    public Cartao(CartaoNumero numero, String titular, YearMonth validade, Cvv cvv, BigDecimal limite,Date dataFechamentoFatura, Date dataVencimentoFatura, BigDecimal saldo) {
        this.numero = numero;
        this.titular = titular;
        this.validade = validade;
        this.cvv = cvv;
        this.limite = limite;
        this.saldo = saldo;
        this.dataFechamentoFatura = dataFechamentoFatura;
        this.dataVencimentoFatura = dataVencimentoFatura;
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

    public Date getDataFechamentoFatura() {
        return dataFechamentoFatura;
    }

    public Date getDataVencimentoFatura() {
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

    public void setDataFechamentoFatura(Date dataFechamentoFatura) {
        this.dataFechamentoFatura = dataFechamentoFatura;
    }

    public void setDataVencimentoFatura(Date dataVencimentoFatura) {
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

    public void realizarTransacao(BigDecimal valor) {
        if (fatura == null) {
            fatura = new Fatura( String.valueOf(contador) + System.currentTimeMillis(), this, dataVencimentoFatura);
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
