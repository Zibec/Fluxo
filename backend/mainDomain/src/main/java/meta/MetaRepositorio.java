package meta;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.apache.commons.lang3.Validate.notNull;

public interface MetaRepositorio {

    void salvar(Meta meta);

    Optional<Meta> obterMeta(String metaId);

    Optional<Meta> obterMetaPorNome(String nomeMeta);

    void deletarMeta(String metaId);

    List<Meta> listar();

}