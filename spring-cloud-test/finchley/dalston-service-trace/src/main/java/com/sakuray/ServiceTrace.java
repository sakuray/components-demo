package com.sakuray;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import zipkin.server.EnableZipkinServer;

@SpringBootApplication
@EnableZipkinServer
public class ServiceTrace {

    public static void main(String[] args) {
        new SpringApplicationBuilder(ServiceTrace.class).web(true).run(args);
    }
}
