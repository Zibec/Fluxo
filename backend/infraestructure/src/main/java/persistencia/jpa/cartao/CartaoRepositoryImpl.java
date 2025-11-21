package persistencia.jpa.cartao;

import cartao.*;
import conta.Conta;
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
        var cartaoJpa = (CartaoJpa) repository.findByNumero(numero.getCodigo());

        if(cartaoJpa == null){
            return null;
        }

        var fatura = faturaRepository.obterFatura(cartaoJpa.id);

        var cartao = mapper.map(cartaoJpa,Cartao.class);
        cartao.setFatura(fatura);
        fatura.setCartao(cartao);
        return cartao;
    }

    @Override
    public Cartao obterCartaoPorId(String cartaoId) {
        var cartaoJpa = (CartaoJpa) repository.findById(cartaoId).orElse(null);

        if(cartaoJpa == null){
            return null;
        }

        var fatura = faturaRepository.obterFatura(cartaoJpa.id);

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

        return cartoes.stream()
                .map(jpa -> mapper.map(jpa, Cartao.class))
                .toList();
    }

    @Override
    public List<Cartao> obterCartaoPorUsarioId(String id) {
        var cartoes = repository.findAllByUsuarioId(id);

        if(cartoes == null){
            return null;
        }

        return cartoes.stream()
                .map(jpa -> mapper.map(jpa, Cartao.class))
                .toList();
    }

    @Override
    public void deletarCartao(String id) {
        repository.deleteById(id);
    }
}
