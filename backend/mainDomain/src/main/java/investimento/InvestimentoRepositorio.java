package investimento;

import java.util.*;

import static org.apache.commons.lang3.Validate.notNull;

public class InvestimentoRepositorio {

    //Refatorar para ser uma lista.
    private ArrayList<Investimento> investimentos = new ArrayList<Investimento>();

    public void salvar(Investimento investimento){
        investimentos.add(investimento);
    }

    public Investimento obter(String investimentoId){
        for (Investimento inv : investimentos){
            if (investimentoId.equals(inv.getId())){
                return inv;
            }
        }
        return null;
    }

    public ArrayList<Investimento> obterTodos(){
        return investimentos;
    }

    public void atualizar(String investimentoId, Investimento investimento){

        for (Investimento inv : investimentos){
            if(investimentoId.equals(inv.getId())){
                inv = investimento;
            }
        }
    }

    public void deletar(String investimentoId){
        notNull(investimentoId, "O id não pode ser nulo");
        for (Investimento inv : investimentos){
            if (investimentoId.equals(inv.getId())){
                investimentos.remove(inv);
            }
        }

    }

}
