package com.tiny.demo.common.config;

import com.tiny.demo.common.metrics.MetricsJobListener;
import io.micrometer.core.instrument.MeterRegistry;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

@Configuration
@RequiredArgsConstructor
public class JobConfig {

  public static final String JOB_NAME = "job.name";

  private final JobRepository jobRepository;
  private final MeterRegistry registry;
  private final Environment environment;

  @Bean
  public JobBuilder jobBuilder() {
    return new JobBuilder(environment.getProperty(JOB_NAME), jobRepository)
        .listener(new MetricsJobListener(registry));
  }
}
