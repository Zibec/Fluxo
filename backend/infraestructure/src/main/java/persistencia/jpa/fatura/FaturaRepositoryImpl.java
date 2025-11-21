package persistencia.jpa.fatura;

import cartao.Cartao;
import cartao.Fatura;
import cartao.FaturaRepositorio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import persistencia.jpa.Mapper;
import persistencia.jpa.cartao.CartaoJpa;
import persistencia.jpa.cartao.CartaoRepositoryImpl;

@Repository
public class FaturaRepositoryImpl implements FaturaRepositorio {
    @Autowired
    private FaturaJpaRepository repository;

    @Autowired
    private Mapper mapper;

     public void salvarFatura(Fatura fatura) {
        var faturaJpa =  mapper.map(fatura, FaturaJpa.class);
        repository.save(faturaJpa);
    }

    public Fatura obterFatura(String id) {
        var faturaJpa = repository.findById(id);
        return mapper.map(faturaJpa, Fatura.class);
    }

    @Override
    public void deletarFatura(String id) {
        repository.deleteById(id);
    }
}
