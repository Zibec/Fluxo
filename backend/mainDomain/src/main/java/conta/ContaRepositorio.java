package conta;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.List;
import java.util.ArrayList;
import static org.apache.commons.lang3.Validate.notNull;

public class ContaRepositorio {

    private final Map<String, Conta> contas = new HashMap<>();

    public void salvar(Conta conta) {
        notNull(conta, "A conta não pode ser nula");
        contas.put(conta.getId(), conta);
    }

    public Optional<Conta> obter(String contaId) {
        notNull(contaId, "O ID da conta não pode ser nulo");
        return Optional.ofNullable(contas.get(contaId));
    }

    public boolean contaExistente(String nome) {
        notNull(nome, "O nome da conta não pode ser nulo");
        return contas.values().stream().anyMatch(c -> c.getNome().equals(nome));
    }

    public List<Conta> listarTodas() {
        return new ArrayList<>(contas.values());
    }

    public void limpar() {
        contas.clear();
    }
}