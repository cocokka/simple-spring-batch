# Simple Spring Batch Project Documentation

## 📦 1. Project Overview

This is a simple Spring Batch project designed to demonstrate how to configure and run batch jobs using Spring Boot and Spring Batch. The project supports multiple jobs (e.g., `job1`) and allows conditional configuration based on environment properties.

---

## 🧱 2. Project Structure

```
simple-spring-batch/
├── app/                          # Main application module
│   └── src/main/java/
│       └── com/tiny/demo/app/
│           ├── AppApplication.java     # Entry point of the application
│           └── ...                       # Common utilities, runners, etc.
│
├── job1/                         # Module for job1
│   └── src/main/java/
│       └── com/tiny/demo/job1/
│           ├── registry/
│           │   ├── JobRegister.java    # Configuration class defining job1 bean
│           │   └── EnableJob1.java     # Custom annotation to enable job1
│           └── ...                       # Step, Reader, Processor, Writer implementations
│
└── common/                       # Shared components across jobs
    └── src/main/java/
        └── com/tiny/demo/common/
            └── metrics/
                └── MetricsJobListener.java  # Reusable metrics listener
```


### Key Features:
- **Modular design**: Each job lives in its own module (`job1`, `job2`, etc.)
- **Conditional loading**: Jobs are only loaded if corresponding property (e.g., `job.name=job1`) is set
- **Component scanning**: Uses custom annotations and `@ComponentScan` to register beans
- **Extensible architecture**: Easy to add new jobs with minimal duplication

---

## ➕ 3. How to Add a New Job (Example: job2)

To add a new job named `job2`, follow these steps:

### Step 1: Create a New Module

Create a new directory structure like this:

```
job2/
└── src/main/java/
    └── com/tiny/demo/job2/
        └── registry/
            ├── JobRegister.java
            └── EnableJob2.java
```


### Step 2: Define the Job Configuration

**JobRegister.java**
```java
package com.tiny.demo.job2.registry;

import com.tiny.demo.common.metrics.MetricsJobListener;
import io.micrometer.core.instrument.MeterRegistry;
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
@ComponentScan("com.tiny.demo.job2")
public class JobRegister {

    @Bean
    public Job job2(JobRepository jobRepository, Step step2, MeterRegistry registry) {
        return new JobBuilder("job2", jobRepository)
                .start(step2)
                .listener(new MetricsJobListener(registry))
                .build();
    }
}
```


### Step 3: Create a Custom Enable Annotation (Optional)

**EnableJob2.java**
```java
package com.tiny.demo.job2.registry;

import org.springframework.context.annotation.Import;
import java.lang.annotation.*;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Import(JobRegister.class)
public @interface EnableJob2 {
}
```


### Step 4: Update Main Application to Include the New Job

In `AppApplication.java`, ensure that the package containing `job2` is scanned:

```java
@SpringBootApplication
@ComponentScan(basePackages = {
    "com.tiny.demo.app",
    "com.tiny.demo.common",
    "com.tiny.demo.job1.registry",
    "com.tiny.demo.job2.registry"
})
public class AppApplication {
    // ...
}
```


Or use your custom annotation:

```java
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@EnableJob1
@EnableJob2
public @interface EnableJobs {
    // ...
}
```


### Step 5: Run the New Job

Run the application with the `job.name` parameter:

```bash
./mvnw spring-boot:run -Dspring-boot.run.arguments="--job.name=job2"
```


Or via JAR:

```bash
java -jar target/app.jar --job.name=job2
```


---
