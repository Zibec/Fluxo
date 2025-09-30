package investimento;

import historicoInvestimento.HistoricoInvestimento;
import historicoInvestimento.HistoricoInvestimentoService;
import selicApiClient.SelicApiClient;

import java.time.LocalDate;

import static org.apache.commons.lang3.Validate.notNull;

public class InvestimentoService {
    private final InvestimentoRepositorio investimentoRepositorio;
    private final HistoricoInvestimentoService historicoInvestimentoService;
    private final SelicApiClient selicApiClient;

    public InvestimentoService(InvestimentoRepositorio investimentoRepositorio, SelicApiClient selicApiClient, HistoricoInvestimentoService historicoInvestimentoService) {
        this.investimentoRepositorio = investimentoRepositorio;
        this.historicoInvestimentoService = historicoInvestimentoService;
        this.selicApiClient = selicApiClient;
    }

    public void salvar (Investimento investimento){
        notNull(investimento, "Invesimento não pode ser nulo");
        investimentoRepositorio.salvar(investimento);
    }
    public Investimento obter(String investimentoId){
        notNull(investimentoId, "Investimento não pode ser nulo");
        return investimentoRepositorio.obter(investimentoId).orElseThrow(() -> new IllegalArgumentException("Investimento não encontrado"));
    }

    public void atualizar(String investimentoId, Investimento investimento){
        notNull(investimentoId, "O id não pode ser nulo");
        notNull(investimento, "O investimento não pode ser nulo");
        investimentoRepositorio.atualizar(investimentoId, investimento);
    }

    public void atualizarRendimento (Investimento investimento){
        Double taxaSelic = selicApiClient.buscarTaxaSelicDiaria();

        if (taxaSelic == null) {
            throw new RuntimeException("Taxa Selic não disponível");
        }

        investimento.atualizarValor(taxaSelic);

        HistoricoInvestimento historicoInvestimento = new HistoricoInvestimento(
                investimento.getId(),
                investimento.getValorAtual(),
                LocalDate.now()
        );

        historicoInvestimentoService.salvar(historicoInvestimento);
    }
}
