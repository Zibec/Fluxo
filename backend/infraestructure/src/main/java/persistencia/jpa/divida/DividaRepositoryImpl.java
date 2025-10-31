package persistencia.jpa.divida;

import divida.Divida;
import divida.DividaRepositorio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class DividaRepositoryImpl implements DividaRepositorio {
    @Autowired
    private DividaJpaRepository repositorio;

    @Override
    public void salvar(Divida divida) {

    }

    @Override
    public List<Divida> obterTodosDivida() {
        return List.of();
    }

    @Override
    public void limparDivida() {

    }
}
