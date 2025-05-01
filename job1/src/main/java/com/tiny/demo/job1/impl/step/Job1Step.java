package com.tiny.demo.job1.impl.step;

import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.support.ListItemReader;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

import java.util.List;

@Configuration
@Slf4j
public class Job1Step {

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
