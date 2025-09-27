package categoria;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import static org.apache.commons.lang3.Validate.notNull;

public class CategoriaRepositorio {

    private final Map<String, Categoria> categorias = new HashMap<>();

    public void salvar(Categoria categoria) {
        notNull(categoria, "A categoria não pode ser nula.");

        //nomes duplicados
        if (obterPorNome(categoria.getNome()).isPresent()) {
            throw new IllegalArgumentException("Categoria já existe");
        }
        categorias.put(categoria.getId(), categoria);
    }

    public Optional<Categoria> obterPorNome(String nome) {
        notNull(nome, "O nome da categoria não pode ser nulo.");
        return categorias.values().stream()
                .filter(cat -> cat.getNome().equalsIgnoreCase(nome))
                .findFirst();
    }

    public Optional<Categoria> obter(String id) {
        notNull(id, "O ID da categoria não pode ser nulo.");
        return Optional.ofNullable(categorias.get(id));
    }

    public void deletar(String id) {
        notNull(id, "O ID da categoria não pode ser nulo.");
        categorias.remove(id);
    }

    public int contagem() {
        return categorias.size();
    }
}