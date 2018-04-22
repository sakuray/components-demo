package com.sakuray.controller;

import com.sakuray.feign.FeignService;
import com.sakuray.rest.RestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
//@RefreshScope
public class TestController {

    @Autowired
    private RestService restService;
    @Autowired
    private FeignService feignService;

    @RequestMapping(value = "rest")
    public String testRest(@RequestParam String name) {
        return restService.testService(name);
    }

    @RequestMapping(value = "feign")
    public String testFeign(@RequestParam String name) {
        return feignService.testFeign(name);
    }

    @RequestMapping(value = "rest/failB")
    public String testRestFailBack() {
        return restService.testFailureWithBack();
    }

    @RequestMapping(value = "rest/failN")
    public String testRestFailNothing() {
        return restService.testFailureWithNothing();
    }

<<<<<<< HEAD
    @RequestMapping(value = "nothing")
    public String testSleuth() {
        return "do nothing!!!";
=======
    @RequestMapping(value = "sleuth")
    public String testSleuth() {
        return "Call Tracing Consumer";
    }

//    @Value("${foo}")
//    private String foo;
//
//    @RequestMapping(value = "config")
//    public String testConfig() {
//        return foo;
//    }

    @RequestMapping(value = "login")
    public String testLogin() {
        return UUID.randomUUID().toString();
    }

    @RequestMapping(value = "security")
    public String testSecurity() {
        return "only login success can through zuul arrive here. congratulation!!!";
>>>>>>> 6ee02b5cf3e426e48a828c84ba0da9c625477c96
    }
}
