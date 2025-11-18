package persistencia.jpa.investimento;

import investimento.Investimento;
import investimento.InvestimentoRepositorio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import persistencia.jpa.Mapper;
import transacao.Transacao;

import java.util.ArrayList;
import java.util.List;

@Repository
public class InvestimentoRepositoryImpl implements InvestimentoRepositorio {
    @Autowired
    private InvestimentoJpaRepository repositorio;

    @Autowired
    private Mapper mapper;

    @Override
    public void salvar(Investimento investimento) {
        var investimentoJpa = mapper.map(investimento, InvestimentoJpa.class);
        repositorio.save(investimentoJpa);
    }

    @Override
    public Investimento obterInvestimento(String investimentoId) {
        var investimentoJpa = repositorio.findById(investimentoId);
        return mapper.map(investimentoJpa, Investimento.class);
    }

    @Override
    public List<Investimento> obterTodosInvestimentos() {
        var investimentoJpa = repositorio.findAll();


        return investimentoJpa.stream()
                .map(jpa -> mapper.map(jpa, Investimento.class))
                .toList();
    }

    public List<Investimento> obterTodosInvestimentosPorUsuarioId(String id) {
        var investimentoJpa = repositorio.findAllByUsuarioId(id);

        return investimentoJpa.stream()
                .map(jpa -> mapper.map(jpa, Investimento.class))
                .toList();
    }

    @Override
    public void atualizarInvestimento(String investimentoId, Investimento investimento) {
        repositorio.deleteById(investimentoId);

        var investimentoJpa = mapper.map(investimento, InvestimentoJpa.class);
        repositorio.save(investimentoJpa);
    }

    @Override
    public void deletarInvestimento(String investimentoId) {
        repositorio.deleteById(investimentoId);
    }

    @Override
    public void limparInvestimento() {
        repositorio.deleteAll();
    }
}
