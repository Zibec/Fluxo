package categoria;

import java.util.List;
import java.util.Optional;

public class CategoriaRepositorioProxy implements CategoriaRepositorio {

    private final CategoriaRepositorio real;

    public CategoriaRepositorioProxy(CategoriaRepositorio real) {
        this.real = real;
    }

    @Override
    public void salvar(Categoria categoria) {
        System.out.println("[PROXY] validando antes de salvar...");
        real.salvar(categoria);
    }

    @Override
    public Optional<Categoria> obterCategoriaPorNome(String nome) {
        return real.obterCategoriaPorNome(nome);
    }

    @Override
    public Optional<Categoria> obterCategoria(String id) {
        return real.obterCategoria(id);
    }

    @Override
    public void deletarCategoria(String id) {
        System.out.println("[PROXY] deletando categoria...");
        real.deletarCategoria(id);
    }

    @Override
    public int contagem() {
        return real.contagem();
    }

    @Override
    public List<Categoria> listarCategorias() {
        return real.listarCategorias();
    }

    @Override
    public List<Categoria> listarCategoriasPorUsuarioId(String id) {
        return real.listarCategoriasPorUsuarioId(id);
    }
}
