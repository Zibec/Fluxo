package persistencia.jpa.meta;

import meta.Meta;
import meta.MetaRepositorio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import persistencia.jpa.Mapper;

import java.util.List;
import java.util.Optional;

@Repository
public class MetaRepositoryImpl implements MetaRepositorio {

    @Autowired
    private MetaJpaRepository repository;

    @Autowired
    private Mapper mapper;

    @Override
    public void salvar(Meta meta) {
        MetaJpa jpa = mapper.map(meta, MetaJpa.class);
        repository.save(jpa); // save faz update quando o ID existe
    }

    @Override
    public Optional<Meta> obterMeta(String metaId) {
        return repository.findById(metaId)
                .map(jpa -> mapper.map(jpa, Meta.class)); // <-- mapeia o conteúdo do Optional
    }

    @Override
    public Optional<Meta> obterMetaPorNome(String nomeMeta) {
        MetaJpa jpa = repository.findByDescricao(nomeMeta);
        return Optional.ofNullable(jpa)
                .map(entity -> mapper.map(entity, Meta.class)); // <-- trata null corretamente
    }

    @Override
    public void deletarMeta(String metaId) {
        repository.deleteById(metaId);
    }

    @Override
    public List<Meta> listar() {
        var metasJpa = repository.findAll();
        return metasJpa.stream()
                .map(c -> mapper.map(c, Meta.class))
                .toList();
    }
}
