package persistencia.jpa.cartao;

import cartao.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import persistencia.jpa.Mapper;

import java.util.List;

@Repository
public class CartaoRepositoryImpl implements CartaoRepositorio {

    @Autowired
    private CartaoJpaRepository repository;

    @Autowired
    private Mapper mapper;

    @Override
    public void salvar(Cartao cartao) {
        var cartaoJpa = mapper.map(cartao,CartaoJpa.class);
        repository.save(cartaoJpa);
        //repository.save(cartao.getFatura());
    }

    @Override
    public Cartao obterCartao(CartaoNumero numero) {
        var cartaoJpa = repository.findByNumero(numero);
        //var fatura = repository.obterFatura(cartaoJpa);
        var cartao = mapper.map(cartaoJpa,Cartao.class);
        //cartao.setFatura(fatura);
        return cartao;
    }

    @Override
    public Cartao obterCartaoPorId(CartaoId cartaoId) {
        var cartaoJpa = repository.findById(cartaoId.getId()).orElse(null);
        //var fatura = repository.obterFatura(cartaoJpa);
        var cartao = mapper.map(cartaoJpa,Cartao.class);
        //cartao.setFatura(fatura);
        return cartao;
    }

    @Override
    public List<Cartao> obterTodos() {
        var cartoes = repository.findAll();
        return mapper.map(cartoes, List.class);
    }

    /*public void salvarFatura(Fatura fatura) {
        repository.save(fatura);
    }

    public Fatura obterFatura(CartaoJpa cartao) {
        var cartaoJpa = mapper.map(cartao,CartaoJpa.class);
        return mapper.map(cartaoJpa,Fatura.class);
    }*/
}
