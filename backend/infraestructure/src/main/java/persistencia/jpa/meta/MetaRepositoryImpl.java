package persistencia.jpa.meta;

import meta.Meta;
import meta.MetaRepositorio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
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
        MetaJpa jpa = repository.findById(meta.getId())
                .orElseThrow(() -> new IllegalArgumentException("Meta não encontrada: " + meta.getId()));

        // Atualiza os campos da JPA a partir do domínio, sem criar um novo objeto
        mapper.map(meta, jpa);

        // Agora garante que o saldo seja realmente acumulado
        BigDecimal saldoAtual = jpa.getSaldoAcumulado() != null ? jpa.getSaldoAcumulado() : BigDecimal.ZERO;
        BigDecimal saldoNovo = saldoAtual.add(meta.getSaldoAcumulado());
        jpa.setSaldoAcumulado(saldoNovo);

        repository.save(jpa);
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
