package investimento;

import historicoInvestimento.HistoricoInvestimento;
import historicoInvestimento.HistoricoInvestimentoRepositorio;
import historicoInvestimento.HistoricoInvestimentoService;
import selicApiClient.SelicApiClient;
import taxaSelic.TaxaSelic;
import taxaSelic.TaxaSelicRepository;
import taxaSelic.TaxaSelicService;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import static org.apache.commons.lang3.Validate.notNull;

public class InvestimentoService {
    private final InvestimentoRepositorio investimentoRepositorio;
    private final HistoricoInvestimentoRepositorio historicoInvestimentoRepositorio;
    private final TaxaSelicRepository taxaSelicRepository;

    public InvestimentoService(InvestimentoRepositorio investimentoRepositorio, TaxaSelicRepository taxaSelicRepository, HistoricoInvestimentoRepositorio historicoInvestimentoRepositorio) {
        this.investimentoRepositorio = investimentoRepositorio;
        this.historicoInvestimentoRepositorio = historicoInvestimentoRepositorio;
        this.taxaSelicRepository = taxaSelicRepository;
    }

    public void salvar (Investimento investimento){
        notNull(investimento, "Invesimento não pode ser nulo");
        investimentoRepositorio.salvar(investimento);
    }
    public Investimento obter(String investimentoId){
        notNull(investimentoId, "Investimento não pode ser nulo");
        return investimentoRepositorio.obter(investimentoId);
    }

    public void atualizar(String investimentoId, Investimento investimento){
        notNull(investimentoId, "O id não pode ser nulo");
        notNull(investimento, "O investimento não pode ser nulo");
        investimentoRepositorio.atualizar(investimentoId, investimento);
    }

    public void atualizarRendimento (Investimento investimento){
        TaxaSelic taxaSelic = taxaSelicRepository.obter();

        if (taxaSelic == null) {
            throw new RuntimeException("Taxa Selic não disponível.");
        }

        investimento.atualizarValor(taxaSelic.getValor());

        HistoricoInvestimento historicoInvestimento = new HistoricoInvestimento(
                investimento.getId(),
                investimento.getValorAtual(),
                LocalDate.now()
        );

        historicoInvestimentoRepositorio.salvar(historicoInvestimento);
    }

    public void resgateTotal(String investimentoId){

        historicoInvestimentoRepositorio.deletarTodosPorId(investimentoId);

        investimentoRepositorio.deletar(investimentoId);

    }

    public void resgateParcial(String investimentoId, BigDecimal valor){
        Investimento investimento = investimentoRepositorio.obter(investimentoId);

        if(investimento.getValorAtual().compareTo(valor) == 0 || investimento.getValorAtual().compareTo(valor) < 0){
            throw new RuntimeException("Tentativa de resgate total em resgate parcial ou valor inválido.");
        }

        investimento.resgatarValor(valor);

        historicoInvestimentoRepositorio.salvar(new HistoricoInvestimento(investimentoId, investimento.getValorAtual(), LocalDate.now()));
    }
}
