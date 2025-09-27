package meta;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static org.apache.commons.lang3.Validate.notNull;

public class MetaRepositorio {

    private final Map<String, Meta> metas = new HashMap<>();

    public void salvar(Meta meta) {
        notNull(meta, "A meta não pode ser nula");
        metas.put(meta.getId(), meta);
    }

    public Optional<Meta> obter(String metaId) {
        notNull(metaId, "O ID da meta não pode ser nulo");

        // Retorna um Optional para evitar retornos nulos (NullPointerException)
        return Optional.ofNullable(metas.get(metaId));
    }

    public Optional<Meta> obterPorNome(String nomeMeta) {
        notNull(nomeMeta, "O nome da meta não pode ser nulo");

        return metas.values().stream()
                .filter(meta -> meta.getDescricao().equalsIgnoreCase(nomeMeta))
                .findFirst();
    }

    public void deletar(String metaId) {
        notNull(metaId, "O ID da meta não pode ser nulo");
        metas.remove(metaId);
    }


}