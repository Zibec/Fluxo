package agendamento;

import transacao.TransacaoService;

import java.time.LocalDate;
import java.util.*;

public class AgendamentoService {

    private final Map<String, Agendamento> agendamento = new HashMap<>();
    private final Set<String> execucoesDoDia = new HashSet<>(); // evita reexecução no mesmo dia
    private final TransacaoService transacaoService;

    public AgendamentoService(TransacaoService transacaoService) {
        this.transacaoService = Objects.requireNonNull(transacaoService);
    }

    public void salvar(Agendamento a) { agendamento.put(a.getId(), a); }

    public Optional<Agendamento> obter(String id) { return Optional.ofNullable(agendamento.get(id)); }

    public boolean executarSeHoje(Agendamento a, LocalDate hoje) {
        if (!a.isAtivo() || !Objects.equals(a.getProximaData(), hoje)) return false;

        String chave = a.getId() + "#" + hoje;
        if (!execucoesDoDia.add(chave)) return false; // já executou hoje

        transacaoService.criarPendenteDeAgendamento(
                a.getId(),
                a.getDescricao(),
                a.getValor(),
                hoje
        );

        a.avancarProximaData();
        agendamento.put(a.getId(), a); // persiste alteração da próxima data
        return true;
    }
}
