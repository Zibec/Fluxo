package com.fluxo.config.selic;

import com.fluxo.atualizador.AtualizarTaxaSelicJob;
import org.quartz.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.TimeZone;

@Configuration
public class QuartzConfig {

    @Bean
    public JobDetail jobDetail(){
        return JobBuilder.newJob(AtualizarTaxaSelicJob.class)
                .withIdentity("jobAtualizarTaxaSelic")
                .storeDurably()
                .build();
    }

    @Bean
    public Trigger trigger(JobDetail jobDetail){
        return TriggerBuilder.newTrigger()
                .forJob(jobDetail)
                .withIdentity("triggerAtualizarTaxaSelic")
                .withSchedule(
                        CronScheduleBuilder
                                .dailyAtHourAndMinute(0, 0) //meia noite
                                .inTimeZone(TimeZone.getTimeZone("America/Sao_Paulo"))
                )
                .build();
    }
}
