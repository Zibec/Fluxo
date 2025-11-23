package metaInversa;

import static org.apache.commons.lang3.Validate.notNull;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import conta.Conta;
import conta.ContaRepositorio;
import meta.Meta;


public class MetaInversaService {

    private final MetaInversaRepositorio metaRepositorio;
    private final ContaRepositorio contaRepositorio; // Agora precisamos do repositório de conta

    public MetaInversaService(MetaInversaRepositorio metaRepositorio, ContaRepositorio contaRepositorio) {
        this.metaRepositorio = metaRepositorio;
        this.contaRepositorio = contaRepositorio;
    }

    public void realizarAporte(String metaId, BigDecimal valorDoAporte) {
        notNull(metaId, "O ID da meta não pode ser nulo");
        notNull(valorDoAporte, "Valor do aporte deve ser um número positivo.");

        MetaInversa meta = metaRepositorio.obterMetaInversa(metaId).orElseThrow(() -> new IllegalArgumentException("Meta não encontrada com o ID: " + metaId));

        Conta conta = contaRepositorio.obterConta(meta.getContaAssociadaId()).orElseThrow(() -> new IllegalArgumentException("Conta não encontrada com o ID: " + meta.getContaAssociadaId()));;

        if (!conta.temSaldoSuficiente(valorDoAporte)) {
            throw new IllegalArgumentException("Saldo insuficiente na conta principal");
        }

        conta.realizarTransacao(valorDoAporte);
        meta.realizarAporte(valorDoAporte);

        metaRepositorio.salvarMetaInversa(meta);
        contaRepositorio.salvar(conta);
    }

    public void salvar(MetaInversa meta) {
        metaRepositorio.salvarMetaInversa(meta);
    }

    public MetaInversa buscar(String id){
        return metaRepositorio.obterMetaInversa(id).orElse(null);
    }

    public MetaInversa buscarPorNome(String nomeMeta){
        return metaRepositorio.obterMetaInversaPorNome(nomeMeta).orElse(null);
    }

    public void deletar(String metaId){
        metaRepositorio.deletarMetaInversa(metaId);
    }

    public void limpar(){
        metaRepositorio.limparMetaInversa();
    }

    public Optional<MetaInversa> obterOptional(String id) {
        return metaRepositorio.obterMetaInversa(id);
    }

    public Optional<MetaInversa> buscarPorNomeOptional(String nome) {
        return metaRepositorio.obterMetaInversaPorNome(nome);
    }

    public List<MetaInversa> obterPorUsuario(String usuarioId) {
        List<MetaInversa> metasJpa = metaRepositorio.obterMetaInversaPorUsuario(usuarioId);

        List<MetaInversa> metas = metasJpa.stream()
        .map(jpa -> new MetaInversa(
            jpa.getId(),
            jpa.getNome(),
            jpa.getValorDivida(),
            jpa.getContaAssociadaId(),
            jpa.getDataLimite(),
            jpa.getValorAmortizado(),
            jpa.getStatus()
        ))
        .toList();

        ColecaoMetaInversa colecao = new ColecaoMetaInversa(metas);

        List<MetaInversa> resultado = new ArrayList<>();

        for (MetaInversa m : colecao) {
            resultado.add(m);
        }

        return resultado;
    }

    public ColecaoMetaInversa listarColecaoPorUsuario(String usuarioId) {
        var metas = metaRepositorio.obterMetaInversaPorUsuario(usuarioId);
        return new ColecaoMetaInversa(metas);
    }

}
