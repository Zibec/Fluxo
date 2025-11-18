package persistencia.jpa.conta;

import conta.Conta;
import conta.ContaId;
import conta.ContaRepositorio;
import investimento.Investimento;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import persistencia.jpa.Mapper;

import java.util.List;
import java.util.Optional;

@Repository
public class ContaRepositoryImpl implements ContaRepositorio {

    @Autowired
    private ContaJpaRepository repositorio;

    @Autowired
    private Mapper mapper;

    @Override
    public void salvar(Conta conta) {
        var contaJpa = mapper.map(conta, ContaJpa.class);
        repositorio.save(contaJpa);
    }

    @Override
    public Optional<Conta> obterConta(String contaId) {
        var conta = repositorio.findById(contaId);

        if(conta.isEmpty()){
            return Optional.empty();
        }

        return Optional.of(mapper.map(conta, Conta.class));
    }

    @Override
    public boolean contaExistente(String nome) {
        return repositorio.existsById(nome);
    }

    @Override
    public List<Conta> listarTodasContas() {
        var contasJpa = repositorio.findAll();

        if (contasJpa.isEmpty()) {
            return List.of();
        }

        return contasJpa.stream()
                .map(jpa -> mapper.map(jpa, Conta.class))
                .toList();
    }

    @Override
    public void limparConta() {
        repositorio.deleteAll();
    }

    @Override
    public void deletarConta(String id) {
        repositorio.deleteById(id);
    }

    @Override
    public List<Conta> obterContaPorUsuarioId(String id) {
        var contasJpa = repositorio.findAllByUsuarioId(id);

        if (contasJpa.isEmpty()) {
            return List.of();
        }

        return contasJpa.stream()
                .map(jpa -> mapper.map(jpa, Conta.class))
                .toList();
    }
}
