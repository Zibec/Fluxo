package investimento;

import java.util.*;

import static org.apache.commons.lang3.Validate.notNull;

public interface InvestimentoRepositorio {

    //Refatorar para ser uma lista.

    void salvar(Investimento investimento);

    Investimento obterInvestimento(String investimentoId);

    ArrayList<Investimento> obterTodos();

    void atualizarInvestimento(String investimentoId, Investimento investimento);

    void deletarInvestimento(String investimentoId);

    void limparInvestimento();

}
