package persistencia.jpa.metaInversa;

import metaInversa.MetaInversa;
import metaInversa.MetaInversaRepositorio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import persistencia.jpa.Mapper;

import java.util.Optional;

@Repository
public class MetaInversaRepositoryImpl implements MetaInversaRepositorio {

    @Autowired
    private MetaInversaJpaRepository repository;

    @Autowired
    private Mapper mapper;

    @Override
    public void salvarMetaInversa(MetaInversa metaInversa) {
        var metaInversaJpa = mapper.map(metaInversa, MetaInversaJpa.class);
        repository.save(metaInversaJpa);
    }

    @Override
    public Optional<MetaInversa> obterMetaInversa(String metaId) {
        var metaInversaJpa = repository.findById(metaId);
        return Optional.of(mapper.map(metaInversaJpa, MetaInversa.class));
    }

    @Override
    public Optional<MetaInversa> obterMetaInversaPorNome(String nomeMeta) {
        var metaInversaJpa = repository.findByNome(nomeMeta);
        return Optional.of(mapper.map(metaInversaJpa, MetaInversa.class));
    }

    @Override
    public void deletarMetaInversa(String metaId) {
        repository.deleteById(metaId);
    }

    @Override
    public void limparMetaInversa() {
        repository.deleteAll();
    }
}
