package com.tiny.demo.job2.registry;

import com.tiny.demo.common.metrics.MetricsJobListener;
import io.micrometer.core.instrument.MeterRegistry;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConditionalOnProperty(name = "job.name", havingValue = "job2")
@ComponentScan(basePackages = {"com.tiny.demo.job2"})
@Slf4j
public class JobRegister {
    @Bean
    public Job job2(JobRepository jobRepository, Step step2, MeterRegistry registry) {
        return new JobBuilder("job2", jobRepository)
                .start(step2)
                .listener(new MetricsJobListener(registry))
                .build();
    }
}
