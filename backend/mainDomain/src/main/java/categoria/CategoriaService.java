// Em: src/main/java/categoria/CategoriaService.java
package categoria;

import transacao.TransacaoRepositorio;

import java.util.List;
import java.util.Optional;
import static org.apache.commons.lang3.Validate.notNull;

public class CategoriaService {

    private final CategoriaRepositorio categoriaRepositorio;
    private final TransacaoRepositorio transacaoRepositorio;

    public CategoriaService(CategoriaRepositorio categoriaRepositorio, TransacaoRepositorio transacaoRepositorio) {
        this.categoriaRepositorio = categoriaRepositorio;
        this.transacaoRepositorio = transacaoRepositorio;
    }
    public void salvar(Categoria categoria) {
        notNull(categoria, "A categoria não pode ser nula.");
        categoriaRepositorio.salvar(categoria);
    }

    public Optional<Categoria> obterPorNome(String nome) {
        notNull(nome, "O nome da categoria não pode ser nulo.");
        return categoriaRepositorio.obterCategoriaPorNome(nome);
    }

    public void deletar(String id) {
        notNull(id, "O ID da categoria não pode ser nulo.");

        if (categoriaRepositorio.obterCategoria(id).isEmpty()) {
            throw new IllegalArgumentException("Categoria não encontrada");
        }

        // VERIFICAÇÃO DE SEGURANÇA: A categoria está em uso?
        if (transacaoRepositorio.existeTransacaoPorCategoriaId(id)) {
            throw new IllegalStateException("Categoria não pode ser excluída pois está em uso");
        }

        // Se a verificação passar, a exclusão é permitida
        categoriaRepositorio.deletarCategoria(id);
    }

    public Optional<Categoria> obterCategoria(String id) {
        return categoriaRepositorio.obterCategoria(id);
    }

    public List<Categoria> listarCategorias() {
        return categoriaRepositorio.listarCategorias();
    }


}