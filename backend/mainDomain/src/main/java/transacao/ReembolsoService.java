package transacao;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;
// O import do 'notNull' foi removido pois não é mais necessário
// import static org.apache.commons.lang3.Validate.notNull;

public class ReembolsoService {

    private final TransacaoRepositorio transacaoRepositorio;

    public ReembolsoService(TransacaoRepositorio transacaoRepositorio) {
        this.transacaoRepositorio = transacaoRepositorio;
    }

    public Transacao registrarReembolso(BigDecimal valorReembolso, String idDespesaOriginal) {
        // --- VALIDAÇÃO AJUSTADA AQUI ---
        if (valorReembolso == null) {
            throw new IllegalArgumentException("O valor do reembolso não pode ser nulo");
        }
        if (idDespesaOriginal == null) {
            throw new IllegalArgumentException("O ID da despesa original não pode ser nulo");
        }
        // --- FIM DO AJUSTE ---

        Transacao despesaOriginal = transacaoRepositorio.obterPorId(idDespesaOriginal)
                .orElseThrow(() -> new IllegalArgumentException("Despesa original não encontrada"));

        if (valorReembolso.compareTo(despesaOriginal.getValor()) > 0) {
            throw new IllegalArgumentException("O valor do reembolso não pode ser maior que o da despesa original");
        }

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

        reembolso.setTransacaoOriginalId(idDespesaOriginal);

        transacaoRepositorio.salvar(reembolso);

        return reembolso;
    }
}