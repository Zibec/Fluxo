package persistencia.jpa.historicoInvestimento;

import divida.Divida;
import divida.DividaRepositorio;
import historicoInvestimento.HistoricoInvestimento;
import historicoInvestimento.HistoricoInvestimentoRepositorio;
import investimento.Investimento;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import persistencia.jpa.Mapper;
import persistencia.jpa.divida.DividaJpa;

import java.util.List;

@Repository
public class HistoricoInvestimentoRepositoryImpl implements HistoricoInvestimentoRepositorio {
    @Autowired
    private HistoricoInvestimentoJpaRepository repositorio;

    @Autowired
    private Mapper mapper;

    @Override
    public void salvar(HistoricoInvestimento historicoInvestimento) {
        var historicoJpa = mapper.map(historicoInvestimento, HistoricoInvestimentoJpa.class);
        repositorio.save(historicoJpa);
    }

    @Override
    public List<HistoricoInvestimento> obterTodosHistoricos() {
        var lista = repositorio.findAll();
        return mapper.map(lista, List.class);
    }

    @Override
    public List<HistoricoInvestimento> obterTodosHistoricosPorInvestimento(String investimentoId) {
        var lista = repositorio.findAllByInvestimentoId(investimentoId);
        return mapper.map(lista, List.class);
    }

    @Override
    public void deletarTodosHistoricosPorId(String investimentoId) {
        repositorio.deleteAllByInvestimentoId(investimentoId);
    }

    @Override
    public void setStatus(boolean status) {
        //
    }


}
