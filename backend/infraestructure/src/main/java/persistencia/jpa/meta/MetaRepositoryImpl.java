package persistencia.jpa.meta;

import meta.Meta;
import meta.MetaRepositorio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import persistencia.jpa.Mapper;

import java.util.Optional;

@Repository
public class MetaRepositoryImpl implements MetaRepositorio {

    @Autowired
    private MetaJpaRepository repository;

    @Autowired
    private Mapper mapper;

    @Override
    public void salvar(Meta meta) {
        var metaJpa = mapper.map(meta, MetaJpa.class);
        repository.save(metaJpa);
    }

    @Override
    public Optional<Meta> obterMeta(String metaId) {
        var metaJpa = repository.findById(metaId);
        return Optional.of( mapper.map(metaJpa, Meta.class));
    }

    @Override
    public Optional<Meta> obterMetaPorNome(String nomeMeta) {
        var metaJpa = repository.findByDescricao(nomeMeta);
        return Optional.of(mapper.map(metaJpa, Meta.class));
    }

    @Override
    public void deletarMeta(String metaId) {
        repository.deleteById(metaId);
    }
}
