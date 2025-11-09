package persistencia.jpa.categoria;

import categoria.Categoria;
import categoria.CategoriaRepositorio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import persistencia.jpa.Mapper;

import java.util.List;
import java.util.Optional;

@Repository
public class CategoriaRepository implements CategoriaRepositorio {
    @Autowired
    private CategoriaJpaRepository repository;

    @Autowired
    private Mapper mapper;

    @Override
    public void salvar(Categoria categoria) {
        var categoriaJpa = mapper.map(categoria,CategoriaJpa.class);
        repository.save(categoriaJpa);
    }

    @Override
    public Optional<Categoria> obterCategoriaPorNome(String nome) {
        var categoriaJpa = repository.findByNome(nome);
        return Optional.of(mapper.map(categoriaJpa, Categoria.class));
    }

    @Override
    public Optional<Categoria> obterCategoria(String id) {
        var categoriaJpa = repository.findById(id);
        return Optional.of(mapper.map(categoriaJpa, Categoria.class));
    }

    @Override
    public void deletarCategoria(String id) {
        repository.deleteById(id);
    }

    @Override
    public int contagem() {
        return (int) repository.count();
    }

    @Override
    public List<Categoria> listarCategorias() {
        return List.of();
    }

    @Override
    public List<Categoria> listarCategoriasPorUsuarioId(String id) {
        List<?> categoriaJpa = repository.findAllByUsuarioId(id);

        if(categoriaJpa.isEmpty()){
            return List.of();
        }

        return mapper.map(categoriaJpa, List.class);
    }

}
