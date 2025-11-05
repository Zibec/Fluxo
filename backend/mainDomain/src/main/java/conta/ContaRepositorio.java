package conta;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.List;
import java.util.ArrayList;
import static org.apache.commons.lang3.Validate.notNull;

public interface ContaRepositorio {

    void salvar(Conta conta);

    Optional<Conta> obterConta(String contaId);

    boolean contaExistente(String nome);

    List<Conta> listarTodasContas();

    void limparConta();

    void deletarConta(String id);

    List<Conta> obterContaPorUsuarioId(String id);
}