package patrimonio;

import conta.ContaRepositorio;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.YearMonth; // Adicionado import
import java.util.List;
import static org.apache.commons.lang3.Validate.notNull;

public class PatrimonioService {

    private final ContaRepositorio contaRepositorio;
    private final InvestimentoRepositorio investimentoRepositorio;
    private final DividaRepositorio dividaRepositorio;
    private final SnapshotPatrimonioRepositorio snapshotRepositorio;

    public PatrimonioService(ContaRepositorio contaRepositorio,
                             InvestimentoRepositorio investimentoRepositorio,
                             DividaRepositorio dividaRepositorio,
                             SnapshotPatrimonioRepositorio snapshotRepositorio) {
        this.contaRepositorio = notNull(contaRepositorio, "contaRepositorio não pode ser nulo.");
        this.investimentoRepositorio = notNull(investimentoRepositorio, "investimentoRepositorio não pode ser nulo.");
        this.dividaRepositorio = notNull(dividaRepositorio, "dividaRepositorio não pode ser nulo.");
        this.snapshotRepositorio = notNull(snapshotRepositorio, "snapshotRepositorio não pode ser nulo.");
    }

    public BigDecimal calcularPatrimonioLiquido() {
        BigDecimal totalContas = contaRepositorio.listarTodas().stream()
                .map(conta -> conta.getSaldo())
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal totalInvestimentos = investimentoRepositorio.obterTodos().stream()
                .map(investimento -> investimento.getValorAtual())
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal totalDividas = dividaRepositorio.obterTodos().stream()
                .map(divida -> divida.getValorDevedor())
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        return totalContas.add(totalInvestimentos).subtract(totalDividas);
    }

    /**
     * Calcula o patrimônio líquido e salva um registro histórico (snapshot)
     * APENAS se a data fornecida for o último dia do seu respectivo mês.
     */
    public void gerarEsalvarSnapshot(LocalDate data) {
        // --- LÓGICA AJUSTADA AQUI ---
        YearMonth yearMonth = YearMonth.from(data);
        boolean isUltimoDiaDoMes = data.equals(yearMonth.atEndOfMonth());

        if (isUltimoDiaDoMes) {
            BigDecimal valorAtual = calcularPatrimonioLiquido();
            SnapshotPatrimonio snapshot = new SnapshotPatrimonio(data, valorAtual);
            snapshotRepositorio.salvar(snapshot);
        }
        // Se não for o último dia, ele simplesmente não faz nada.
    }

    public List<SnapshotPatrimonio> obterHistoricoDePatrimonio() {
        return snapshotRepositorio.obterTodos();
    }
}