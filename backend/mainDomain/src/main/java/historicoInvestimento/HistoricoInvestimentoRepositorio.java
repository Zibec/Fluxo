package historicoInvestimento;

import investimento.Investimento;

import java.sql.Array;
import java.util.*;

public class HistoricoInvestimentoRepositorio {

    private List<HistoricoInvestimento> historico = new ArrayList<>();


    public void salvar(HistoricoInvestimento historicoInvestimento){
        historico.add(historicoInvestimento);
    }

    public List<HistoricoInvestimento> obterTodos(){
        return historico;
    }
}
