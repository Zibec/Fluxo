// Em: src/main/java/categoria/CategoriaService.java
package categoria;

import java.util.Optional;
import static org.apache.commons.lang3.Validate.notNull;

public class CategoriaService {

    private final CategoriaRepositorio categoriaRepositorio;

    public CategoriaService(CategoriaRepositorio categoriaRepositorio) {
        this.categoriaRepositorio = categoriaRepositorio;
    }

    public void salvar(Categoria categoria) {
        notNull(categoria, "A categoria não pode ser nula.");
        categoriaRepositorio.salvar(categoria);
    }

    public Optional<Categoria> obterPorNome(String nome) {
        notNull(nome, "O nome da categoria não pode ser nulo.");
        return categoriaRepositorio.obterPorNome(nome);
    }

    public void deletar(String id) {
        notNull(id, "O ID da categoria não pode ser nulo.");

        if (categoriaRepositorio.obter(id).isEmpty()) {
            throw new IllegalArgumentException("Categoria não encontrada");
        }

        categoriaRepositorio.deletar(id);
    }
}