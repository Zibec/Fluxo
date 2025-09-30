package historicoInvestimento;

import investimento.Investimento;

import java.util.List;
import java.util.Map;

import static org.apache.commons.lang3.Validate.notNull;

public class HistoricoInvestimentoService {

    private final HistoricoInvestimentoRepositorio historicoInvestimentoRepositorio;

    public HistoricoInvestimentoService(HistoricoInvestimentoRepositorio historicoInvestimentoRepositorio) {
        this.historicoInvestimentoRepositorio = historicoInvestimentoRepositorio;
    }

    public void salvar (HistoricoInvestimento historicoInvestimento){
        notNull(historicoInvestimento, "Histórico não pode ser nulo");
        historicoInvestimentoRepositorio.salvar(historicoInvestimento);
    }

    public List<HistoricoInvestimento> obterTodos (){
        return historicoInvestimentoRepositorio.obterTodos();
    }

}
