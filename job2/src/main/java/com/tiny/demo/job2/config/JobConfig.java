package com.tiny.demo.job2.config;

import com.tiny.demo.common.metrics.MetricsJobListener;
import io.micrometer.core.instrument.MeterRegistry;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.support.ListItemReader;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

import java.util.List;

@Configuration
@ConditionalOnProperty(name = "job.name", havingValue = "job2")
@Slf4j
public class JobConfig {
    @Bean
    public Job job2(JobRepository jobRepository, Step step1, MeterRegistry registry) {
        return new JobBuilder("job2", jobRepository)
                .start(step1)
                .listener(new MetricsJobListener(registry))
                .build();
    }

    @Bean
    public Step step2(JobRepository jobRepository, PlatformTransactionManager txManager) {
        return new StepBuilder("step2", jobRepository)
                .<String, String>chunk(10, txManager)
                .reader(reader2())
                .processor(processor2())
                .writer(writer2())
                .build();
    }

    @Bean
    public ItemReader<String> reader2() {
        return new ListItemReader<>(List.of("data21", "data22"));
    }

    @Bean
    public ItemProcessor<String, String> processor2() {
        return String::toUpperCase;
    }

    @Bean
    public ItemWriter<String> writer2() {
        return items -> items.forEach(log::info);
    }
}
