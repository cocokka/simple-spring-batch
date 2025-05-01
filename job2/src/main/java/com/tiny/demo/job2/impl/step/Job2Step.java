package com.tiny.demo.job2.impl.step;

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
public class Job2Step {

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
