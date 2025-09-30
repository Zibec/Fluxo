package patrimonio;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.apache.commons.lang3.Validate.notNull;

public class SnapshotPatrimonioRepositorio {

    private final Map<String, SnapshotPatrimonio> snapshots = new HashMap<>();

    public void salvar(SnapshotPatrimonio snapshot) {
        notNull(snapshot, "O snapshot não pode ser nulo");
        snapshots.put(snapshot.getId(), snapshot);
    }

    public List<SnapshotPatrimonio> obterTodos() {
        return new ArrayList<>(snapshots.values());
    }

    public void limpar() {
        snapshots.clear();
    }
}
