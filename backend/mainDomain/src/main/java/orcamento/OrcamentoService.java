package orcamento;

import java.math.BigDecimal;
import java.time.YearMonth;

public class OrcamentoService {
    private final OrcamentoRepositorio orcamentoRepositorio;
    private final TransacaoSumario transacaoSumario;

    public OrcamentoService(OrcamentoRepositorio orcamentoRepositorio, TransacaoSumario transacaoSumario) {
        this.orcamentoRepositorio = orcamentoRepositorio;
        this.transacaoSumario = transacaoSumario;
    }

    public void criarOrcamentoMensal(String usuarioId, String categoriaid, YearMonth anoMes, BigDecimal limite){

        var chave = new OrcamentoChave(usuarioId, anoMes, categoriaid);
        orcamentoRepositorio.salvarNovo(chave, new Orcamento(limite));
    }

    public void atualizarOrcamentoMensal(String usuarioId, String categoriaid, YearMonth anoMes, BigDecimal limite){

        var chave = new OrcamentoChave(usuarioId, anoMes, categoriaid);
        orcamentoRepositorio.atualizarOrcamento(chave, new Orcamento(limite));
    }

    public BigDecimal saldoMensalTotal(String usuarioId, String categoriaid, YearMonth anoMes){
        var chave = new OrcamentoChave(usuarioId, anoMes, categoriaid);
        var orcamento = orcamentoRepositorio.obterOrcamento(chave)
                .orElseThrow(()-> new IllegalStateException("Não existe um orçamento para essa chave"));

        var totalGasto = transacaoSumario.totalGastoMes(usuarioId, categoriaid, anoMes);
        return orcamento.getLimite().subtract(totalGasto);
    }
}
