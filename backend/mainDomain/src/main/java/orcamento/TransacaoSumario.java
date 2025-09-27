package orcamento;

import java.math.BigDecimal;
import java.time.YearMonth;

public interface TransacaoSumario {
    BigDecimal totalGastoMes(String usuarioId, String categoriaId, YearMonth anoMes);
}
