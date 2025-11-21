package com.fluxo.agendador;

import cartao.CartaoService;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

@Component
public class AgendarFechamentoFaturaJob implements Job {

    @Autowired
    private CartaoService cartaoService;

    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        String id = jobExecutionContext.getJobDetail().getJobDataMap().getString("id");

        cartaoService.fecharFatura(id);
    }
}
