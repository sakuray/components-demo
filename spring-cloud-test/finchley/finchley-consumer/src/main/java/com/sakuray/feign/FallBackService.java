package com.sakuray.feign;

import org.springframework.stereotype.Component;

/**
 * 测试Feign的断路器
 */
@Component
public class FallBackService implements FeignService{
    @Override
    public String testFeign(String name) {
        return "i'm sorry, the service down";
    }
}
