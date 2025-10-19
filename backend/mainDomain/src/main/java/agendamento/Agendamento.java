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
        if (!this.ativo) return;
        if (this.proximaData == null) return;

        switch (this.frequencia) {
            case MENSAL -> {
                //guarda o dia desejado
                int diaDesejado = this.proximaData.getDayOfMonth();

                //soma 1 mês
                LocalDate candidata = this.proximaData.plusMonths(1);

                //limita ao último dia do mês destino
                int ultimoDiaMes = candidata.lengthOfMonth();
                this.proximaData = candidata.withDayOfMonth(Math.min(diaDesejado, ultimoDiaMes));
            }
            case SEMANAL -> this.proximaData = this.proximaData.plusWeeks(1);
            case DIARIA  -> this.proximaData = this.proximaData.plusDays(1);
            case ANUAL   -> {
                //ano bissexto: 29/02 pode cair para 28/02 em ano não bissexto
                int dia = this.proximaData.getDayOfMonth();
                int mes = this.proximaData.getMonthValue();
                LocalDate candidata = this.proximaData.plusYears(1);
                int ultimoDiaMes = candidata.withMonth(mes).lengthOfMonth();
                this.proximaData = candidata.withMonth(mes).withDayOfMonth(Math.min(dia, ultimoDiaMes));
            }
            default -> throw new IllegalStateException("Frequência não suportada: " + this.frequencia);
        }
    }
}
