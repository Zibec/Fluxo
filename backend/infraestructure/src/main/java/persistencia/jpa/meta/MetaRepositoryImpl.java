package persistencia.jpa.meta;

import meta.Meta;
import meta.MetaRepositorio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.web.client.HttpClientErrorException;
import persistencia.jpa.Mapper;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Repository
public class MetaRepositoryImpl implements MetaRepositorio {

    @Autowired
    private MetaJpaRepository repository;

    @Autowired
    private Mapper mapper;

    @Override
    public Optional<Meta> obterMeta(String metaId) {
        System.out.println(metaId);
        MetaJpa metaJpa = repository.findById(metaId).get();
        System.out.printf(String.valueOf(metaJpa.saldoAcumulado));
        return Optional.of(mapper.map(metaJpa, Meta.class));
    }

    @Override
    public Optional<Meta> obterMetaPorNome(String nome) {
        return Optional.ofNullable(repository.findByDescricao(nome))
                .map(jpa -> mapper.map(jpa, Meta.class));
    }
    @Override
    public void salvar(Meta meta) {
        if(repository.findById(meta.getId()).isPresent()) {
            deletarMeta(meta.getId());
        }

        repository.save(mapper.map(meta, MetaJpa.class));
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

    @Override
    public List<Meta> obterMetaPorUsuario(String usuario) {
        var jpa = repository.findAllByUsuarioId(usuario);
        return mapper.map(jpa, List.class);
    }
}
