package investimento;

import generics.Observer;
import historicoInvestimento.HistoricoInvestimento;
import historicoInvestimento.HistoricoInvestimentoRepositorio;
import taxaSelic.TaxaSelic;
import taxaSelic.TaxaSelicRepository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.apache.commons.lang3.Validate.notNull;

public class InvestimentoService implements Observer {
    // implementa a interface observador que irá se registrar como observador do evento de atualização de taxa selic 
    private final InvestimentoRepositorio investimentoRepositorio;
    private final HistoricoInvestimentoRepositorio historicoInvestimentoRepositorio;
    private final TaxaSelicRepository taxaSelicRepository;

    public InvestimentoService(InvestimentoRepositorio investimentoRepositorio, TaxaSelicRepository taxaSelicRepository, HistoricoInvestimentoRepositorio historicoInvestimentoRepositorio) {
        this.investimentoRepositorio = investimentoRepositorio;
        this.historicoInvestimentoRepositorio = historicoInvestimentoRepositorio;
        this.taxaSelicRepository = taxaSelicRepository;
    }

    public void salvar (Investimento investimento){
        notNull(investimento, "Invesimento não pode ser nulo");
        investimentoRepositorio.salvar(investimento);
    }
    public Investimento obterInvestimento(String investimentoId){
        notNull(investimentoId, "Investimento não pode ser nulo");
        return investimentoRepositorio.obterInvestimento(investimentoId);
    }

    public void atualizar(String investimentoId, Investimento investimento){
        notNull(investimentoId, "O id não pode ser nulo");
        notNull(investimento, "O investimento não pode ser nulo");
        investimentoRepositorio.atualizarInvestimento(investimentoId, investimento);
    }

    public List<Investimento> obterTodos(){
        return investimentoRepositorio.obterTodosInvestimentos();
    }

    public List<Investimento> obterTodosPorUsuarioId(String id){
        return investimentoRepositorio.obterTodosInvestimentosPorUsuarioId(id);
    }

    public void deletarInvestimento(String investimentoId){
        notNull(investimentoId, "O id não pode ser nulo");
        investimentoRepositorio.deletarInvestimento(investimentoId);
    }

    public void limparInvestimento(){
        investimentoRepositorio.limparInvestimento();
    }

    public void atualizarRendimento (Investimento investimento){
        TaxaSelic taxaSelic = taxaSelicRepository.obterTaxaSelic();

        if (taxaSelic == null) {
            throw new RuntimeException("Taxa Selic não disponível.");
        }

        investimento.atualizarValor(taxaSelic.getValor());

        HistoricoInvestimento historicoInvestimento = new HistoricoInvestimento(
                investimento.getId(),
                investimento.getValorAtual(),
                LocalDate.now()
        );

        historicoInvestimentoRepositorio.salvar(historicoInvestimento);
    }

    public void resgateTotal(String investimentoId){

        historicoInvestimentoRepositorio.deletarTodosHistoricosPorId(investimentoId);

        investimentoRepositorio.deletarInvestimento(investimentoId);

    }

    public void resgateParcial(String investimentoId, BigDecimal valor){
        Investimento investimento = investimentoRepositorio.obterInvestimento(investimentoId);

        if(investimento.getValorAtual().compareTo(valor) <= 0 || valor.doubleValue() <= 0){
            throw new RuntimeException("Tentativa de resgate total em resgate parcial ou valor inválido.");
        }

        investimento.resgatarValor(valor);

        investimentoRepositorio.salvar(investimento);

        historicoInvestimentoRepositorio.salvar(new HistoricoInvestimento(investimentoId, investimento.getValorAtual(), LocalDate.now()));
    }

    @Override
    public void update() {
        List<Investimento> investimentos = obterTodos();

        for (Investimento investimento : investimentos){
            atualizarRendimento(investimento);
        }
    }
}
