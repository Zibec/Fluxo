package orcamento;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static org.apache.commons.lang3.Validate.notNull;

public interface OrcamentoRepositorio {

    void salvarOrcamento(OrcamentoChave chave, Orcamento orcamento);

    void atualizarOrcamento(OrcamentoChave chave, Orcamento orcamento);

    Optional<Orcamento> obterOrcamento(OrcamentoChave chave);

    void limparOrcamento();
}