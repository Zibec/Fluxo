package persistencia.jpa.perfil;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "PERFIL")
public class PerfilJpa {
    @Id
    public String id;
    public String nome;
}