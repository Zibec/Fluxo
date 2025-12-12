package com.fluxo.atualizador;

import org.modelmapper.internal.bytebuddy.implementation.bind.MethodDelegationBinder;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import taxaSelic.TaxaSelicService;

import java.time.LocalDate;

public class AtualizarTaxaSelicJob implements Job {

    @Autowired
    private TaxaSelicService taxaSelicService;

    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        System.out.println("Atualização da Taxa Selic em" + LocalDate.now());
        taxaSelicService.atualizarTaxaSelic();
    }
}
