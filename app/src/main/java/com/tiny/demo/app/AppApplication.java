package com.tiny.demo.app;

import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.core.env.Environment;
import org.springframework.util.Assert;

@SpringBootApplication
@ComponentScan(basePackages = {"com.tiny.demo"})
@RequiredArgsConstructor
public class AppApplication implements CommandLineRunner {

    private final ApplicationContext context;
    private final JobLauncher jobLauncher;
    private final Environment environment;

    public static void main(String[] args) {
        SpringApplication.run(AppApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        String jobName = args[0];
        if (environment.getProperty("job.name") != null) {
            jobName = environment.getProperty("job.name");
        }
        Assert.notNull(jobName, "job.name must be set");
        Job job = context.getBean(jobName, Job.class);
        jobLauncher.run(job, new JobParameters());
    }

}
