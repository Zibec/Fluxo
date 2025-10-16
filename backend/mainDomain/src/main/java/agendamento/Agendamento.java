package agendamento;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Objects;

import static org.apache.commons.lang3.Validate.isTrue;
import static org.apache.commons.lang3.Validate.notBlank;

public class Agendamento {
    private final String descricao;
    private final String id;
    private BigDecimal valor;
    private final Frequencia frequencia;
    private LocalDate proximaData;
    private boolean ativo = true;

    private String perfilId;
    public Agendamento(String id, String descricao, BigDecimal valor, Frequencia frequencia, LocalDate proximaData, String perfilId) {
        this.id = Objects.requireNonNull(id);
        this.descricao = notBlank(descricao, "Descrição obrigatória");
        isTrue(valor != null && valor.signum() >= 0, "Valor deve ser positivo");
        this.valor = valor;
        this.frequencia = Objects.requireNonNull(frequencia);
        this.proximaData = Objects.requireNonNull(proximaData);
        this.perfilId = perfilId;
    }

    public String getPerfilId() {
        return perfilId;
    }

    public String getId() { return id; }
    public String getDescricao() { return descricao; }
    public BigDecimal getValor() { return valor; }
    public void setValor(BigDecimal valor) { if (valor == null || valor.signum() < 0) {
        throw new IllegalArgumentException("O valor deve ser positivo e não nulo");
    }
        this.valor = valor;
    }
    public Frequencia getFrequencia() { return frequencia; }
    public LocalDate getProximaData() { return proximaData; }
    public boolean isAtivo() { return ativo; }
    public void cancelar() { this.ativo = false; this.proximaData = null; }

    public void avancarProximaData() {
        switch (frequencia) {
            case DIARIA -> proximaData = proximaData.plusDays(1);
            case SEMANAL -> proximaData = proximaData.plusWeeks(1);
            case MENSAL -> proximaData = proximaData.plusMonths(1)
                    .withDayOfMonth(Math.min(proximaData.getDayOfMonth(),
                            proximaData.plusMonths(0).lengthOfMonth()));
            case ANUAL -> proximaData = proximaData.plusYears(1);
        }
        // regra “meses curtos”: ao avançar mês, se dia 29/30/31 não existe, ajusta p/ último dia do mês
        var ultimoDia = proximaData.lengthOfMonth();
        if (proximaData.getDayOfMonth() > ultimoDia) {
            proximaData = proximaData.withDayOfMonth(ultimoDia);
        }
    }
}
