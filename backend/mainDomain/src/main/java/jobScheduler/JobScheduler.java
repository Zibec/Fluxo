package jobScheduler;

import investimento.Investimento;
import investimento.InvestimentoService;

import java.util.List;

public class JobScheduler {

    private final InvestimentoService investimentoService;
    private List<Investimento> investimentos;

    public JobScheduler(InvestimentoService investmentoService, List<Investimento> investimentos) {
        this.investimentoService = investmentoService;
        this.investimentos = investimentos;
    }

    public void executarJob() {
        for (Investimento inv : investimentos) {
            if ("Tesouro Selic".equals(inv.getTipo())) {
                investimentoService.atualizarRendimento(inv);
            }
        }
    }

}
