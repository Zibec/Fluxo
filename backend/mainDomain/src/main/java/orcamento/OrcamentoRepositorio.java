package orcamento;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static org.apache.commons.lang3.Validate.notNull;

public class OrcamentoRepositorio {

    private Map<OrcamentoChave, Orcamento> orcamentos = new HashMap<>();

    public void salvarNovo(OrcamentoChave chave, Orcamento orcamento) {
        notNull(chave, "A achave não pode ser nula");
        notNull(orcamento, "O orcamento não pode ser nulo");

        if(orcamentos.putIfAbsent(chave,orcamento) != null){
            throw new IllegalStateException("Já existe um orçamento para essa chave");
        }
    }

    public void atualizarOrcamento(OrcamentoChave chave, Orcamento orcamento) {
        notNull(chave, "A achave não pode ser nula");
        notNull(orcamento, "O orcamento não pode ser nulo");

        if(orcamentos.replace(chave,orcamento) == null){
            throw new IllegalStateException("Não existe um orçamento para essa chave");
        }
    }

    public Optional<Orcamento> obterOrcamento(OrcamentoChave chave) {
        notNull(chave, "A chave do orcamento não pode ser nula");
        return Optional.ofNullable((orcamentos.get(chave)));
    }
}
