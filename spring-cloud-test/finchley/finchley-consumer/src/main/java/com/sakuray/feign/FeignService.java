package com.sakuray.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(value = "provider-test", fallback = FallBackService.class)
public interface FeignService {

    @RequestMapping("hi")
    public String testFeign(@RequestParam(value = "name") String name);
}
