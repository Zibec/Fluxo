package meta;

import conta.Conta;
import conta.ContaRepositorio;

import java.math.BigDecimal;
import java.util.Optional;

import static org.apache.commons.lang3.Validate.notNull;

public class MetaService {
    private final MetaRepositorio metaRepositorio;
    private final ContaRepositorio contaRepositorio; // Agora precisamos do repositório de conta

    public MetaService(MetaRepositorio metaRepositorio, ContaRepositorio contaRepositorio) {
        this.metaRepositorio = metaRepositorio;
        this.contaRepositorio = contaRepositorio;
    }

    public void realizarAporte(String metaId, BigDecimal valorDoAporte, Conta contaPrincipal) {
        notNull(metaId, "O ID da meta não pode ser nulo");
        notNull(valorDoAporte, "O valor do aporte não pode ser nulo");
        notNull(contaPrincipal, "A conta principal não pode ser nula");

        Meta meta = metaRepositorio.obter(metaId)
                .orElseThrow(() -> new IllegalArgumentException("Meta não encontrada com o ID: " + metaId));

        if (!contaPrincipal.temSaldoSuficiente(valorDoAporte)) {
            throw new IllegalArgumentException("Saldo insuficiente na conta principal");
        }

        contaPrincipal.realizarTransacao(valorDoAporte);
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
        return metaRepositorio.obter(id);
    }

    public Optional<Meta> obterPorNome(String nomeMeta) {
        notNull(nomeMeta, "O nome da meta não pode ser nulo");
        return metaRepositorio.obterPorNome(nomeMeta);
    }

    public void deletar(String metaId) {
        notNull(metaId, "O ID da meta não pode ser nulo");

        if (metaRepositorio.obter(metaId).isEmpty()) {
            throw new IllegalArgumentException("Meta não encontrada com o ID: " + metaId);
        }

        metaRepositorio.deletar(metaId);
    }
}
