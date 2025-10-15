package investimento;

import historicoInvestimento.HistoricoInvestimento;
import historicoInvestimento.HistoricoInvestimentoService;
import selicApiClient.SelicApiClient;
import taxaSelic.TaxaSelic;
import taxaSelic.TaxaSelicRepository;
import taxaSelic.TaxaSelicService;

import java.time.LocalDate;

import static org.apache.commons.lang3.Validate.notNull;

public class InvestimentoService {
    private final InvestimentoRepositorio investimentoRepositorio;
    private final HistoricoInvestimentoService historicoInvestimentoService;
    private final TaxaSelicRepository taxaSelicRepository;

    public InvestimentoService(InvestimentoRepositorio investimentoRepositorio, TaxaSelicRepository taxaSelicRepository, HistoricoInvestimentoService historicoInvestimentoService) {
        this.investimentoRepositorio = investimentoRepositorio;
        this.historicoInvestimentoService = historicoInvestimentoService;
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

        historicoInvestimentoService.salvar(historicoInvestimento);
    }
}
