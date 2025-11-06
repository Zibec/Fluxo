package meta;

import conta.Conta;
import conta.ContaRepositorio;
import jakarta.transaction.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.apache.commons.lang3.Validate.notNull;

public class MetaService {
    private final MetaRepositorio metaRepositorio;
    private final ContaRepositorio contaRepositorio; // Agora precisamos do repositório de conta

    public MetaService(MetaRepositorio metaRepositorio, ContaRepositorio contaRepositorio) {
        this.metaRepositorio = metaRepositorio;
        this.contaRepositorio = contaRepositorio;
    }

    @Transactional
    public void realizarAporte(String metaId, BigDecimal valorDoAporte, String contaId) {
        notNull(metaId, "O ID da meta não pode ser nulo");
        notNull(valorDoAporte, "O valor do aporte não pode ser nulo");
        notNull(contaId, "O ID da conta é obrigatório");

        Meta meta = metaRepositorio.obterMeta(metaId)
                .orElseThrow(() -> new IllegalArgumentException("Meta não encontrada com o ID: " + metaId));

        Conta contaPrincipal = contaRepositorio.obterConta(contaId)
                .orElseThrow(() -> new IllegalArgumentException("Conta não encontrada com o ID: " + contaId));

        if (!contaPrincipal.temSaldoSuficiente(valorDoAporte)) {
            throw new IllegalArgumentException("Saldo insuficiente na conta principal");
        }

        contaPrincipal.realizarTransacao(valorDoAporte);

        System.out.println("MetaService, Valor do aporte; " + valorDoAporte);

        meta.realizarAporte(valorDoAporte);
        metaRepositorio.salvar(meta);
        contaRepositorio.salvar(contaPrincipal);
    }

    public void salvar(Meta meta) {
        notNull(meta, "O cartão não pode ser nulo");
        metaRepositorio.salvar(meta);
    }

    public Optional<Meta> obter(String id) {
        notNull(id, "O número do cartão não pode ser nulo");

        Meta meta = metaRepositorio.obterMeta(id).get();

        System.out.println("Aqui: " + meta.getSaldoAcumulado());

        return Optional.of(meta);

    }

    public Optional<Meta> obterPorNome(String nomeMeta) {
        notNull(nomeMeta, "O nome da meta não pode ser nulo");
        return metaRepositorio.obterMetaPorNome(nomeMeta);
    }

    public void deletar(String metaId) {
        notNull(metaId, "O ID da meta não pode ser nulo");

        if (metaRepositorio.obterMeta(metaId).isEmpty()) {
            throw new IllegalArgumentException("Meta não encontrada com o ID: " + metaId);
        }

        metaRepositorio.deletarMeta(metaId);
    }

    public List<Meta> listar() {
        return metaRepositorio.listar();
    }

}
