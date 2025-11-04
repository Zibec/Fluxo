package persistencia.jpa.orcamento;

import meta.MetaRepositorio;
import orcamento.Orcamento;
import orcamento.OrcamentoChave;
import orcamento.OrcamentoRepositorio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import persistencia.jpa.Mapper;

import java.time.YearMonth;
import java.util.List;
import java.util.Optional;

@Repository
public class OrcamentoRepositoryImpl implements OrcamentoRepositorio {
    @Autowired
    private OrcamentoJpaRepository repositorio;

    @Autowired
    private Mapper mapper;

    @Override
    public void salvarOrcamento(OrcamentoChave chave, Orcamento orcamento) {
        if (orcamento.getOrcamentoChave() == null) {
            orcamento.setOrcamentoChave(chave);
        }

        String id = chaveToId(chave);

        OrcamentoJpa jpa = new OrcamentoJpa();
        jpa.chave = id;
        jpa.usuarioId = chave.getUsuarioId();
        jpa.categoriaId = chave.getCategoriaId();
        jpa.ano = chave.getAnoMes().getYear();
        jpa.mes = chave.getAnoMes().getMonthValue();
        jpa.limite = orcamento.getLimite();
        jpa.dataLimite = orcamento.getDataLimite();

        repositorio.save(jpa);
    }

    @Override
    public void atualizarOrcamento(OrcamentoChave chave, Orcamento orcamento) {
        String id = chaveToId(chave);

        OrcamentoJpa jpa = repositorio.findById(id).orElseGet(() -> {
            OrcamentoJpa novo = new OrcamentoJpa();
            novo.chave = id;
            novo.usuarioId = chave.getUsuarioId();
            novo.categoriaId = chave.getCategoriaId();
            novo.ano = chave.getAnoMes().getYear();
            novo.mes = chave.getAnoMes().getMonthValue();
            return novo;
        });

        jpa.limite = orcamento.getLimite();
        jpa.dataLimite = orcamento.getDataLimite();

        repositorio.save(jpa);
    }

    @Override
    public List<Orcamento> listarTodos(){
        return repositorio.findAll().stream().map(this::toDomain).toList();
    }

    @Override
    public Optional<Orcamento> obterOrcamento(OrcamentoChave chave) {
        String id = chaveToId(chave);
        return repositorio.findById(id).map(j -> {
            // Reconstroi a chave de domínio direto das colunas sombreadas
            var ym = YearMonth.of(j.ano, j.mes);
            var chaveDomain = new OrcamentoChave(j.usuarioId, ym, j.categoriaId);
            return new Orcamento(chaveDomain, j.limite, j.dataLimite);
        });
    }

    @Override
    public void limparOrcamento() {
        repositorio.deleteAll();
    }

// Helpers
    private String chaveToId(OrcamentoChave c) {
        // YearMonth.toString() já é "YYYY-MM"
        return c.getUsuarioId() + "|" + c.getCategoriaId() + "|" + c.getAnoMes();
    }

    private Orcamento toDomain(OrcamentoJpa j) {
        //Remonta o YearMonth a partir das colunas sombreadas do banco
        var ym = java.time.YearMonth.of(j.ano, j.mes);

        //Reconstrói a chave de domínio
        var chaveDomain = new OrcamentoChave(j.usuarioId, ym, j.categoriaId);

        //Cria o agregado de domínio Orcamento com os dados persistidos
        return new Orcamento(chaveDomain, j.limite, j.dataLimite);
    }
}
