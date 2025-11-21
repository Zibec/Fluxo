package persistencia.jpa.divida;

import divida.Divida;
import divida.DividaRepositorio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.stream.Collectors;

@Repository
public class DividaRepositoryImpl implements DividaRepositorio {

    @Autowired
    private DividaJpaRepository repositorio;

    @Override
    public void salvar(Divida divida) {
        DividaJpa entity = new DividaJpa();
        entity.id = divida.getId();
        entity.nome = divida.getNome();
        entity.valorDevedor = divida.getValorDevedor();
        entity.usuarioId = divida.getUsuarioId();

        repositorio.save(entity);
    }

    @Override
    public List<Divida> obterTodosDivida() {
        return repositorio.findAll().stream()
                .map(this::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public void limparDivida() {
        repositorio.deleteAll();
    }

    @Override
    public List<Divida> obterDividaPorUsuarioId(String usuarioId) {
        return repositorio.findAll().stream()
                .map(this::toDomain)
                .filter(d -> d.getUsuarioId() != null && d.getUsuarioId().equals(usuarioId))
                .collect(Collectors.toList());
    }

    private Divida toDomain(DividaJpa entity) {
        Divida divida = new Divida(
                entity.id,
                entity.nome,
                entity.valorDevedor
        );

        divida.setUsuarioId(entity.usuarioId);

        return divida;
    }
}