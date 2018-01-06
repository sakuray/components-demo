package com.sakuray.dubbo.api;

import com.alibaba.dubbo.config.ApplicationConfig;
import com.alibaba.dubbo.config.ReferenceConfig;
import com.alibaba.dubbo.config.RegistryConfig;
import com.sakuray.dubbo.service.DemoService;

public class APIConsumer {
    
    public static void main(String[] args) {
        // 当前应用配置
        ApplicationConfig applicationConfig =  new ApplicationConfig();
        applicationConfig.setName("consumer");
        
        // 连接中心配置
        RegistryConfig registryConfig = new RegistryConfig();
        registryConfig.setAddress("zookeeper://127.0.0.1:2181");
        
        ReferenceConfig<DemoService> reference = new ReferenceConfig<DemoService>();
        reference.setApplication(applicationConfig);
        reference.setRegistry(registryConfig);
        reference.setInterface(DemoService.class);
        
        // 和本地的bean一样使用
        DemoService demoService = reference.get();
        System.out.println(demoService.greet("李四"));
        System.out.println(demoService.getUsers());
    }
}
