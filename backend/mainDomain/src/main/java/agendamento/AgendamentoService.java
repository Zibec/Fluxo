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

    public void salvar(Agendamento a) {
        agRepo.salvar(a);
    }


    public void salvarValidandoNaoNoPassado(Agendamento a, LocalDate hoje) {
        Objects.requireNonNull(a, "Agendamento obrigatório");
        Objects.requireNonNull(hoje, "Hoje obrigatório");
        if (a.getProximaData() != null && a.getProximaData().isBefore(hoje)) {
            throw new IllegalArgumentException("Data inválida por estar no passado");
        }
        agRepo.salvar(a);
    }

    public Optional<Agendamento> obter(String id) {
        return agRepo.obter(id);
    }

    /** Executa se hoje == próximaData; cria transação e avança próximaData. */
    public boolean executarSeHoje(Agendamento a, LocalDate hoje) {
        if (!a.isAtivo() || !Objects.equals(a.getProximaData(), hoje)) return false;

        String chave = a.getId() + "#" + hoje;
        if (!execucoesDoDia.add(chave)) return false;

        transacaoService.criarPendenteDeAgendamento(
                a.getId(), a.getDescricao(), a.getValor(), hoje, new Conta(), false, a.getPerfilId()
        );

        a.avancarProximaData();
        agRepo.salvar(a);
        return true;
    }
}
