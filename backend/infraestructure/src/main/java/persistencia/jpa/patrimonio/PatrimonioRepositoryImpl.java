package persistencia.jpa.patrimonio;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import patrimonio.Patrimonio;
import patrimonio.PatrimonioRepositorio;
import persistencia.jpa.Mapper;

import java.util.List;

@Repository
public class PatrimonioRepositoryImpl implements PatrimonioRepositorio {

    @Autowired
    private PatrimonioJpaRepository repository;

    @Autowired
    private Mapper mapper;

    @Override
    public void salvarPatrimonio(Patrimonio patrimonio) {
        var patrimonioJpa = mapper.map(patrimonio, PatrimonioJpa.class);
        repository.save(patrimonioJpa);
    }

    @Override
    public List<Patrimonio> obterTodosPatrimonios() {
        var patrimonioJpa = repository.findAll();
        return mapper.map(patrimonioJpa, List.class);
    }

    @Override
    public void limparPatrimonio() {
        repository.deleteAll();
    }
}
