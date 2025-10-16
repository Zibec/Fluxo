package patrimonio;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.apache.commons.lang3.Validate.notNull;

public class InvestimentoRepositorio {

    private final Map<String, Investimento> investimentos = new HashMap<>();

    public void salvar(Investimento investimento) {
        notNull(investimento, "O investimento não pode ser nulo");
        investimentos.put(investimento.getId(), investimento);
    }

    public List<Investimento> obterTodos() {
        return new ArrayList<>(investimentos.values());
    }

    public void limpar() {
        investimentos.clear();
    }
}
