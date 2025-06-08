package com.tiny.demo.app;

import static com.tiny.demo.common.config.JobConfig.JOB_NAME;

import com.zaxxer.hikari.HikariDataSource;
import java.util.Map;
import java.util.Optional;
import javax.sql.DataSource;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.beans.BeansException;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.core.env.Environment;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.util.Assert;

@SpringBootApplication
@ComponentScan(basePackages = {"com.tiny.demo.app", "com.tiny.demo.common"})
@EnableBatchProcessing
@RequiredArgsConstructor
@Slf4j
public class AppApplication implements CommandLineRunner {

  private final ApplicationContext context;
  private final JobLauncher jobLauncher;
  private final Environment environment;
  private final DataSource dataSource;

  public static void main(String[] args) {
    SpringApplication application = new SpringApplication(AppApplication.class);
    Optional.ofNullable(System.getProperty(JOB_NAME))
        .ifPresent(jobName -> application.setDefaultProperties(Map.of(JOB_NAME, jobName)));
    application.run(args);
  }

  @Override
  public void run(String... args) {
    printDataSourceInfo();

    String jobName = environment.getProperty(JOB_NAME);
    Assert.notNull(jobName, "job.name must be set");

    String[] beanNames = context.getBeanDefinitionNames();
    for (String name : beanNames) {
      if (context.getType(name).getName().startsWith("com.tiny.demo")) {
        log.info("Got bean {} -> {}", name, context.getType(name));
      }
    }

    try {
      Job job = context.getBean(jobName, Job.class);
      jobLauncher.run(job, new JobParameters());
    } catch (BeansException e) {
      log.warn("Job not found: {}", jobName);
    } catch (Exception e) {
      log.error("Error running job: {}", jobName, e);
    }
  }

  private void printDataSourceInfo() {
    log.info("DataSource class: {}", dataSource.getClass().getName());

    switch (dataSource) {
      case DriverManagerDataSource driverManagerDataSource -> {
        log.info("JDBC URL: {}", driverManagerDataSource.getUrl());
        log.info("Username: {}", driverManagerDataSource.getUsername());
      }
      case HikariDataSource hikariDataSource -> {
        log.info("JDBC URL: {}", hikariDataSource.getJdbcUrl());
        log.info("Username: {}", hikariDataSource.getUsername());
      }
      default -> log.warn("Unknown DataSource type: {}", dataSource.getClass());
    }
  }
}
