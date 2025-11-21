package persistencia.jpa.transacao;

import metaInversa.MetaInversaRepositorio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import persistencia.jpa.Mapper;
import transacao.Transacao;
import transacao.TransacaoRepositorio;

import java.sql.Array;
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
        return repositorio.findByOrigemAgendamentoIdAndData(agendamentoId, data)
                .map(jpa -> mapper.map(jpa, Transacao.class));
    }

    @Override
    public boolean existeTransacaoPorCategoriaId(String categoriaId) {
        return  repositorio.existsById(categoriaId);
    }

    @Override
    public List<Transacao> listarTodasTransacoes() {
        var transacoesJpa = repositorio.findAll();

        return transacoesJpa.stream()
                .map(jpa -> mapper.map(jpa, Transacao.class))
                .toList();
    }

    @Override
    public List<Transacao> listarPorOrigemAgendamentoId(String agendamentoId) {
        var listaJpa = repositorio.findAllByOrigemAgendamentoId(agendamentoId);

        // opção 1: mapear "na mão"
        return listaJpa.stream()
                .map(jpa -> mapper.map(jpa, Transacao.class))
                .toList();
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
        if(repositorio.findById(id).isEmpty()){
            return Optional.empty();
        }

        return Optional.of(mapper.map(repositorio.findById(id), Transacao.class));
    }

    @Override
    public void limparTransacao() {
        repositorio.deleteAll();
    }

    @Override
    public List<Transacao> obterTransacaoPorConta(String usuarioId) {
        List<TransacaoJpa> jpa = repositorio.findAllByUsuarioId(usuarioId);

        if(jpa.isEmpty()) {
            return List.of();
        }

        return mapper.map(jpa, List.class);
    }
}
