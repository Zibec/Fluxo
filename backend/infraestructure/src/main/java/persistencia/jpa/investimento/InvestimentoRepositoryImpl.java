package persistencia.jpa.investimento;

import investimento.Investimento;
import investimento.InvestimentoRepositorio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import persistencia.jpa.Mapper;

import java.util.ArrayList;

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
    public ArrayList<Investimento> obterTodosInvestimentos() {
        var investimentoJpa = repositorio.findAll();
        ArrayList<Investimento> investimentos = mapper.map(investimentoJpa, ArrayList.class);

        return investimentos;
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
