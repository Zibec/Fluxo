package com.fluxo.controllers.transacao;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

public record TransacaoDTO(
    String id,
    String origemAgendamentoId,
    String descricao,
    BigDecimal valor,
    LocalDateTime data,
    String status,
    String categoriaId,
    String tipo,
    String transacaoOriginalId,
    String pagamentoId,
    boolean avulsa,
    String perfilId
) {}

