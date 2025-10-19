package metaInversa;

import static org.apache.commons.lang3.Validate.notNull;

import java.math.BigDecimal;

import conta.Conta;
import conta.ContaRepositorio;


public class MetaInversaService {

    private final MetaInversaRepositorio metaRepositorio;
    private final ContaRepositorio contaRepositorio; // Agora precisamos do repositório de conta

    public MetaInversaService(MetaInversaRepositorio metaRepositorio, ContaRepositorio contaRepositorio) {
        this.metaRepositorio = metaRepositorio;
        this.contaRepositorio = contaRepositorio;
    }

    public void realizarAporte(String metaId, BigDecimal valorDoAporte) {
        notNull(metaId, "O ID da meta não pode ser nulo");
        notNull(valorDoAporte, "O valor do aporte não pode ser nulo");

        MetaInversa meta = metaRepositorio.obter(metaId).orElseThrow(() -> new IllegalArgumentException("Meta não encontrada com o ID: " + metaId));

        Conta conta = contaRepositorio.obter(meta.getContaAssociadaId()).orElseThrow(() -> new IllegalArgumentException("Conta não encontrada com o ID: " + meta.getContaAssociadaId()));;

        if (!conta.temSaldoSuficiente(valorDoAporte)) {
            throw new IllegalArgumentException("Saldo insuficiente na conta principal");
        }

        conta.realizarTransacao(valorDoAporte);
        meta.realizarAporte(valorDoAporte);

        metaRepositorio.salvar(meta);
        contaRepositorio.salvar(conta);
    }
}
