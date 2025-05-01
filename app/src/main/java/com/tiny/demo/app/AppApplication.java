package com.tiny.demo.app;

import com.tiny.demo.app.annotation.EnableJobs;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.beans.BeansException;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.core.env.Environment;
import org.springframework.util.Assert;

@SpringBootApplication
@ComponentScan(basePackages = {
        "com.tiny.demo.app",
        "com.tiny.demo.common"
})
@EnableJobs
@RequiredArgsConstructor
@Slf4j
public class AppApplication implements CommandLineRunner {

    private final ApplicationContext context;
    private final JobLauncher jobLauncher;
    private final Environment environment;

    public static void main(String[] args) {
        SpringApplication.run(AppApplication.class, args);
    }

    @Override
    public void run(String... args) {
        String jobName = environment.getProperty("job.name");
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

}
