package persistencia.jpa.orcamento;

import meta.MetaRepositorio;
import orcamento.Orcamento;
import orcamento.OrcamentoChave;
import orcamento.OrcamentoRepositorio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import persistencia.jpa.Mapper;

import java.util.Optional;

@Repository
public class OrcamentoRepositoryImpl implements OrcamentoRepositorio {
    @Autowired
    private OrcamentoJpaRepository repositorio;

    @Autowired
    private Mapper mapper;

    @Override
    public void salvarOrcamento(OrcamentoChave chave, Orcamento orcamento) {
        var orcamentoJpa = mapper.map(orcamento, OrcamentoJpa.class);
        String chaveJpa = mapper.map(chave, String.class);
        orcamentoJpa.chave = chaveJpa;
        repositorio.save(orcamentoJpa);
    }

    @Override
    public void atualizarOrcamento(OrcamentoChave chave, Orcamento orcamento) {
        repositorio.deleteById(chave);
        var orcamentoJpa = mapper.map(orcamento, OrcamentoJpa.class);
        String chaveJpa = mapper.map(chave, String.class);
        orcamentoJpa.chave = chaveJpa;
        repositorio.save(orcamentoJpa);
    }

    @Override
    public Optional<Orcamento> obterOrcamento(OrcamentoChave chave) {
        var orcamentoJpa = repositorio.findById(chave);
        return Optional.of(mapper.map(orcamentoJpa, Orcamento.class));
    }

    @Override
    public void limparOrcamento() {
        repositorio.deleteAll();
    }
}
