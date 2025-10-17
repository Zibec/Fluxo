package patrimonio;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.apache.commons.lang3.Validate.notNull;

public class DividaRepositorio {

    private final Map<String, Divida> dividas = new HashMap<>();

    public void salvar(Divida divida) {
        notNull(divida, "A dívida não pode ser nula");
        dividas.put(divida.getId(), divida);
    }

    public List<Divida> obterTodos() {
        return new ArrayList<>(dividas.values());
    }

    public void limpar() {
        dividas.clear();
    }
}
