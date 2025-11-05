package conta;

import java.util.List;
import java.util.Optional;
import static org.apache.commons.lang3.Validate.notNull;

public class ContaService {

    private final ContaRepositorio contaRepositorio;

    public ContaService(ContaRepositorio repositorio) {
        this.contaRepositorio = repositorio;
    }

    public void salvar(Conta conta) {
        notNull(conta, "A conta não pode ser nula");
        contaRepositorio.salvar(conta);
    }

    public Optional<Conta> obter(String contaId) {
        notNull(contaId, "O ID da conta não pode ser nulo");
        return contaRepositorio.obterConta(contaId);
    }

    public List<Conta> obterPorUsuarioId(String id) {
        notNull(id, "O ID do usuário não pode ser nulo");
        return contaRepositorio.obterContaPorUsuarioId(id);
    }

    public boolean contaExistente(String nome) {
        notNull(nome, "O nome da conta não pode ser nulo");
        return contaRepositorio.contaExistente(nome);
    }

    public List<Conta> listarTodasContas(){
        return contaRepositorio.listarTodasContas();
    }

    public void limparConta() {
        contaRepositorio.limparConta();
    }

    public void deletar(String id) {
        contaRepositorio.deletarConta(id);
    }
}