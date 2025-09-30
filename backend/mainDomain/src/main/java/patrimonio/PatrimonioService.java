package patrimonio;

import conta.ContaRepositorio;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List; // Adicionado import
import static org.apache.commons.lang3.Validate.notNull;

public class PatrimonioService {

    private final ContaRepositorio contaRepositorio;
    private final InvestimentoRepositorio investimentoRepositorio;
    private final DividaRepositorio dividaRepositorio;
    private final SnapshotPatrimonioRepositorio snapshotRepositorio; // --- ADIÇÃO 1 ---

    public PatrimonioService(ContaRepositorio contaRepositorio,
                             InvestimentoRepositorio investimentoRepositorio,
                             DividaRepositorio dividaRepositorio,
                             SnapshotPatrimonioRepositorio snapshotRepositorio) { // --- ADIÇÃO 2 ---
        this.contaRepositorio = notNull(contaRepositorio, "contaRepositorio não pode ser nulo.");
        this.investimentoRepositorio = notNull(investimentoRepositorio, "investimentoRepositorio não pode ser nulo.");
        this.dividaRepositorio = notNull(dividaRepositorio, "dividaRepositorio não pode ser nulo.");
        this.snapshotRepositorio = notNull(snapshotRepositorio, "snapshotRepositorio não pode ser nulo."); // --- ADIÇÃO 3 ---
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

    // --- ADIÇÃO 4: Novo método para gerar e salvar o snapshot ---
    /**
     * Calcula o patrimônio líquido atual e salva um registro histórico (snapshot) com a data fornecida.
     */
    public void gerarEsalvarSnapshot(LocalDate data) {
        BigDecimal valorAtual = calcularPatrimonioLiquido();
        SnapshotPatrimonio snapshot = new SnapshotPatrimonio(data, valorAtual);
        snapshotRepositorio.salvar(snapshot);
    }

    // --- ADIÇÃO 5: Novo método para buscar o histórico para o gráfico ---
    /**
     * Retorna a lista de todos os snapshots salvos.
     */
    public List<SnapshotPatrimonio> obterHistoricoDePatrimonio() {
        return snapshotRepositorio.obterTodos();
    }
}