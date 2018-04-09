package com.sakuray.controller;

import com.sakuray.feign.FeignService;
import com.sakuray.rest.RestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

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
}