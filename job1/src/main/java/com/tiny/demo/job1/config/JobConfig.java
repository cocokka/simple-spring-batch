package com.tiny.demo.job1.config;

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
@ConditionalOnProperty(name = "job.name", havingValue = "job1")
@Slf4j
public class JobConfig {
    @Bean
    public Job job1(JobRepository jobRepository, Step step1, MeterRegistry registry) {
        return new JobBuilder("job1", jobRepository)
                .start(step1)
                .listener(new MetricsJobListener(registry))
                .build();
    }

    @Bean
    public Step step1(JobRepository jobRepository, PlatformTransactionManager txManager) {
        return new StepBuilder("step1", jobRepository)
                .<String, String>chunk(10, txManager)
                .reader(reader1())
                .processor(processor1())
                .writer(writer1())
                .build();
    }

    @Bean
    public ItemReader<String> reader1() {
        return new ListItemReader<>(List.of("data1", "data2"));
    }

    @Bean
    public ItemProcessor<String, String> processor1() {
        return String::toUpperCase;
    }

    @Bean
    public ItemWriter<String> writer1() {
        return items -> items.forEach(log::info);
    }
}
