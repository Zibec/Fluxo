package transacao;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

import static org.apache.commons.lang3.Validate.notBlank;
import static org.apache.commons.lang3.Validate.notNull;

import cartao.Cartao;
import cartao.CartaoId;
import cartao.CartaoRepositorio;
import conta.Conta;
import conta.ContaId;
import conta.ContaRepositorio;
import perfil.Perfil;

public class TransacaoService {
    private final TransacaoRepositorio repo;
    private final ContaRepositorio contaRepo;
    private final CartaoRepositorio cartaoRepositorio;
    private final Map<String, String> idxAgendamentoData = new ConcurrentHashMap<>();
    private final Map<String, Transacao> transacao = new ConcurrentHashMap<>();


    public TransacaoService(TransacaoRepositorio repo, ContaRepositorio contaRepo, CartaoRepositorio cartaoRepositorio) {
        this.repo = Objects.requireNonNull(repo);
        this.contaRepo = Objects.requireNonNull(contaRepo);
        this.cartaoRepositorio = Objects.requireNonNull(cartaoRepositorio);
    }

    /**
     * Cria uma transação PENDENTE oriunda de agendamento.
     * Idempotente por (agendamentoId, data): se já existe, retorna a existente.
     */
    public Transacao criarPendenteDeAgendamento(String agendamentoId, String descricao, BigDecimal valor, LocalDate data, Conta conta, boolean avulsa, String perfilId) {
        System.out.println("Chegou em transacao aqui");
//        Optional<Transacao> existente = repo.encontrarTransacaoPorAgendamentoEData(agendamentoId, data);
//        if (existente.isPresent()) {
//            return existente.get(); // idempotência: não duplica
//        }

        Transacao t = new Transacao(
                UUID.randomUUID().toString(),
                agendamentoId,
                descricao,
                valor,
                data,
                StatusTransacao.PENDENTE,
                conta.getId(),
                avulsa,
                Tipo.DESPESA,
                perfilId
        );
        repo.salvarTransacao(t);
        return t;
    }

    public BigDecimal calcularGastosConsolidadosPorCategoria(String categoriaId, YearMonth mes) {
        notBlank(categoriaId, "O ID da categoria não pode ser vazio.");
        notNull(mes, "O mês não pode ser nulo.");

        List<Transacao> todasTransacoes = repo.listarTodasTransacoes();

        // 1) Soma todas as DESPESAS da categoria no mês
        BigDecimal totalDespesas = todasTransacoes.stream()
                .filter(t -> t.getTipo() == Tipo.DESPESA)
                .filter(t -> categoriaId.equals(t.getCategoriaId()))
                .filter(t -> YearMonth.from(t.getData()).equals(mes))
                .map(Transacao::getValor)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        // 2) Soma os REEMBOLSOS que corrigem DESPESAS dessa categoria e mês,
        //    mesmo que o reembolso tenha sido em outro mês
        BigDecimal totalReembolsos = todasTransacoes.stream()
                .filter(t -> t.getTipo() == Tipo.REEMBOLSO)
                .map(t -> {
                    // se não tiver transação original ligada, ignora
                    if (t.getTransacaoOriginalId() == null) {
                        return BigDecimal.ZERO;
                    }
                    return repo.buscarTransacaoPorId(t.getTransacaoOriginalId())
                            .filter(orig -> orig.getTipo() == Tipo.DESPESA)
                            .filter(orig -> categoriaId.equals(orig.getCategoriaId()))
                            .filter(orig -> YearMonth.from(orig.getData()).equals(mes))
                            // se a despesa original bate categoria + mês, esse reembolso entra
                            .map(orig -> t.getValor())
                            .orElse(BigDecimal.ZERO);
                })
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        // 3) Gasto líquido = despesas - reembolsos
        return totalDespesas.subtract(totalReembolsos);
    }

    public Transacao registrarReembolso(BigDecimal valorReembolso, String idDespesaOriginal) {
        if (valorReembolso == null) {
            throw new IllegalArgumentException("O valor do reembolso não pode ser nulo");
        }
        if (idDespesaOriginal == null) {
            throw new IllegalArgumentException("O ID da despesa original não pode ser nulo");
        }

        Transacao despesaOriginal = repo.obterTransacaoPorId(idDespesaOriginal)
                .orElseThrow(() -> new IllegalArgumentException("Despesa original não encontrada"));

        if (valorReembolso.compareTo(despesaOriginal.getValor()) > 0) {
            throw new IllegalArgumentException("O valor do reembolso não pode ser maior que o da despesa original");
        }

        String novoId = UUID.randomUUID().toString();
        String descricaoReembolso = "Reembolso de: " + despesaOriginal.getDescricao();

        Conta contaDaDespesaOriginal = contaRepo.obterConta(despesaOriginal.getPagamentoId().getId())
                .orElseThrow(() -> new IllegalArgumentException("Conta da despesa original não encontrada"));

        Transacao reembolso = new Transacao(
                novoId,
                null,
                descricaoReembolso,
                valorReembolso,
                LocalDate.now(),
                StatusTransacao.EFETIVADA,
                despesaOriginal.getCategoriaId(),
                contaDaDespesaOriginal.getId(),
                true,
                Tipo.REEMBOLSO,
                despesaOriginal.getPerfilId()
        );

        reembolso.setTransacaoOriginalId(idDespesaOriginal);
        repo.salvarTransacao(reembolso);
        return reembolso;
    }

    public void efetivarTransacao(String transacaoId) {
        Transacao t = repo.obterTransacaoPorId(transacaoId)
                .orElseThrow(() -> new IllegalArgumentException("Transação não encontrada"));

        t.efetivar();
        if (t.getPagamentoId() instanceof ContaId) {
            Conta conta = contaRepo.obterConta(t.getPagamentoId().getId())
                    .orElseThrow(() -> new IllegalArgumentException("Conta não encontrada"));
            conta.realizarTransacao(t.getValor());
            contaRepo.salvar(conta);
        } else {
            Cartao cartao = cartaoRepositorio.obterCartaoPorId((CartaoId) t.getPagamentoId());

            cartao.realizarTransacao(t.getValor());
            cartaoRepositorio.salvar(cartao);
        }

        repo.salvarTransacao(t);
    }

    public void salvarTransacao(Transacao t){
        if (t.getPerfilId() == null){
            throw new RuntimeException("É obrigatório a seleção de um perfil.");
        }
        transacao.put(t.getId(), t);
        if (t.getOrigemAgendamentoId() != null) {
            idxAgendamentoData.put(chave(t.getOrigemAgendamentoId(), t.getData()), t.getId());
        }
        repo.salvarTransacao(t);
    }

    public Optional<Transacao> encontrarTransacaoPorAgendamentoEData(String agendamentoId, LocalDate data){
        return repo.encontrarTransacaoPorAgendamentoEData(agendamentoId, data);
    }

    public boolean existeTransacaoPorCategoriaId(String categoriaId){
        Objects.requireNonNull(categoriaId, "ID da Categoria não pode ser nulo");
        return repo.existeTransacaoPorCategoriaId(categoriaId);
    }

    public List<Transacao> listarTodasTransacoes(){
        return repo.listarTodasTransacoes();
    }

    private static String chave(String agendamentoId, LocalDate data) {
        return agendamentoId + "#" + data;
    }

    Optional<Transacao> buscarTransacaoPorId(String id){
        Objects.requireNonNull(id, "ID da transação não pode ser nulo");
        return repo.buscarTransacaoPorId(id);
    }

    public void atualizarTransacao(Transacao t){
        repo.atualizarTransacao(t);
    }

    public void excluirTransacao(String id){
        Objects.requireNonNull(id, "O ID da transação não pode ser nulo");

        Transacao removida = transacao.remove(id);

        if (removida != null && removida.getOrigemAgendamentoId() != null) {
            idxAgendamentoData.remove(chave(removida.getOrigemAgendamentoId(), removida.getData()));
        }


        repo.excluirTransacao(id);
    }

    public void excluirPorOrigemAgendamento(String agendamentoId) {
        Objects.requireNonNull(agendamentoId, "Id do agendamento não pode ser nulo");

        List<Transacao> ligadas = repo.listarPorOrigemAgendamentoId(agendamentoId);

        for (Transacao t : ligadas) {

            if (t.getStatus() == StatusTransacao.PENDENTE) {
                excluirTransacao(t.getId());
            }

            excluirTransacao(t.getId());
        }
    }

    public Optional<Transacao> obterTransacaoPorId(String id){
        Objects.requireNonNull(id, "O ID da transação não pode ser nulo");
        return repo.obterTransacaoPorId(id);
    }

    public List<Transacao> obterPorConta(String usuarioId) {
        return repo.obterTransacaoPorConta(usuarioId);
    }
}
