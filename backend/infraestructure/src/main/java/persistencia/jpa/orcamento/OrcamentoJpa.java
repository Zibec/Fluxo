package persistencia.jpa.orcamento;

import jakarta.persistence.*;
import orcamento.OrcamentoChave;
import org.hibernate.annotations.Columns;
import persistencia.jpa.Mapper;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.apache.commons.lang3.Validate.isTrue;
import static org.apache.commons.lang3.Validate.notNull;

@Entity
@Table(
        name = "ORCAMENTO",
        uniqueConstraints = @UniqueConstraint(name = "uk_orcamento_usuario_cat_ano_mes",
        columnNames = {"usuario_id","categoria_id","ano","mes"}
        )
)

public class OrcamentoJpa {
    @Id
    @Column(name = "chave", nullable = false, length = 160)
    public String chave;

//Colunas sombreadas
    @Column(name = "usuario_id", nullable = false, length = 64)
    public String usuarioId;

    @Column(name= "categoria_id", nullable = false, length = 64)
    public String categoriaId;

    public String titulo;

    @Column(name = "ano", nullable = false)
    public int ano;

    @Column(name = "mes", nullable = false)
    public int mes;

    @Column(name = "limite", nullable = false, precision = 15, scale = 2)
    public BigDecimal limite;

    @Column(name = "data_limite", nullable = false)
    public LocalDate dataLimite;

    public OrcamentoJpa() {}
}
