package agendamento;

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

    public void salvar(Agendamento a) {
        agRepo.salvar(a);
    }

    public Optional<Agendamento> obter(String id) {
        return agRepo.obter(id);
    }

    /** Executa se hoje == próximaData; cria transação e avança próximaData. */
    public boolean executarSeHoje(Agendamento a, LocalDate hoje) {
        if (!a.isAtivo() || !Objects.equals(a.getProximaData(), hoje)) return false;

        String chave = a.getId() + "#" + hoje;
        if (!execucoesDoDia.add(chave)) return false; // já executou hoje

        transacaoService.criarPendenteDeAgendamento(
                a.getId(), a.getDescricao(), a.getValor(), hoje, null, false
        );

        a.avancarProximaData();
        agRepo.salvar(a); // persiste a nova próxima data
        return true;
    }
}
