package jobScheduler;

import investimento.Investimento;
import investimento.InvestimentoRepositorio;
import investimento.InvestimentoService;

import java.util.ArrayList;
import java.util.List;

public class JobScheduler {

    private final InvestimentoService investimentoService;
    private InvestimentoRepositorio investimentoRepositorio;

    public JobScheduler(InvestimentoService investmentoService, InvestimentoRepositorio investimentoRepositorio) {
        this.investimentoService = investmentoService;
        this.investimentoRepositorio = investimentoRepositorio;
    }

    public void executarJob() {
        for (Investimento inv : investimentoRepositorio.obterTodos()) {
            if ("Tesouro Selic".equals(inv.getTipo())) {
                investimentoService.atualizarRendimento(inv);
            }
        }
    }

}
