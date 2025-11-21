package com.fluxo.agendador;

import agendamento.Agendamento;
import agendamento.AgendamentoService;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import transacao.Transacao;
import transacao.TransacaoService;

import java.time.LocalDate;

@Component
public class AgendarTransacaoJob implements Job {

    @Autowired
    private TransacaoService transacaoService;

    @Autowired
    private AgendamentoService agendamentoService;


    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        String id = jobExecutionContext.getJobDetail().getJobDataMap().getString("id");

        Agendamento agendamento = agendamentoService.obterAgendamento(id).get();
        agendamentoService.executarSeHoje(agendamento, LocalDate.now());
    }
}
