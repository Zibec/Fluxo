package com.fluxo.controllers.transacao;

import java.math.BigDecimal;
import java.time.LocalDate;

public record TransacaoDTO(
    String id,
    String origemAgendamentoId,
    String descricao,
    BigDecimal valor,
    LocalDate data,
    String status,
    String categoriaId,
    String tipo,
    String transacaoOriginalId,
    String pagamentoId,
    boolean avulsa,
    String perfilId
) {}

