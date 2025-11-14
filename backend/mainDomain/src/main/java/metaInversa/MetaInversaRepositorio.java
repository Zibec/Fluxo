package metaInversa;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.apache.commons.lang3.Validate.notNull;

public interface MetaInversaRepositorio {

    void salvarMetaInversa(MetaInversa metaInversa);

    Optional<MetaInversa> obterMetaInversa(String metaId);

    Optional<MetaInversa> obterMetaInversaPorNome(String nomeMeta);

    List<MetaInversa> obterMetaInversaPorUsuario(String usuarioId);

    void deletarMetaInversa(String metaId);

    void limparMetaInversa();
}
