package historicoInvestimento;

import investimento.Investimento;

import java.sql.Array;
import java.util.*;

public interface HistoricoInvestimentoRepositorio {

    void salvar(HistoricoInvestimento historicoInvestimento);

    List<HistoricoInvestimento> obterTodosHistoricos();

    void deletarTodosHistoricosPorId(String investimentoId);

    void setStatus(boolean status);
}
