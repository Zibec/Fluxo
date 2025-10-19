package metaInversa;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static org.apache.commons.lang3.Validate.notNull;

public class MetaInversaRepositorio {

    private final Map<String, MetaInversa> metasInversas = new HashMap<>();

    /**
     * Salva ou atualiza uma meta inversa no repositório.
     * Substitui o registro anterior caso o ID já exista.
     */
    public void salvar(MetaInversa metaInversa) {
        notNull(metaInversa, "A meta inversa não pode ser nula");
        metasInversas.put(metaInversa.getId(), metaInversa);
    }

    /**
     * Obtém uma meta inversa pelo seu ID.
     * @param metaId ID da meta inversa
     * @return Optional contendo a meta, se encontrada
     */
    public Optional<MetaInversa> obter(String metaId) {
        notNull(metaId, "O ID da meta inversa não pode ser nulo");
        return Optional.ofNullable(metasInversas.get(metaId));
    }

    /**
     * Obtém uma meta inversa pelo nome (case insensitive).
     * @param nomeMeta nome da meta inversa
     * @return Optional contendo a meta, se encontrada
     */
    public Optional<MetaInversa> obterPorNome(String nomeMeta) {
        notNull(nomeMeta, "O nome da meta inversa não pode ser nulo");

        return metasInversas.values().stream()
                .filter(meta -> meta.getNome().equalsIgnoreCase(nomeMeta))
                .findFirst();
    }

    /**
     * Deleta uma meta inversa do repositório pelo ID.
     * @param metaId ID da meta inversa a ser removida
     */
    public void deletar(String metaId) {
        notNull(metaId, "O ID da meta inversa não pode ser nulo");
        metasInversas.remove(metaId);
    }

    /**
     * Limpa o repositório (útil para testes unitários).
     */
    public void limpar() {
        metasInversas.clear();
    }
}
