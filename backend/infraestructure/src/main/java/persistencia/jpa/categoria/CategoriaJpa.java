package persistencia.jpa.categoria;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "CATEGORIA")
public class CategoriaJpa {

    @Id
    public String id;

    public String nome;

    public String usuarioId;
}

