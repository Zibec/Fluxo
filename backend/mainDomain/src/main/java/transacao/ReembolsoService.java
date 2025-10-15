package transacao;

import conta.Conta;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

public class ReembolsoService {

    private final TransacaoRepositorio transacaoRepositorio;

    public ReembolsoService(TransacaoRepositorio transacaoRepositorio) {
        this.transacaoRepositorio = transacaoRepositorio;
    }

    public Transacao registrarReembolso(BigDecimal valorReembolso, String idDespesaOriginal) {
        if (valorReembolso == null) {
            throw new IllegalArgumentException("O valor do reembolso não pode ser nulo");
        }
        if (idDespesaOriginal == null) {
            throw new IllegalArgumentException("O ID da despesa original não pode ser nulo");
        }

        Transacao despesaOriginal = transacaoRepositorio.obterPorId(idDespesaOriginal)
                .orElseThrow(() -> new IllegalArgumentException("Despesa original não encontrada"));

        if (valorReembolso.compareTo(despesaOriginal.getValor()) > 0) {
            throw new IllegalArgumentException("O valor do reembolso não pode ser maior que o da despesa original");
        }

        String novoId = UUID.randomUUID().toString();
        String descricaoReembolso = "Reembolso de: " + despesaOriginal.getDescricao();

        Conta contaDaDespesaOriginal = despesaOriginal.getContaAssociada();

        Transacao reembolso = new Transacao(
                novoId,
                null,
                descricaoReembolso,
                valorReembolso,
                LocalDate.now(),
                StatusTransacao.EFETIVADA,
                despesaOriginal.getCategoriaId(),
                contaDaDespesaOriginal,
                true,
                Transacao.Tipo.REEMBOLSO
        );

        reembolso.setTransacaoOriginalId(idDespesaOriginal);
        transacaoRepositorio.salvar(reembolso);
        return reembolso;
    }
}