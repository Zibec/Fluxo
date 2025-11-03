package orcamento;

import transacao.TransacaoService;
import java.math.BigDecimal;
import java.time.YearMonth;
import java.util.Optional;
import java.util.function.BiConsumer;

import static org.apache.commons.lang3.Validate.notNull;

public class OrcamentoService {
    private final OrcamentoRepositorio orcamentoRepositorio;
    private final TransacaoService transacaoService;

    // quem quiser persiste/manda push/email etc.
    private BiConsumer<OrcamentoChave, String> notificador = (chave, msg) -> {};

    public OrcamentoService(OrcamentoRepositorio orcamentoRepositorio, TransacaoService transacaoService) {
        this.orcamentoRepositorio = notNull(orcamentoRepositorio);
        this.transacaoService = notNull(transacaoService);
    }

    public void criarOrcamentoMensal(String usuarioId, String categoriaid, YearMonth anoMes, BigDecimal limite){
        var chave = new OrcamentoChave(usuarioId, anoMes, categoriaid);
        orcamentoRepositorio.salvarOrcamento(chave, new Orcamento(limite));
    }

    public void atualizarOrcamento(String usuarioId, String categoriaid, YearMonth anoMes, BigDecimal limite){
        var chave = new OrcamentoChave(usuarioId, anoMes, categoriaid);
        orcamentoRepositorio.atualizarOrcamento(chave, new Orcamento(limite));
    }

    public BigDecimal saldoMensalTotal(String usuarioId, String categoriaid, YearMonth anoMes){
        var chave = new OrcamentoChave(usuarioId, anoMes, categoriaid);
        var orcamento = orcamentoRepositorio.obterOrcamento(chave)
                .orElseThrow(() -> new IllegalStateException("Não existe um orçamento para essa chave"));

        //usa a versão sem usuário, como você decidiu
        var totalGasto = transacaoService.calcularGastosConsolidadosPorCategoria(categoriaid, anoMes);
        return orcamento.getLimite().subtract(totalGasto);
    }

    public Optional<Orcamento> obterOrcamento(OrcamentoChave chave){
        return orcamentoRepositorio.obterOrcamento(chave);
    }

    public void limparOrcamento(){
        orcamentoRepositorio.limparOrcamento();
    }

    //LÓGICA DE NOTIFICAÇÕES

    /**
     * Avalia cruzamento de limiar dado um lançamento (valor que acabou de ser registrado).
     * NÃO grava transação; só calcula antes→depois para decidir notificação.
     */
    public Optional<String> avaliarLimiarAposLancamento(String usuarioId, String categoriaId, YearMonth anoMes, BigDecimal valorLancado) {
        notNull(valorLancado);
        var chave  = new OrcamentoChave(usuarioId, anoMes, categoriaId);
        var orc    = orcamentoRepositorio.obterOrcamento(chave)
                .orElseThrow(() -> new IllegalStateException("Orçamento não encontrado"));
        var limite = orc.getLimite();

        //versão sem usuário, como está no seu TransacaoService hoje
        var antes  = transacaoService.calcularGastosConsolidadosPorCategoria(categoriaId, anoMes);
        var depois = antes.add(valorLancado);

        var oitenta = limite.multiply(BigDecimal.valueOf(0.8));

        String msg = null;
        if (antes.compareTo(oitenta) < 0 && depois.compareTo(oitenta) >= 0 && depois.compareTo(limite) < 0) {
            msg = "Você atingiu 80% do limite definido";
        } else if (antes.compareTo(limite) < 0 && depois.compareTo(limite) == 0) {
            msg = "Você atingiu 100% do limite definido";
        } else if (depois.compareTo(limite) > 0) {
            msg = "Você excedeu o limite desta categoria";
        }

        if (msg != null) notificador.accept(chave, msg);
        return Optional.ofNullable(msg);
    }
}