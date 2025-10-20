package orcamento;

import transacao.TransacaoService; // Importe o TransacaoService
import java.math.BigDecimal;
import java.time.YearMonth;
import static org.apache.commons.lang3.Validate.notNull;

public class OrcamentoService {
    private final OrcamentoRepositorio orcamentoRepositorio;
    private final TransacaoService transacaoService;

    public OrcamentoService(OrcamentoRepositorio orcamentoRepositorio, TransacaoService transacaoService) {
        this.orcamentoRepositorio = notNull(orcamentoRepositorio);
        this.transacaoService = notNull(transacaoService);
    }

    public void criarOrcamentoMensal(String usuarioId, String categoriaid, YearMonth anoMes, BigDecimal limite){
        var chave = new OrcamentoChave(usuarioId, anoMes, categoriaid);
        orcamentoRepositorio.salvarOrcamento(chave, new Orcamento(limite));
    }

    public void atualizarOrcamentoMensal(String usuarioId, String categoriaid, YearMonth anoMes, BigDecimal limite){
        var chave = new OrcamentoChave(usuarioId, anoMes, categoriaid);
        orcamentoRepositorio.atualizarOrcamento(chave, new Orcamento(limite));
    }

    public BigDecimal saldoMensalTotal(String usuarioId, String categoriaid, YearMonth anoMes){
        var chave = new OrcamentoChave(usuarioId, anoMes, categoriaid);
        var orcamento = orcamentoRepositorio.obterOrcamento(chave)
                .orElseThrow(()-> new IllegalStateException("Não existe um orçamento para essa chave"));

        var totalGasto = transacaoService.calcularGastosConsolidadosPorCategoria(categoriaid, anoMes);

        return orcamento.getLimite().subtract(totalGasto);
    }
}