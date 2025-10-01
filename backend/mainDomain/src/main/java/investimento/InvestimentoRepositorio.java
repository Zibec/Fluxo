package investimento;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import static org.apache.commons.lang3.Validate.notNull;

public class InvestimentoRepositorio {

    private final Map<String, Investimento> investimentos = new HashMap<>();

    public void salvar(Investimento investimento){
        investimentos.put(investimento.getId(), investimento);
    }

    public Optional<Investimento> obter(String investimentoId){
        return Optional.ofNullable(investimentos.get(investimentoId));
    }

    public void atualizar(String investimentoId, Investimento investimento){

        if(investimentos.replace(investimentoId, investimento) == null){
            throw new IllegalStateException("Não existe um investimento com esse id");
        }
    }

    public void deletar(String investimentoId){
        notNull(investimentoId, "O id não pode ser nulo");
        investimentos.remove(investimentoId);
    }

}
