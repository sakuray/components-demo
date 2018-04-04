package com.sakuray;

import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;

@SpringBootApplication
public class ServiceTrace {

    public static void main(String[] args) {
        new SpringApplicationBuilder(ServiceTrace.class).web(WebApplicationType.SERVLET).run(args);
    }
}
