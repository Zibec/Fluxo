package patrimonio;

import conta.ContaRepositorio;
import divida.DividaRepositorio;
import investimento.InvestimentoRepositorio;
import metaInversa.MetaInversaRepositorio;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.YearMonth; // Adicionado import
import java.util.List;
import static org.apache.commons.lang3.Validate.notNull;

public class PatrimonioService {

    private final ContaRepositorio contaRepositorio;
    private final InvestimentoRepositorio investimentoRepositorio;
    private final DividaRepositorio dividaRepositorio;
    private final PatrimonioRepositorio snapshotRepositorio;
    private final MetaInversaRepositorio metaInversaRepositorio;

    public PatrimonioService(ContaRepositorio contaRepositorio,
                             InvestimentoRepositorio investimentoRepositorio,
                             DividaRepositorio dividaRepositorio,
                             PatrimonioRepositorio snapshotRepositorio) {
        this.contaRepositorio = notNull(contaRepositorio, "contaRepositorio não pode ser nulo.");
        this.investimentoRepositorio = notNull(investimentoRepositorio, "investimentoRepositorio não pode ser nulo.");
        this.dividaRepositorio = notNull(dividaRepositorio, "dividaRepositorio não pode ser nulo.");
        this.snapshotRepositorio = notNull(snapshotRepositorio, "snapshotRepositorio não pode ser nulo.");
        this.metaInversaRepositorio = null;
    }

    public PatrimonioService(ContaRepositorio contaRepositorio,
                             InvestimentoRepositorio investimentoRepositorio,
                             MetaInversaRepositorio metaInversaRepositorio,
                             PatrimonioRepositorio snapshotRepositorio) {
        this.contaRepositorio = notNull(contaRepositorio, "contaRepositorio não pode ser nulo.");
        this.investimentoRepositorio = notNull(investimentoRepositorio, "investimentoRepositorio não pode ser nulo.");
        this.metaInversaRepositorio = notNull(metaInversaRepositorio, "metaInversaRepositorio não pode ser nulo.");
        this.snapshotRepositorio = notNull(snapshotRepositorio, "snapshotRepositorio não pode ser nulo.");
        this.dividaRepositorio = null;
    }

    public BigDecimal calcularPatrimonioLiquido(String usuarioId) {
        BigDecimal totalContas = contaRepositorio.listarTodasContas().stream()
                .map(conta -> conta.getSaldo())
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal totalInvestimentos = investimentoRepositorio.obterTodosInvestimentos().stream()
                .map(investimento -> investimento.getValorAtual())
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal totalDividas;

        if(dividaRepositorio != null) {
            totalDividas = dividaRepositorio.obterTodosDivida().stream()
                    .map(divida -> divida.getValorDevedor())
                    .reduce(BigDecimal.ZERO, BigDecimal::add);
        } else {
            totalDividas = metaInversaRepositorio.obterMetaInversaPorUsuario(usuarioId).stream()
                    .map(metaInversa -> (metaInversa.getValorDivida().subtract(metaInversa.getValorAmortizado())))
                    .reduce(BigDecimal.ZERO, BigDecimal::add);
        }


        return totalContas.add(totalInvestimentos).subtract(totalDividas);
    }


    public void gerarEsalvarSnapshot(LocalDate data, String usuarioId) {
        YearMonth yearMonth = YearMonth.from(data);
        boolean isUltimoDiaDoMes = data.equals(yearMonth.atEndOfMonth());

        if (isUltimoDiaDoMes) {
            BigDecimal valorAtual = calcularPatrimonioLiquido(usuarioId);
            Patrimonio snapshot = new Patrimonio(data, valorAtual);
            snapshotRepositorio.salvarPatrimonio(snapshot);
        }
    }
    public List<Patrimonio> obterHistoricoDePatrimonio() {
        return snapshotRepositorio.obterTodosPatrimonios();
    }

    public void salvarSnapshot(Patrimonio snapshot) {
        notNull(snapshot, "O snapshot não pode ser nulo");
        snapshotRepositorio.salvarPatrimonio(snapshot);
    }

    public void limparPatrimonio() {
        snapshotRepositorio.limparPatrimonio();
    }
}