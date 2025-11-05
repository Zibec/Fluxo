package persistencia.jpa.cartao;

import cartao.CartaoNumero;
import cartao.Cvv;
import cartao.Fatura;
import jakarta.persistence.*;
import persistencia.jpa.Mapper;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.YearMonth;


@Entity
@Table(name = "CARTAO")
public class CartaoJpa {

    @Id
    public String id;

    public String usuarioId;

    public String numero;

    public String cvv;

    public String titular;
    public YearMonth validade;
    public BigDecimal limite;
    public BigDecimal saldo;
    public LocalDate dataFechamentoFatura;
    public LocalDate dataVencimentoFatura;
}
