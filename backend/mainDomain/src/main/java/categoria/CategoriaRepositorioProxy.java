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
        System.out.println("[PROXY] obtendo categoria por nome...");
        return real.obterCategoriaPorNome(nome);
    }

    @Override
    public Optional<Categoria> obterCategoria(String id) {
        System.out.println("[PROXY] obtendo categoria por Id...");
        return real.obterCategoria(id);
    }

    @Override
    public void deletarCategoria(String id) {
        System.out.println("[PROXY] deletando categoria...");
        real.deletarCategoria(id);
    }

    @Override
    public int contagem() {
        System.out.println("[PROXY] contagem de categorias...");
        return real.contagem();
    }

    @Override
    public List<Categoria> listarCategorias() {
        System.out.println("[PROXY] listando categorias...");
        return real.listarCategorias();
    }

    @Override
    public List<Categoria> listarCategoriasPorUsuarioId(String id) {
        System.out.println("[PROXY] listando categorias por usuario...");
        return real.listarCategoriasPorUsuarioId(id);
    }
}
