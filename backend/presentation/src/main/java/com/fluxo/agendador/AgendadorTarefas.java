package com.fluxo.agendador;

import agendamento.Agendamento;
import cartao.Cartao;
import persistencia.jpa.jobs.job.JobAgendado;
import persistencia.jpa.jobs.job.TipoJob;
import org.quartz.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import persistencia.jpa.jobs.JobsRepository;
import transacao.Tipo;

import java.sql.Timestamp;
import java.time.LocalDateTime;

@Component
public class AgendadorTarefas {

    @Autowired
    private Scheduler scheduler;

    @Autowired
    private JobsRepository jobsRepository;

    public void agendarFechamentoFatura(Cartao cartao) {
        try {
            JobDetail  job = JobBuilder.newJob(AgendarFechamentoFaturaJob.class)
                    .withIdentity("job-fechamento-" + cartao.getId().getId())
                    .usingJobData("id", cartao.getId().getId())
                    .build();

            Trigger trigger = TriggerBuilder.newTrigger()
                    .withIdentity("trigger-fechamento-" + cartao.getId().getId())
                    .startAt(Timestamp.valueOf(cartao.getDataFechamentoFatura().atStartOfDay()))
                    .build();

            scheduler.scheduleJob(job, trigger);
            jobsRepository.save(new JobAgendado(cartao.getId().getId() + "-fechamento", cartao.getDataFechamentoFatura().atStartOfDay(), TipoJob.FECHAMENTOFATURA, ""));

        } catch (SchedulerException e) {
            e.printStackTrace();
        }
    }

    public void agendarVencimentoFatura(Cartao cartao) {
        try {
            JobDetail  job = JobBuilder.newJob(AgendarVencimentoFaturaJob.class)
                    .withIdentity("job-vencimento-" + cartao.getId().getId())
                    .usingJobData("id", cartao.getId().getId())
                    .build();

            Trigger trigger = TriggerBuilder.newTrigger()
                    .withIdentity("trigger-vencimento-" + cartao.getId().getId())
                    .startAt(Timestamp.valueOf(cartao.getDataVencimentoFatura().atStartOfDay()))
                    .build();

            scheduler.scheduleJob(job, trigger);
            jobsRepository.save(new JobAgendado(cartao.getId().getId() + "-vencimento", cartao.getDataVencimentoFatura().atStartOfDay(), TipoJob.VENCIMENTOFATURA, ""));
        } catch (SchedulerException e) {
            e.printStackTrace();
        }
    }

    public void agendarTransacao(Agendamento a, String contaId) {
        try {
            JobDetail  job = JobBuilder.newJob(AgendarTransacaoJob.class)
                    .withIdentity("job-" + a.getId())
                    .usingJobData("id", a.getId())
                    .usingJobData("contaId", contaId)
                    .build();

            Trigger trigger = TriggerBuilder.newTrigger()
                    .withIdentity("trigger-" + a.getId())
                    .startAt(Timestamp.valueOf(a.getProximaData()))
                    .build();

            System.out.println("agendado para: " + a.getProximaData());
            System.out.println("agora: " + LocalDateTime.now());
            scheduler.scheduleJob(job, trigger);
            jobsRepository.save(new JobAgendado(a.getId(), a.getProximaData(), TipoJob.AGENDAMENTO, contaId));
        } catch (SchedulerException e) {
            e.printStackTrace();
        }
    }

    public void atualizarAgendamento(Agendamento a, String contaId) {
        try {
            Trigger trigger = TriggerBuilder.newTrigger()
                    .withIdentity("trigger-" + a.getId())
                    .startAt(Timestamp.valueOf(a.getProximaData()))
                    .build();
            System.out.println("atualizou");
            scheduler.rescheduleJob(trigger.getKey(), trigger);
            jobsRepository.save(new JobAgendado(a.getId(), a.getProximaData(), TipoJob.AGENDAMENTO, contaId));
        } catch(SchedulerException e) {
            e.printStackTrace();
        }
    }
    
    public void atualizarFatura(Cartao cartao) {
        try {

            Trigger trigger1 = TriggerBuilder.newTrigger()
                    .withIdentity("trigger-vencimento-" + cartao.getId().getId())
                    .startAt(Timestamp.valueOf(cartao.getDataVencimentoFatura().atStartOfDay()))
                    .build();
            System.out.println("atualizou");
            scheduler.rescheduleJob(trigger1.getKey(), trigger1);
            jobsRepository.save(new JobAgendado(cartao.getId().getId() + "-vencimento", cartao.getDataVencimentoFatura().atStartOfDay(), TipoJob.VENCIMENTOFATURA, ""));

            Trigger trigger2 = TriggerBuilder.newTrigger()
                    .withIdentity("trigger-fechamento-" + cartao.getId().getId())
                    .startAt(Timestamp.valueOf(cartao.getDataFechamentoFatura().atStartOfDay()))
                    .build();
            System.out.println("atualizou");
            scheduler.rescheduleJob(trigger2.getKey(), trigger2);
            jobsRepository.save(new JobAgendado(cartao.getId().getId() + "-fechamento", cartao.getDataFechamentoFatura().atStartOfDay(), TipoJob.FECHAMENTOFATURA, ""));

        } catch(SchedulerException e) {
            e.printStackTrace();
        }
    }

    public void deletarAgendamento(String id) {
        try {
            System.out.println("deletou");
            scheduler.deleteJob(new JobKey("job-"+id));
            jobsRepository.deleteById(id);
        } catch(SchedulerException e) {
            e.printStackTrace();
        }
    }

    public void deletarFatura(String id) {
        try {
            System.out.println("deletou");
            scheduler.deleteJob(new JobKey("job-vencimento-" + id));
            scheduler.deleteJob(new JobKey("job-fechamento-" + id));
            jobsRepository.deleteById(id);
        } catch(SchedulerException e) {
            e.printStackTrace();
        }
    }

    public void buildJobs(JobAgendado job) {
        try {
            System.out.println(job.getData());
            System.out.println(LocalDateTime.now().minusHours(3));
            if (job.getData().isBefore(LocalDateTime.now())) {
                System.out.println("Ignorando job atrasado: " + job.getId());
                return;
            }

            JobDetail jobDetail = null;
            Trigger trigger = null;

            // Decide qual tipo de job recriar
            if (job.getTipo() == TipoJob.AGENDAMENTO) {

                jobDetail = JobBuilder.newJob(AgendarTransacaoJob.class)
                        .withIdentity("job-" + job.getId())
                        .usingJobData("id", job.getId())
                        .usingJobData("contaId", job.getAuxiliar())
                        .build();

                trigger = TriggerBuilder.newTrigger()
                        .withIdentity("trigger-" + job.getId())
                        .startAt(Timestamp.valueOf(job.getData()))
                        .build();
            }

            if (job.getTipo() == TipoJob.FECHAMENTOFATURA) {

                jobDetail = JobBuilder.newJob(AgendarFechamentoFaturaJob.class)
                        .withIdentity("job-fechamento-" + job.getId())
                        .usingJobData("id", job.getId())
                        .build();

                trigger = TriggerBuilder.newTrigger()
                        .withIdentity("trigger-fechamento-" + job.getId())
                        .startAt(Timestamp.valueOf(job.getData()))
                        .build();
            }

            if(job.getTipo() == TipoJob.VENCIMENTOFATURA) {
                jobDetail = JobBuilder.newJob(AgendarFechamentoFaturaJob.class)
                        .withIdentity("job-vencimento-" + job.getId())
                        .usingJobData("id", job.getId())
                        .build();

                trigger = TriggerBuilder.newTrigger()
                        .withIdentity("trigger-vencimento-" + job.getId())
                        .startAt(Timestamp.valueOf(job.getData()))
                        .build();
            }

            if (jobDetail == null || trigger == null) {
                System.out.println("Tipo de job não reconhecido: " + job.getTipo());
                return;
            }

            // Se já existe, substitui
            if (scheduler.checkExists(jobDetail.getKey())) {
                scheduler.deleteJob(jobDetail.getKey());
            }

            scheduler.scheduleJob(jobDetail, trigger);
            System.out.println("Job recriado: " + job.getId() + " para " + job.getData());

        } catch (SchedulerException e) {
            e.printStackTrace();
        }
    }
}
