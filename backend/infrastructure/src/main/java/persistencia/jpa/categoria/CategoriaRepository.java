package persistencia.jpa.categoria;

import categoria.Categoria;
import categoria.CategoriaRepositorio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public class CategoriaRepository implements CategoriaRepositorio {
    @Autowired
    private CategoriaJpaRepository repository;

    @Override
    public void salvar(Categoria categoria) {
        repository.save(categoria);
    }

    @Override
    public Optional<Categoria> obterCategoriaPorNome(String nome) {
        return repository.findById(nome);
    }

    @Override
    public Optional<Categoria> obterCategoria(String id) {
        return repository.findById(id);
    }

    @Override
    public void deletarCategoria(String id) {
        repository.deleteById(id);
    }

    @Override
    public int contagem() {
        return (int) repository.count();
    }
}
