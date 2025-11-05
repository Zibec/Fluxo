package conta;

import generics.FormaPagamento;

import java.math.BigDecimal;
import java.util.UUID;

import static org.apache.commons.lang3.Validate.notNull;

public class Conta implements FormaPagamento {

    private ContaId id;
    private BigDecimal saldo;

    private String nome;
    private String tipo;
    private String banco;

    private String usuarioId;

    public Conta() {
        this.id = new ContaId(UUID.randomUUID().toString());
        this.saldo = BigDecimal.ZERO;
    }

    public Conta(String nome, String tipo, String banco) {
        this.id = new ContaId(UUID.randomUUID().toString());
        this.saldo = BigDecimal.ZERO;
        notNull(nome);
        this.nome = nome;
        notNull(tipo);
        this.tipo = tipo;
        this.banco = banco;
    }

    public Conta(String nome, String tipo, String banco, BigDecimal saldoInicial) {
        this.id = new ContaId(UUID.randomUUID().toString());
        this.nome = nome;
        this.tipo = tipo;
        this.banco = banco;
        if (saldoInicial.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("O saldo inicial não pode ser negativo.");
        }
        this.saldo = saldoInicial;
    }

    public Conta(ContaId id, String nome, String tipo, String banco, BigDecimal saldoInicial, String usuarioId) {
        this.id = id;
        this.nome = nome;
        this.tipo = tipo;
        this.banco = banco;
        if (saldoInicial.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("O saldo inicial não pode ser negativo.");
        }
        this.saldo = saldoInicial;
        this.usuarioId = usuarioId;
    }

    public void realizarTransacao(BigDecimal valor) {
        if (!temSaldoSuficiente(valor)) {
            throw new IllegalArgumentException("Saldo insuficiente para realizar o débito.");
        }
        this.saldo = this.saldo.subtract(valor);
    }

    public void creditar(BigDecimal valor) {
        if (valor == null || valor.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Valor do crédito deve ser positivo.");
        }
        this.saldo = this.saldo.add(valor);
    }

    public boolean temSaldoSuficiente(BigDecimal valor) {
        return this.saldo.compareTo(valor) >= 0;
    }

    public ContaId getId() {
        return id;
    }

    public void setId(String id) {
        this.id = new ContaId(id);
    }

    public BigDecimal getSaldo() {
        return saldo;
    }

    public void setSaldo(BigDecimal saldo) {
        this.saldo = saldo;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getBanco() {
        return banco;
    }

    public void setBanco(String banco) {
        this.banco = banco;
    }

    public String getUsuarioId() {
        return usuarioId;
    }

    public void setUsuarioId(String usuarioId) {
        this.usuarioId = usuarioId;
    }
}