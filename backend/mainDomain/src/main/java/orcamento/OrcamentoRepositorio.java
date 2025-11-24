package orcamento;

import usuario.Usuario;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.apache.commons.lang3.Validate.notNull;

public interface OrcamentoRepositorio {

    void salvarOrcamento(OrcamentoChave chave, Orcamento orcamento);

    void atualizarOrcamento(OrcamentoChave chave, Orcamento orcamento);

    Optional<Orcamento> obterOrcamento(OrcamentoChave chave);

    Optional<Orcamento> obterOrcamentoPorCategoria(String categoria);

    void limparOrcamento();

    List<Orcamento> listarTodos();

    List<Orcamento> listarTodosPorUsuario(String id);

}