package agendamento;

import conta.Conta;
import transacao.Transacao;
import transacao.TransacaoService;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.*;

public class AgendamentoService {

    private final AgendamentoRepositorio agRepo;
    private final TransacaoService transacaoService;
    private final Set<String> execucoesDoDia = new HashSet<>(); // evita reexecução no mesmo dia

    public AgendamentoService(AgendamentoRepositorio agRepo, TransacaoService transacaoService) {
        this.agRepo = Objects.requireNonNull(agRepo);
        this.transacaoService = Objects.requireNonNull(transacaoService);
    }

    public void salvar(Agendamento agendamento) {
        agRepo.salvar(agendamento);
    }

    public void salvarComTransacao(Agendamento agendamento, Conta conta) {
        System.out.println("Passou por aqui");
        salvar(agendamento);
        if (agendamento.getProximaData() != null) {
            System.out.println("Passou por aqui kkkk ");
             transacaoService.criarPendenteDeAgendamento(
                    agendamento.getId(),
                    agendamento.getDescricao(),
                    agendamento.getValor(),
                    agendamento.getProximaData(),
                     agendamento.getCategoriaId(),
                    conta,
                    false,
                    agendamento.getPerfilId()
            );
        }
    }

    public void atualizarComTransacao(Agendamento agendamento, LocalDate hoje){
        agRepo.atualizarAgendamento(agendamento.getId(), agendamento.getValor());

        Transacao t = transacaoService.listarPorOrigemAgendamentoId(agendamento.getId());

        if (agendamento.getProximaData() != null && agendamento.getProximaData().isBefore(hoje)) {
            throw new IllegalArgumentException("Data inválida por estar no passado");
        }

        t.atualizarValor(agendamento.getValor());

        transacaoService.excluirTransacao(t.getId());
        transacaoService.salvarTransacao(t);
    }


    public void deletarAgendamento(String id){
        System.out.println("Passou por aqui");
        if (id == null || id.isBlank()) {
            throw new IllegalArgumentException("Id do agendamento obrigatorio");
        }
        System.out.println("Passou por aqui kkkk ");
        var opt = agRepo.obterAgendamento(id);
        if (opt.isEmpty()) {
            throw new NoSuchElementException("Agendamento não encontrado: " + id);
        }
        System.out.println("Passou por aqui kkkk olaaa ");
        transacaoService.excluirPorOrigemAgendamento(id);

        agRepo.deletarAgendamento(id);
    }

    public void salvarValidandoNaoNoPassado(Agendamento agendamento, LocalDate hoje, Conta conta) {
        Objects.requireNonNull(agendamento, "Agendamento obrigatório");
        Objects.requireNonNull(hoje, "Hoje obrigatório");
        if (agendamento.getProximaData() != null && agendamento.getProximaData().isBefore(hoje)) {
            throw new IllegalArgumentException("Data inválida por estar no passado");
        }
        salvarComTransacao(agendamento, conta);
    }

    public Iterable<Agendamento> buscarTodos(int pageSize){
        return agRepo.buscarTodos(pageSize);
    }

    public Iterable<Agendamento> buscarTodosPorPerfilId(String id, int pageSize){
        return agRepo.buscarTodosPorPerfilId(id, pageSize);
    }

    public Optional<Agendamento> obterAgendamento(String id) {
        return agRepo.obterAgendamento(id);
    }

    /** Executa se hoje == próximaData; cria transação e avança próximaData. */
    public boolean executarSeHoje(Agendamento agendamento, LocalDate hoje) {
        if (!agendamento.isAtivo() || !Objects.equals(agendamento.getProximaData(), hoje)) return false;

        String chave = agendamento.getId() + "#" + hoje;
        if (!execucoesDoDia.add(chave)) return false;

        transacaoService.criarPendenteDeAgendamento(
                agendamento.getId(), agendamento.getDescricao(), agendamento.getValor(), hoje, agendamento.getCategoriaId(), new Conta(), false, agendamento.getPerfilId()
        );

        agendamento.avancarProximaData();
        agRepo.salvar(agendamento);
        return true;
    }
}
