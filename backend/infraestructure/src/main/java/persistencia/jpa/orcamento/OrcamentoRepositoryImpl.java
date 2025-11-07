package persistencia.jpa.orcamento;

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

        // normaliza (evita espaços/tabs acidentais)
        var usuarioId = chave.getUsuarioId().trim();
        var categoriaId = chave.getCategoriaId().trim();
        var ano = chave.getAnoMes().getYear();
        var mes = chave.getAnoMes().getMonthValue();

        if (repositorio.existsByUsuarioIdAndCategoriaIdAndAnoAndMes(usuarioId, categoriaId, ano, mes)) {
            throw new IllegalStateException("Já existe um orçamento para essa chave");
        }

        var jpa = new OrcamentoJpa();
        jpa.chave = chaveToId(chave);
        jpa.usuarioId = usuarioId;
        jpa.categoriaId = categoriaId;
        jpa.ano = ano;
        jpa.mes = mes;
        jpa.limite = orcamento.getLimite();
        jpa.dataLimite = orcamento.getDataLimite();

        repositorio.save(jpa);
    }

    @Override
    public void atualizarOrcamento(OrcamentoChave chave, Orcamento orcamento) {
        var id = chaveToId(chave);
        if (!repositorio.existsById(id)) {
            throw new IllegalStateException("Não existe um orçamento para essa chave");
        }
        var jpa = repositorio.findById(id).orElseThrow();
        jpa.limite = orcamento.getLimite();
        jpa.dataLimite = orcamento.getDataLimite();
        repositorio.save(jpa);
    }

    @Override
    public List<Orcamento> listarTodos(){
        return repositorio.findAll().stream().map(this::toDomain).toList();
    }

    @Override
    public List<Orcamento> listarTodosPorUsuario(String id) {
        return mapper.map(repositorio.findAllByUsuarioId(id), List.class);
    }

    @Override
    public Optional<Orcamento> obterOrcamento(OrcamentoChave chave) {
        String id = chaveToId(chave);
        return repositorio.findById(id).map(j -> {
            var ym = YearMonth.of(j.ano, j.mes);
            var chaveDomain = new OrcamentoChave(j.usuarioId, ym, j.categoriaId);
            return new Orcamento(chaveDomain, j.limite, j.dataLimite);
        });
    }



    @Override
    public void limparOrcamento() {
        repositorio.deleteAll();
    }

//Helpers
    private String chaveToId(OrcamentoChave c) {
        return c.getUsuarioId() + "|" + c.getCategoriaId() + "|" + c.getAnoMes();
    }

    private Orcamento toDomain(OrcamentoJpa j) {
        var ym = java.time.YearMonth.of(j.ano, j.mes);

        //Reconstrói a chave de domínio
        var chaveDomain = new OrcamentoChave(j.usuarioId, ym, j.categoriaId);

        //Cria o agregado de domínio Orcamento com os dados persistidos
        return new Orcamento(chaveDomain, j.limite, j.dataLimite);
    }
}
