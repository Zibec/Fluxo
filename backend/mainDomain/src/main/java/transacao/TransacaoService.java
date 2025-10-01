package transacao;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

import conta.Conta;

public class TransacaoService {
    private final TransacaoRepositorio repo;

    public TransacaoService(TransacaoRepositorio repo) {
        this.repo = Objects.requireNonNull(repo);
    }

    /**
     * Cria uma transação PENDENTE oriunda de agendamento.
     * Idempotente por (agendamentoId, data): se já existe, retorna a existente.
     */
    public Transacao criarPendenteDeAgendamento(String agendamentoId, String descricao, BigDecimal valor, LocalDate data, Conta conta, boolean avulsa) {
        Optional<Transacao> existente = repo.encontrarPorAgendamentoEData(agendamentoId, data);
        if (existente.isPresent()) {
            return existente.get(); // idempotência: não duplica
        }

        Transacao t = new Transacao(
                UUID.randomUUID().toString(),
                agendamentoId,
                descricao,
                valor,
                data,
                StatusTransacao.PENDENTE,
                conta,
                avulsa
        );
        repo.salvar(t);
        return t;
    }
}
