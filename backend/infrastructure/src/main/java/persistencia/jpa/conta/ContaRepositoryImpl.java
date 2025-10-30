package persistencia.jpa.conta;

import conta.Conta;
import conta.ContaId;
import conta.ContaRepositorio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class ContaRepositoryImpl implements ContaRepositorio {

    @Autowired
    private ContaJpaRepository repositorio;

    @Override
    public void salvar(Conta conta) {
        repositorio.save(conta);
    }

    @Override
    public Optional<Conta> obterConta(String contaId) {
        return repositorio.findById(new ContaId(contaId));
    }

    @Override
    public boolean contaExistente(String nome) {
        return repositorio.existsById(new ContaId(nome));
    }

    @Override
    public List<Conta> listarTodasContas() {
        return repositorio.findAll();
    }

    @Override
    public void limparConta() {
        repositorio.deleteAll();
    }
}
