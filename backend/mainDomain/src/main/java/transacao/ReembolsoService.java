package transacao;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;
import static org.apache.commons.lang3.Validate.notNull;

public class ReembolsoService {

    private final TransacaoRepositorio transacaoRepositorio;

    // O serviço depende APENAS do repositório de transação por enquanto.
    public ReembolsoService(TransacaoRepositorio transacaoRepositorio) {
        this.transacaoRepositorio = transacaoRepositorio;
    }

    public Transacao registrarReembolso(BigDecimal valorReembolso, String idDespesaOriginal) {
        notNull(valorReembolso, "O valor do reembolso não pode ser nulo");
        notNull(idDespesaOriginal, "O ID da despesa original não pode ser nulo");

        Transacao despesaOriginal = transacaoRepositorio.obterPorId(idDespesaOriginal)
                .orElseThrow(() -> new IllegalArgumentException("Despesa original não encontrada"));

        // Regra: Validação de Valor
        if (valorReembolso.compareTo(despesaOriginal.getValor()) > 0) {
            throw new IllegalArgumentException("O valor do reembolso não pode ser maior que o da despesa original");
        }

        // Criando a nova transação do tipo REEMBOLSO
        String novoId = UUID.randomUUID().toString();
        String descricaoReembolso = "Reembolso de: " + despesaOriginal.getDescricao();

        Transacao reembolso = new Transacao(
                novoId,
                null, // Não veio de agendamento
                descricaoReembolso,
                valorReembolso,
                LocalDate.now(),
                StatusTransacao.EFETIVADA, // Reembolsos já entram como efetivados
                despesaOriginal.getCategoriaId(),
                Transacao.Tipo.REEMBOLSO
        );

        // Regra: Vinculando o reembolso à despesa
        reembolso.setTransacaoOriginalId(idDespesaOriginal);

        transacaoRepositorio.salvar(reembolso);

        return reembolso;
    }
}