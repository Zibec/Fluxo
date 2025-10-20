package metaInversa;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static org.apache.commons.lang3.Validate.notNull;

public interface MetaInversaRepositorio {

    void salvarMetaInversa(MetaInversa metaInversa);

    Optional<MetaInversa> obterMetaInversa(String metaId);

    Optional<MetaInversa> obterMetaInversaPorNome(String nomeMeta);

    void deletarMetaInversa(String metaId);

    void limparMetaInversa();
}
