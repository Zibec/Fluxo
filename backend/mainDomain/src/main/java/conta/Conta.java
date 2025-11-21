package conta;

import generics.FormaPagamento;

import java.math.BigDecimal;
import java.util.UUID;

import static org.apache.commons.lang3.Validate.notNull;

public class Conta extends FormaPagamento {

    private ContaId id;
    private String nome;
    private String tipo;
    private String banco;

    private String usuarioId;

    public Conta() {
        this.id = new ContaId(UUID.randomUUID().toString());
        super.setSaldo(BigDecimal.ZERO);
    }

    public Conta(String nome, String tipo, String banco) {
        this.id = new ContaId(UUID.randomUUID().toString());
        super.setSaldo(BigDecimal.ZERO);
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
        super.setSaldo(saldoInicial);
    }

    public Conta(ContaId id, String nome, String tipo, String banco, BigDecimal saldoInicial, String usuarioId) {
        this.id = id;
        this.nome = nome;
        this.tipo = tipo;
        this.banco = banco;
        if (saldoInicial.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("O saldo inicial não pode ser negativo.");
        }
        super.setSaldo(saldoInicial);
        this.usuarioId = usuarioId;
    }

    @Override
    public void realizarTransacao(BigDecimal valor) {
        if (!temSaldoSuficiente(valor)) {
            throw new IllegalArgumentException("Saldo insuficiente para realizar o débito.");
        }

        super.realizarTransacao(valor);
    }

    public boolean temSaldoSuficiente(BigDecimal valor) {
        return super.getSaldo().compareTo(valor) >= 0;
    }

    public ContaId getId() {
        return id;
    }

    public void setId(String id) {
        this.id = new ContaId(id);
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