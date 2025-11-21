package com.fluxo.agendador;

import agendamento.Agendamento;
import cartao.Cartao;
import org.quartz.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import transacao.Transacao;

import java.sql.Timestamp;

@Component
public class AgendadorTarefas {

    @Autowired
    private Scheduler scheduler;

    public void agendarFechamentoFatura(Cartao cartao) {
        try {
            JobDetail  job = JobBuilder.newJob(AgendarFechamentoFaturaJob.class)
                    .withIdentity("job-" + cartao.getId().getId())
                    .usingJobData("id", cartao.getId().getId())
                    .build();

            Trigger trigger = TriggerBuilder.newTrigger()
                    .withIdentity("trigger-" + cartao.getId().getId())
                    .startAt(Timestamp.valueOf(cartao.getDataFechamentoFatura().atStartOfDay()))
                    .build();

            scheduler.scheduleJob(job, trigger);
        } catch (SchedulerException e) {
            e.printStackTrace();
        }
    }

    public void agendarVencimentoFatura(Cartao cartao) {
        try {
            JobDetail  job = JobBuilder.newJob(AgendarVencimentoFaturaJob.class)
                    .withIdentity("job-" + cartao.getId().getId())
                    .usingJobData("id", cartao.getId().getId())
                    .build();

            Trigger trigger = TriggerBuilder.newTrigger()
                    .withIdentity("trigger-" + cartao.getId().getId())
                    .startAt(Timestamp.valueOf(cartao.getDataVencimentoFatura().atStartOfDay()))
                    .build();

            scheduler.scheduleJob(job, trigger);
        } catch (SchedulerException e) {
            e.printStackTrace();
        }
    }

    public void agendarTransacao(Agendamento a) {
        try {
            JobDetail  job = JobBuilder.newJob(AgendarTransacaoJob.class)
                    .withIdentity("job-" + a.getId())
                    .usingJobData("id", a.getId())
                    .build();

            Trigger trigger = TriggerBuilder.newTrigger()
                    .withIdentity("trigger-" + a.getId())
                    .startAt(Timestamp.valueOf(a.getProximaData().atStartOfDay()))
                    .build();

            scheduler.scheduleJob(job, trigger);
        } catch (SchedulerException e) {
            e.printStackTrace();
        }
    }
}
