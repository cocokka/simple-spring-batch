package com.tiny.demo.app.annotation;

import com.tiny.demo.job1.registry.EnableJob1;
import com.tiny.demo.job2.registry.EnableJob2;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Documented
@EnableJob1
@EnableJob2
public @interface EnableJobs {
}
