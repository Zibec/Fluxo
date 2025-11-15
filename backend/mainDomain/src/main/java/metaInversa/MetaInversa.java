package metaInversa;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.UUID;


public class MetaInversa {
    private String id;
    private String nome;
    private BigDecimal valorDivida;
    private BigDecimal valorAcumulado;
    private String contaAssociadaId;
    private LocalDate dataLimite;
    private MetaInversaStatus status;

    private String usuarioId;

    public MetaInversa() {
        this.id = UUID.randomUUID().toString();
        this.valorAcumulado = BigDecimal.ZERO;
    }

    public MetaInversa(String id, String nome, BigDecimal valorDivida, String contaAssociada, LocalDate dataLimite) {
        this.id = id;
        this.nome = nome;
        this.valorDivida = valorDivida;
        this.contaAssociadaId = contaAssociada;
        this.dataLimite = dataLimite;
        this.valorAcumulado = BigDecimal.ZERO;
        this.status = MetaInversaStatus.ATIVA;
    }

    public MetaInversa(String id, String nome, BigDecimal valorDivida, String contaAssociada, LocalDate dataLimite, BigDecimal valorAcumulado,MetaInversaStatus status) {
        this.id = id;
        this.nome = nome;
        this.valorDivida = valorDivida;
        this.contaAssociadaId = contaAssociada; 
        this.dataLimite = dataLimite;
        this.valorAcumulado = valorAcumulado;
        this.status = status;
    }

    private void conluirMeta() {
        this.status = MetaInversaStatus.CONCLUIDA;
    }

    public void realizarAporte(BigDecimal valorDoAporte) {
        if (valorDoAporte == null || valorDoAporte.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Valor do aporte deve ser um número positivo.");
        }
        this.valorAcumulado = this.valorAcumulado.add(valorDoAporte);

        if (this.valorAcumulado.compareTo(this.valorDivida) >= 0) {
            conluirMeta();
        }
    }

    public BigDecimal getProgresso() {
        if (valorDivida == null || valorAcumulado == null || valorDivida.compareTo(BigDecimal.ZERO) == 0) {
            return BigDecimal.ZERO;
        }
        return valorAcumulado.divide(valorDivida, 2, RoundingMode.HALF_UP);
    }

    public String getId() {
        return id;
    }

    public String getUsuarioId() {
        return usuarioId;
    }

    public void setUsuarioId(String usuarioId) {
        this.usuarioId = usuarioId;
    }

    public BigDecimal getValorDivida() {
        return valorDivida;
    }

    public BigDecimal getValorAmortizado() {
        return valorAcumulado;
    }

    public String getContaAssociadaId() {
        return contaAssociadaId;
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

    public void setContaAssociada(String id) {
        this.contaAssociadaId = id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setValorAmortizado(BigDecimal valor) {
        this.valorAcumulado = valor;
    }

}
