package com.sakuray.rest;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class RestService {

    @Autowired
    private RestTemplate restTemplate;

    public String testService(String name) {
        return restTemplate.getForObject("http://provider-test/hi?name="+name, String.class);
    }


    public String testFailureWithNothing() {
        return restTemplate.getForObject("http://no-provider/", String.class);
    }

    @HystrixCommand(fallbackMethod = "fallback")
    public String testFailureWithBack() {
        return restTemplate.getForObject("http://no-provider/", String.class);
    }

    public String fallback() {
        return "the service is unavailiable!!!";
    }
}
