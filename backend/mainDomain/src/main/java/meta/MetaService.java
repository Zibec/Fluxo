package meta;

import java.util.Optional;

import static org.apache.commons.lang3.Validate.notNull;

public class MetaService {
    private final MetaRepositorio metaRepositorio;

    public MetaService(MetaRepositorio repositorio) {
        this.metaRepositorio = repositorio;
    }

    public void salvar(Meta meta) {
        notNull(meta, "O cartão não pode ser nulo");
        metaRepositorio.salvar(meta);
    }

    public Optional<Meta> obter(String id) {
        notNull(id, "O número do cartão não pode ser nulo");
        return metaRepositorio.obter(id);
    }
}
