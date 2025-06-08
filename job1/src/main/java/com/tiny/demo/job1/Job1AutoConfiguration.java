package com.tiny.demo.job1;

import static com.tiny.demo.common.config.JobConfig.JOB_NAME;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan
@Slf4j
@RequiredArgsConstructor
@ConditionalOnProperty(name = JOB_NAME, havingValue = "job1")
public class Job1AutoConfiguration {

  private final JobBuilder jobBuilder;

  @Bean
  public Job job1(Step step1) {
    return jobBuilder.start(step1).build();
  }
}
