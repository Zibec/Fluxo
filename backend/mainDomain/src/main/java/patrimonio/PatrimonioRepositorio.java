package patrimonio;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.apache.commons.lang3.Validate.notNull;

public class PatrimonioRepositorio {

    private final Map<String, Patrimonio> snapshots = new HashMap<>();

    public void salvar(Patrimonio snapshot) {
        notNull(snapshot, "O snapshot não pode ser nulo");
        snapshots.put(snapshot.getId(), snapshot);
    }

    public List<Patrimonio> obterTodos() {
        return new ArrayList<>(snapshots.values());
    }

    public void limpar() {
        snapshots.clear();
    }
}
