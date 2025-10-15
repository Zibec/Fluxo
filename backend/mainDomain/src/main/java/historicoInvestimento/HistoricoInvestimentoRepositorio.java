package historicoInvestimento;

import investimento.Investimento;

import java.sql.Array;
import java.util.*;

public class HistoricoInvestimentoRepositorio {

    private List<HistoricoInvestimento> historico = new ArrayList<>();
    private boolean status;

    public void setStatus(boolean status) {
        this.status = status;
    }

    public void salvar(HistoricoInvestimento historicoInvestimento){
        if(status){
            historico.add(historicoInvestimento);
        }
        else{
            throw new RuntimeException("Falha ao salvar histórico");
        }

    }

    public List<HistoricoInvestimento> obterTodos(){
        return historico;
    }
}
