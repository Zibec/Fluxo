package agendamento;

import conta.Conta;
import transacao.TransacaoService;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

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


    public void salvarValidandoNaoNoPassado(Agendamento agendamento, LocalDate hoje) {
        Objects.requireNonNull(agendamento, "Agendamento obrigatório");
        Objects.requireNonNull(hoje, "Hoje obrigatório");
        if (agendamento.getProximaData() != null && agendamento.getProximaData().isBefore(hoje)) {
            throw new IllegalArgumentException("Data inválida por estar no passado");
        }
        agRepo.salvar(agendamento);
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
                agendamento.getId(), agendamento.getDescricao(), agendamento.getValor(), hoje, new Conta(), false, agendamento.getPerfilId()
        );

        agendamento.avancarProximaData();
        agRepo.salvar(agendamento);
        return true;
    }
}
