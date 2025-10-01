package transacao;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;
import java.util.List;
import java.util.ArrayList;

import static org.apache.commons.lang3.Validate.notBlank;
import static org.apache.commons.lang3.Validate.notNull;

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
                avulsa,
                Transacao.Tipo.DESPESA
        );
        repo.salvar(t);
        return t;
    }

    public BigDecimal calcularGastosConsolidadosPorCategoria(String categoriaId, YearMonth mes) {
        notBlank(categoriaId, "O ID da categoria não pode ser vazio.");
        notNull(mes, "O mês não pode ser nulo.");

        // Busca todas as transações (em um sistema real, isso seria otimizado)
        List<Transacao> todasTransacoes = repo.listarTodas();

        // Soma todas as DESPESAS da categoria no mês especificado
        BigDecimal totalDespesas = todasTransacoes.stream()
                .filter(t -> t.getTipo() == Transacao.Tipo.DESPESA)
                .filter(t -> categoriaId.equals(t.getCategoriaId()))
                .filter(t -> YearMonth.from(t.getData()).equals(mes))
                .map(Transacao::getValor)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        // Soma todos os REEMBOLSOS da categoria no mês especificado
        BigDecimal totalReembolsos = todasTransacoes.stream()
                .filter(t -> t.getTipo() == Transacao.Tipo.REEMBOLSO)
                .filter(t -> categoriaId.equals(t.getCategoriaId()))
                .filter(t -> YearMonth.from(t.getData()).equals(mes))
                .map(Transacao::getValor)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        // O gasto consolidado é a diferença
        return totalDespesas.subtract(totalReembolsos);
    }
}
