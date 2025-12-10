package persistencia.jpa.jobs.job;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.time.LocalDateTime;
@Entity
@Table(name = "jobs_agendados")
public class JobAgendado {

    @Id
    private String id;
    private LocalDateTime data;
    private TipoJob tipoJob;
    private String auxiliar;

    public JobAgendado(String id, LocalDateTime data, TipoJob tipoJob, String auxiliar) {
        this.id = id;
        this.data = data;
        this.tipoJob = tipoJob;
        this.auxiliar = auxiliar;
    }

    public JobAgendado() {

    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public LocalDateTime getData() {
        return data;
    }

    public void setData(LocalDateTime data) {
        this.data = data;
    }

    public TipoJob getTipo() {
        return tipoJob;
    }

    public void setTipo(TipoJob tipoJob) {
        this.tipoJob = tipoJob;
    }

    public String getAuxiliar() {
        return auxiliar;
    }

    public void setAuxiliar(String auxiliar) {
        this.auxiliar = auxiliar;
    }
}

