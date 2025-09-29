package transacao;

import java.math.BigDecimal;
import java.time.LocalDate;

import conta.Conta;

public class Transacao {
    private BigDecimal valor;
    private Conta contaAssociada;
    private StatusTransacao status;
    private String descricao;
    private LocalDate vencimento;

    public Transacao(BigDecimal valor, Conta contaAssociada, StatusTransacao status, String descricao, LocalDate vencimento){
        this.valor = valor;
        this.contaAssociada = contaAssociada;
        this.status = status;
        this.descricao = descricao;
        this.vencimento = vencimento;
    }
    
    public Transacao(BigDecimal valor, Conta contaAssociada, StatusTransacao status, String descricao){
        this.valor = valor;
        this.contaAssociada = contaAssociada;
        this.status = status;
        this.descricao = descricao;
    }

    public void efetivar(){
        this.contaAssociada.debitar(this.valor);
        this.status = StatusTransacao.EFETIVADA;
    }


    // Getter e Setter para 'valor'
    public BigDecimal getValor() {
        return valor;
    }
    public void setValor(BigDecimal valor) {
        this.valor = valor;
    }

    // Getter para 'contaAssociada'
    public Conta getContaAssociada() {
        return contaAssociada;
    }

    // Getter para 'status'
    public StatusTransacao getStatus() {
        return status;
    }

}