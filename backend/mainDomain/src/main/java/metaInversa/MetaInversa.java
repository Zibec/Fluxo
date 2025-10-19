package metaInversa;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;

import conta.Conta;

public class MetaInversa {
    private final String id;
    private String nome;
    private BigDecimal valorDivida;
    private BigDecimal valorAcumulado;
    private Conta contaAssociada;
    private LocalDate dataLimite;
    private MetaInversaStatus status;

    public MetaInversa(String id, String nome, BigDecimal valorDivida, Conta contaAssociada, LocalDate dataLimite) {
        this.id = id;
        this.nome = nome;
        this.valorDivida = valorDivida;
        this.contaAssociada = contaAssociada;
        this.dataLimite = dataLimite;
        this.valorAcumulado = BigDecimal.ZERO; // opcionalmente pode iniciar zerado
        this.status = MetaInversaStatus.ATIVA;
    }

    private void conluirMeta() {
        this.status = MetaInversaStatus.CONCLUIDA;
    }

    public void realizarAporte(BigDecimal valorDoAporte) {
        if (valorDoAporte == null || valorDoAporte.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Valor do aporte deve ser positivo.");
        }
        this.valorAcumulado = this.valorAcumulado.add(valorDoAporte);
        this.contaAssociada.realizarTransacao(valorDoAporte);

        if (this.valorAcumulado.compareTo(this.valorDivida) >= 0) {
            conluirMeta();
        }
    }

    public BigDecimal getProgresso() {
        return valorAcumulado.divide(valorDivida, 2, RoundingMode.HALF_UP);
    }

    // Getters
    public String getId() {
        return id;
    }

    public BigDecimal getValorDivida() {
        return valorDivida;
    }

    public BigDecimal getValorAmortizado() {
        return valorAcumulado;
    }

    public Conta getContaAssociada() {
        return contaAssociada;
    }

    public LocalDate getDataLimite() {
        return dataLimite;
    }

    public MetaInversaStatus getStatus() {
        return this.status;
    }

    public String getNome() {
        return this.nome;
    }

    // Setters
    public void setValorDivida(BigDecimal valorDivida) {
        this.valorDivida = valorDivida;
    }

    public void setDataLimite(LocalDate dataLimite) {
        this.dataLimite = dataLimite;
    }

    public void setNome (String nome) {
        this.nome = nome;
    }

}
