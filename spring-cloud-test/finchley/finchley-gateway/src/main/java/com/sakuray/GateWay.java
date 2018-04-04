package com.sakuray;

import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.cloud.netflix.zuul.EnableZuulProxy;

@EnableZuulProxy
@SpringBootApplication
public class GateWay {

    public static void main(String[] args) {
        new SpringApplicationBuilder(GateWay.class).web(WebApplicationType.SERVLET).run(args);
    }
}
