package com.sakuray.dubbo.api;

import com.alibaba.dubbo.config.ApplicationConfig;
import com.alibaba.dubbo.config.ProtocolConfig;
import com.alibaba.dubbo.config.RegistryConfig;
import com.alibaba.dubbo.config.ServiceConfig;
import com.sakuray.dubbo.service.DemoService;
import com.sakuray.dubbo.service.impl.DemoServiceImpl;

public class APIProvider {

	public static boolean running = true;
	
	public static void main(String[] args) {
        // 服务实现
        DemoService demoService = new DemoServiceImpl();
        
        // 当前应用配置
        ApplicationConfig applicationConfig =  new ApplicationConfig();
        applicationConfig.setName("provider");
        
        // 连接中心配置
        RegistryConfig registryConfig = new RegistryConfig();
        registryConfig.setAddress("zookeeper://127.0.0.1:2181");
        
        // 服务提供者协议配置
        ProtocolConfig protocolConfig = new ProtocolConfig();
        protocolConfig.setName("dubbo");
        protocolConfig.setPort(20886);
        protocolConfig.setPayload(16*1024*1024);
        
        // 注意: ServerConfig为重要对象，内部封装了与注册中心的连接，以及开启服务端口
        ServiceConfig<DemoService> service = new ServiceConfig<DemoService>(); // 此实例很重要，封装了与注册中心的连接，自行缓存，否则可能造成内存和连接泄露
        
        service.setApplication(applicationConfig);
        service.setRegistry(registryConfig);
        service.setProtocol(protocolConfig);
        service.setInterface(DemoService.class);
        service.setRef(demoService);
        
        service.export();
        
        System.out.println("------------------提供服务成功-------------------");
        
        Runtime.getRuntime().addShutdownHook(new Thread() {
            public void run() {
                synchronized (APIProvider.class) {
                    running = false;
                    APIProvider.class.notify();
                }
            }
        });
        
        synchronized(APIProvider.class) {
            while(running) {
                try {
                    APIProvider.class.wait();
                } catch (Throwable e) {
                }
            }
        }
    }
}
