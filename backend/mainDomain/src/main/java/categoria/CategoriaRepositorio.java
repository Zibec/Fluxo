package categoria;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import static org.apache.commons.lang3.Validate.notNull;

public interface CategoriaRepositorio {

    void salvar(Categoria categoria);

    Optional<Categoria> obterCategoriaPorNome(String nome);

    Optional<Categoria> obterCategoria(String id);

    void deletarCategoria(String id);

    int contagem();
}