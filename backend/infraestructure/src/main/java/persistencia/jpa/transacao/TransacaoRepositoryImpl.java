package persistencia.jpa.transacao;

import metaInversa.MetaInversaRepositorio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import persistencia.jpa.Mapper;
import transacao.Transacao;
import transacao.TransacaoRepositorio;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public class TransacaoRepositoryImpl implements TransacaoRepositorio {

    @Autowired
    private TransacaoJpaRepository repositorio;

    @Autowired
    private Mapper mapper;

    @Override
    public void salvarTransacao(Transacao t) {
        var transacaoJpa =  mapper.map(t, TransacaoJpa.class);
        repositorio.save(transacaoJpa);
    }

    @Override
    public Optional<Transacao> encontrarTransacaoPorAgendamentoEData(String agendamentoId, LocalDate data) {
        var transacaoJpa = repositorio.findByOrigemAgendamentoIdAndData(agendamentoId, data);
        return Optional.of(mapper.map(transacaoJpa, Transacao.class));
    }

    @Override
    public boolean existeTransacaoPorCategoriaId(String categoriaId) {
        return  repositorio.existsById(categoriaId);
    }

    @Override
    public List<Transacao> listarTodasTransacoes() {
        var transacaoJpa = repositorio.findAll();
        return mapper.map(transacaoJpa, List.class);
    }

    @Override
    public Optional<Transacao> buscarTransacaoPorId(String id) {
        var transacaoJpa = repositorio.findById(id);
        return Optional.of(mapper.map(transacaoJpa, Transacao.class));
    }

    @Override
    public void atualizarTransacao(Transacao t) {
        repositorio.deleteById(t.getId());
        var transacaoJpa = mapper.map(t, TransacaoJpa.class);
        transacaoJpa.id = t.getId();
        repositorio.save(transacaoJpa);
    }

    @Override
    public void excluirTransacao(String id) {
        repositorio.deleteById(id);
    }

    @Override
    public Optional<Transacao> obterTransacaoPorId(String id) {
        return Optional.of(mapper.map(repositorio.findById(id), Transacao.class));
    }

    @Override
    public void limparTransacao() {
        repositorio.deleteAll();
    }
}
