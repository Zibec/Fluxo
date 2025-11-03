package com.fluxo.controllers.cartao;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.YearMonth;

public class CartaoDTO {
    public String id;

    public String numero;

    public String cvv;

    public String titular;
    public YearMonth validade;
    public BigDecimal limite;
    public BigDecimal saldo;
    public String dataFechamentoFatura;
    public String dataVencimentoFatura;

    public CartaoDTO() {}
}
