package com.sakuray;

import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
@RestController
public class EurekaProvider {

    @RequestMapping(value = "/hi")
    public String home(@RequestParam(value = "name", required = false) String name) {
        return "hello world :" + name;
    }

    public static void main(String[] args) {
        new SpringApplicationBuilder(EurekaProvider.class).web(WebApplicationType.SERVLET).run(args);
    }
}
