package persistencia.jpa.fatura;

import cartao.Fatura;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import persistencia.jpa.Mapper;
import persistencia.jpa.cartao.CartaoJpa;

@Repository
public class FaturaRepositoryImpl {

    @Autowired
    private FaturaJpaRepository repository;

    @Autowired
    private Mapper mapper;

     public void salvarFatura(Fatura fatura) {
        var faturaJpa =  mapper.map(fatura, FaturaJpa.class);
        repository.save(faturaJpa);
    }

    public Fatura obterFatura(CartaoJpa cartao) {
        var faturaJpa = repository.findById(cartao.id);
        return mapper.map(faturaJpa, Fatura.class);
    }
}
