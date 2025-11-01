package persistencia.jpa.cartao;

import cartao.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import persistencia.jpa.Mapper;
import persistencia.jpa.fatura.FaturaRepositoryImpl;

import java.util.List;

@Repository
public class CartaoRepositoryImpl implements CartaoRepositorio {

    @Autowired
    private CartaoJpaRepository repository;

    @Autowired
    private FaturaRepositoryImpl faturaRepository;

    @Autowired
    private Mapper mapper;

    @Override
    public void salvar(Cartao cartao) {
        var cartaoJpa = mapper.map(cartao,CartaoJpa.class);
        repository.save(cartaoJpa);
    }

    @Override
    public Cartao obterCartao(CartaoNumero numero) {
        var cartaoJpa = repository.findByNumero(numero.getCodigo());

        if(cartaoJpa == null){
            return null;
        }

        var fatura = faturaRepository.obterFatura((CartaoJpa) cartaoJpa);



        var cartao = mapper.map(cartaoJpa,Cartao.class);
        cartao.setFatura(fatura);
        return cartao;
    }

    @Override
    public Cartao obterCartaoPorId(CartaoId cartaoId) {
        var cartaoJpa = repository.findById(cartaoId.getId()).orElse(null);

        if(cartaoJpa == null){
            return null;
        }

        var fatura = faturaRepository.obterFatura(cartaoJpa);



        var cartao = mapper.map(cartaoJpa,Cartao.class);
        cartao.setFatura(fatura);
        return cartao;
    }

    @Override
    public List<Cartao> obterTodos() {
        var cartoes = repository.findAll();

        if(cartoes == null){
            return null;
        }

        return mapper.map(cartoes, List.class);
    }

    @Override
    public void deletarCartao(CartaoId id) {
        repository.deleteById(id.getId());
    }
}
