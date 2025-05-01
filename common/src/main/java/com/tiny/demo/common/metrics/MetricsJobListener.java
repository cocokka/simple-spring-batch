package com.tiny.demo.common.metrics;

import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Timer;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobExecutionListener;

import java.util.concurrent.TimeUnit;

public class MetricsJobListener implements JobExecutionListener {
    private final MeterRegistry registry;
    private Long startTime;

    public MetricsJobListener(MeterRegistry registry) {
        this.registry = registry;
    }

    @Override
    public void beforeJob(JobExecution jobExecution) {
        startTime = System.currentTimeMillis();
    }

    @Override
    public void afterJob(JobExecution jobExecution) {
        Timer.builder("job.duration")
                .tag("job_name", jobExecution.getJobInstance().getJobName())
                .tag("status", jobExecution.getStatus().name())
                .register(registry)
                .record(System.currentTimeMillis() - startTime, TimeUnit.MILLISECONDS);
    }
}