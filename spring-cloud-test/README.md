# spring cloud learning

<h2 id="navigate">目录</h2>

+ [1.基础概念](#1)
+ [2.构建练习](#2)
	+ [2.1 注册中心](#2.1)
	+ [2.2 生产者](#2.2)
	+ [2.3 消费者](#2.3)
	+ [2.4 断路器](#2.4)
	+ [2.5 断路器面板](#2.5)
	+ [2.6 网关路由](#2.6)
	+ [2.7 服务追踪](#2.7)

---
<h3 id="1" href="navigate">1.基础概念</h3>
> spring cloud提供了一系列工具让开发者可以快速的构建在分布式系统中共通的部分。例如：
	
* 配置管理
* 服务发现
* 断路器
* 智能路由
* 微代理
* 控制总线
* 一次性tokens
* 全局锁
* 领导人选举
* 分布式sessions
* 集群状态

> spring cloud初学时会接触到大量jar包，让人感到疑惑。实际上如果弄明白每个jar包的具体作用，那对整个spring cloud体系就会有一个初步的理解。

*    **Eureka**：这个包的主要作用是服务发现和注册了，其分为Client和Server。微服务最基础的一环就是必须要有注册中心，而开发者的服务必须要以某种协议方式注册到服务中心，让消费者能够发现可用的服务了。所以Eureka的Server的作用就是一个注册中心。Client就是服务提供方的服务了。
*    **ribbon**：这个包的主要作用是负载均衡。在微服务中负载均衡也是极其重要的一环，其用于保证系统的稳定，“让系统每一个节点均匀受力”。ribbon是一个纯粹的负载均衡器，需要与消费者一同使用，常见的消费者有spring提供的restTemplate以及Feign。
*    **Feign**：这个包就是上面提到的消费者了。上面提到的另一个restTemplate很容易理解，就是通过rest形式的http请求访问服务。这个Feign的含义和其名称一致，是个伪装者，也是通过http的方式访问服务，在程序内部构建了一个RequestTemplate进行请求，其伪装表现在其更像一个接口，可以像调用本地接口服务一样进行调用远程服务。
*    **Hystrix**：这个包的主要作用是断路器。通常情况下注册中心并不能及时发现后台哪个服务出现了故障，此情况下会使得消费者调用了一个不可用的服务，这很可能会带来灾难性的后果。比如消费者A调用服务B，服务B调用服务C，服务C调用服务D，假设服务D故障了，导致整条链路上的请求阻塞了，短时间内有大量请求服务D，这造成了大量的服务请求阻塞，直到耗尽所有线程资源，服务B、C、D都不可用。更糟糕的是服务E会调用服务C，服务E也会因为服务C不可用而阻塞导致E故障，这样不断的传导，导致整个服务集群宕机，这就是雪崩效应。
*    **zuul**：这个包的主要作用就是智能路由。说到路由就逃不掉负载均衡，所以其默认是与ribbon连用。zuul也是一个提供了一个服务，其要实现路由，也就必须获取注册中心Eureka的服务，所以其也是一个Eureka的Client。zuul提供了很多功能，此章节不进行介绍。
*    **config**：这个包是spring cloud开发的一个配置中心，服务过多最麻烦的就是配置混乱，所以需要集中统一管理，此包就是由此而来。该包和Eureka一样，分为Server和Client。Server负责存储配置，主要读取的是git或者SVN或者本地文件，加载配置。Client的主要作用就是读取这些配置。同样Server和Client的服务可以注册到Eureka达到自动发现的目的。
*    **Bus**：此包的作用主要是将各个服务节点通过轻量级的消息代理连接起来，起到一个消息总线的作用。可以用于配置更新后的广播通知服务的手段，也可以用于监控。可以使用RabbitMQ作为传播介质。
*    **Sleuth**：此包的作用主要就是链路追踪了。在断路器中也解释过，作为微服务，整个服务体系是很复杂的，相互调用的情况很普遍。所以需要一个监控整个调用链的手段，Sleuth就是用于此。Sleuth集成了zipkin。
*    **consul**：此包的作用和Eureka一致，也是服务发现和配置的工具。Eureka是Netflix开源的程序的封装。Consul是针对Consul而开发的产品。
> 服务发现的比较：[这里](https://blog.csdn.net/dengyisheng/article/details/71215234)

> Spring Cloud的GitHub地址：[这里](https://github.com/spring-cloud)

> 上面也仅仅是介绍了Spring cloud的一部分内容而已，Spring cloud包含非常多的组件，而且都有各自的一套版本号。事实上，初学者会被不同的版本号迷惑，比如目前就有：

* Finchley M*
* Edgware SR*
* Dalston SR*
* Camden SR*
> *代表数字，含义是第几个发行版本
>> [这里](https://projects.spring.io/spring-cloud/)有一个官网提供的当前各个模块组件的版本号。顺便一提上面的版本是按伦敦地铁站名称，按字母顺序时间顺序命名的，比如更早的还有Angel和Brixton版本。目前这些版本在官方的推荐中都不存在了。

>另外要注意的是上面这些版本依赖的Spring Boot的版本号不同。
>> + **Finchley是构建在Spring Boot 2.0.x上，并不能与1.5.x版本一同使用**
>> + **Dalston和Edgware是构建在1.5.x版本上，不能和2.0.x版本同用**
>> + **Camden构建在1.4.x版本上，但是在1.5.x版本上也测试过**
>> + **Angle和Brixton在2017年7月被标记成EOL，end-of-life，Brixton使用1.3.x版本，但也在1.4.x上测试过。Angle构建在1.2.x版本上，在某些方面与1.3.x不兼容。** 

<h3 id="2" href="navigate">2.构建练习</h3>
> 此次构建基于当前最新的Finchley M9版本，官方文档：[这里](http://cloud.spring.io/spring-cloud-static/spring-cloud-netflix/2.0.0.M8/multi/multi_spring-cloud-netflix.html)。文档配置上有些错误，不要全信。

> **注意：Finchley M9是基于SpringBoot2.0.0的，和早期的Dalston基于1.5.x版本的在代码书写上有不同之处，这点要特别注意。**
<h4 id="2.1" href="navigate">2.1 注册中心Eureka</h4>
> pom.xml配置如下：

	<parent>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-parent</artifactId>
        <version>2.0.0.RELEASE</version>
    </parent>

    <dependencies>
        <dependency>
            <groupId>org.springframework.cloud</groupId>
            <artifactId>spring-cloud-starter-netflix-eureka-server</artifactId>
        </dependency>
    </dependencies>

    <dependencyManagement>
        <dependencies>
            <dependency>
                <groupId>org.springframework.cloud</groupId>
                <artifactId>spring-cloud-dependencies</artifactId>
                <version>Finchley.M9</version>
                <type>pom</type>
                <scope>import</scope>
            </dependency>
        </dependencies>
    </dependencyManagement>

    <repositories>
        <repository>
            <id>spring-milestones</id>
            <name>Spring Milestones</name>
            <url>https://repo.spring.io/libs-milestone</url>
            <snapshots>
                <enabled>false</enabled>
            </snapshots>
        </repository>
    </repositories>

> 注册中心的代码十分短，如下：

	@SpringBootApplication
	@EnableEurekaServer
	public class EurekaServer {
	
	    public static void main(String[] args) {
	        new SpringApplicationBuilder(EurekaServer.class).web(WebApplicationType.SERVLET).run(args);
	    }
	}

> 主要是配置文件application.yml，单机的配置方法如下：

	server:
	  port: 8761
	
	eureka:
	  instance:
	    preferIpAddress: true
	    hostname: 127.0.0.1
	  client:
	    registerWithEureka: false
	    fetchRegistry:  false
	    serviceUrl:
	      defaultZone: http://${eureka.instance.hostname}:${server.port}/eureka/
配置好后，打开http://127.0.0.1:8761/就可以看见当前注册中心的状态了
> 注册中心集群的配置方法：

	---
	spring:
	  profiles: peer1
	eureka:
	  instance:
	    hostname: peer1
	  client:
	    serviceUrl:
	      defaultZone: http://peer2/eureka/
	
	---
	spring:
	  profiles: peer2
	eureka:
	  instance:
	    hostname: peer2
	  client:
	    serviceUrl:
	      defaultZone: http://peer1/eureka/

<h4 id="2.2" href="navigate">2.2 生产者(服务提供者)</h4>
>pom.xml配置

	<parent>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-parent</artifactId>
        <version>2.0.0.RELEASE</version>
    </parent>

    <dependencyManagement>
        <dependencies>
            <dependency>
                <groupId>org.springframework.cloud</groupId>
                <artifactId>spring-cloud-dependencies</artifactId>
                <version>Finchley.M9</version>
                <scope>import</scope>
                <type>pom</type>
            </dependency>
        </dependencies>
    </dependencyManagement>

    <dependencies>
        <dependency>
            <groupId>org.springframework.cloud</groupId>
            <artifactId>spring-cloud-netflix-eureka-client</artifactId>
        </dependency>
        <dependency>
            <groupId>com.netflix.eureka</groupId>
            <artifactId>eureka-client</artifactId>
        </dependency>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-actuator</artifactId>
        </dependency>
        <dependency>
            <groupId>org.springframework.cloud</groupId>
            <artifactId>spring-cloud-commons</artifactId>
        </dependency>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-web</artifactId>
        </dependency>
    </dependencies>

    <repositories>
        <repository>
            <id>spring-milestones</id>
            <name>Spring Milestones</name>
            <url>https://repo.spring.io/libs-milestone</url>
            <snapshots>
                <enabled>false</enabled>
            </snapshots>
        </repository>
    </repositories>
> 代码：

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


> 配置：

	server:
	  port: 8762
	
	spring:
	  application:
	    name: provider1-test
	
	eureka:
	  client:
	    serviceUrl:
	      defaultZone: http://127.0.0.1:8761/eureka/
	    healthcheck:
	      enabled: true
> 启动生产者，到这一步，再去访问Eureka注册中心的可视化界面，就可以看见服务被注册进去了。

<h4 id="2.3" href="navigate">2.3 消费者(服务调用方)</h4>
> 消费者这里介绍上面出现过的Rest和Feign，这两者都会配合ribbon做负载均衡来调用生产者。
> pom.xml配置如下：

	<parent>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-parent</artifactId>
        <version>2.0.0.RELEASE</version>
    </parent>

    <dependencies>
        <dependency>
            <groupId>org.springframework.cloud</groupId>
            <artifactId>spring-cloud-netflix-eureka-client</artifactId>
        </dependency>
        <dependency>
            <groupId>com.netflix.eureka</groupId>
            <artifactId>eureka-client</artifactId>
        </dependency>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-actuator</artifactId>
        </dependency>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-web</artifactId>
        </dependency>
        <dependency>
            <groupId>org.springframework.cloud</groupId>
            <artifactId>spring-cloud-starter-config</artifactId>
        </dependency>
        <dependency>
            <groupId>org.springframework.cloud</groupId>
            <artifactId>spring-cloud-commons</artifactId>
        </dependency>
        <!-- ribbon -->
        <dependency>
            <groupId>com.netflix.ribbon</groupId>
            <artifactId>ribbon-httpclient</artifactId>
        </dependency>
        <dependency>
            <groupId>com.netflix.ribbon</groupId>
            <artifactId>ribbon-eureka</artifactId>
        </dependency>
        <dependency>
            <groupId>org.springframework.cloud</groupId>
            <artifactId>spring-cloud-netflix-ribbon</artifactId>
        </dependency>
        <dependency>
            <groupId>com.netflix.ribbon</groupId>
            <artifactId>ribbon</artifactId>
        </dependency>
        <dependency>
            <groupId>com.netflix.ribbon</groupId>
            <artifactId>ribbon-loadbalancer</artifactId>
        </dependency>
        <!-- feign -->
        <dependency>
            <groupId>org.springframework.cloud</groupId>
            <artifactId>spring-cloud-starter-openfeign</artifactId>
        </dependency>
    </dependencies>

    <dependencyManagement>
        <dependencies>
            <dependency>
                <groupId>org.springframework.cloud</groupId>
                <artifactId>spring-cloud-dependencies</artifactId>
                <version>Finchley.M9</version>
                <scope>import</scope>
                <type>pom</type>
            </dependency>
        </dependencies>
    </dependencyManagement>

    <repositories>
        <repository>
            <id>spring-milestones</id>
            <name>Spring Milestones</name>
            <url>https://repo.spring.io/libs-milestone</url>
            <snapshots>
                <enabled>false</enabled>
            </snapshots>
        </repository>
    </repositories>
> 启动代码如下，早期版本还需一个EnableDiscoveryClient注解，最新的代码直接通过配置文件加载，不需要此注解了，@LoadBalanced使得rest开启了ribbon的负载均衡，feign整合了ribbon，不需要额外的设置：
	
	@SpringBootApplication
	@RestController
	@EnableFeignClients
	public class GeneralConsumer {
	
	    @Bean
	    @LoadBalanced
	    public RestTemplate restTemplate() {
	        return new RestTemplate();
	    }
	
	    public static void main(String[] args) {
	        new SpringApplicationBuilder(GeneralConsumer.class).web(WebApplicationType.SERVLET).run(args);
	    }
	}

> 配置文件如下：

	server:
	  port: 8764
	
	spring:
	  application:
	    name: consumer-test
	
	eureka:
	  client:
	    serviceUrl:
	      defaultZone: http://127.0.0.1:8761/eureka/
> 其它实现类：

	@Service
	public class RestService {
	
	    @Autowired
	    private RestTemplate restTemplate;
	
	    public String testService(String name) {
	        return restTemplate.getForObject("http://provider-test/hi?name="+name, String.class);
	    }
	}

	@FeignClient(value = "provider-test")
	public interface FeignService {
	
	    @RequestMapping("hi")
	    public String testFeign(@RequestParam(value = "name") String name);
	}

	@RestController
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
	}

> 最后访问 http://127.0.0.1:8764/feign?name=wangw2和http://127.0.0.1:8764/rest?name=wangw3 就能看见整套注册中心，生产者消费者部署成功了。
<h4 id="2.4" href="navigate">2.4 断路器</h4>
> 断路器的作用前面已经介绍了，这里介绍Hystrix的使用方法，断路器放在消费者，在上面消费者的代码基础上进行改造。**这里还要注意：断路器的超时时间要比服务调用的超时时间要长，包含重试的时间，道理很简单，默认重试3次，超时1秒，如果断路器2秒就生效了，重试的配置就没有效果了。**
> pom.xml配置文件添加：
	
		<dependency>
            <groupId>org.springframework.cloud</groupId>
            <artifactId>spring-cloud-starter-netflix-hystrix</artifactId>
        </dependency>
> 启动类上添加注解EnableCircuitBreaker，注解EnableHystrix也行，看其实现就可以明白其也就是EnableCircuitBreaker：

	@SpringBootApplication
	@RestController
	@EnableFeignClients
	@EnableCircuitBreaker
> rest使用方法：

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
> 再加两个测试接口：

	@RequestMapping(value = "rest/failB")
    public String testRestFailBack() {
        return restService.testFailureWithBack();
    }

    @RequestMapping(value = "rest/failN")
    public String testRestFailNothing() {
        return restService.testFailureWithNothing();
    }
> no-provider服务是不存在的，nothing的接口会返回错误，fallback的接口会显示fallback方法里的内容。

> feign自集成了断路器，但是默认是关闭的，所以需要在配置文件中开启：
	
	feign:
	  hystrix:
	    enabled: true
> 其注解上自带了fallback需要调用的类，所以创建一个失败时执行的类，注意该类要实现FeginClient的服务类接口：

	@Component
	public class FallBackService implements FeignService{
	    @Override
	    public String testFeign(String name) {
	        return "i'm sorry, the service down";
	    }
	}

	@FeignClient(value = "provider-test", fallback = FallBackService.class)
> 关闭服务provider-test，就能够看见错误提示了，再打开服务provider-test，不一会又能调用成功了。

>**这里再介绍一下hystrix断路器的一些工作细节：其判断一个服务是否不可能用的熔断算法判断依据是：根据bucket中记录的次数，计算错误率，当错误率超过预设的值(默认是50%)，且10秒内超过20个请求，就开启熔断。对于熔断的请求也不是永久切断，暂停一段时间后（默认5秒），允许部分请求通过，若请求是健康的（RT<250ms），则取消熔断，否则继续。服务调用的所有结果（成功、异常、超时、拒绝）都会计入bucket。**
<h4 id="2.5" href="navigate">2.5 断路器面板</h4>
> pom.xml补充下面依赖：

		<dependency>
            <groupId>org.springframework.cloud</groupId>
            <artifactId>spring-cloud-starter-netflix-hystrix-dashboard</artifactId>
        </dependency>
		<dependency>
            <groupId>org.springframework</groupId>
            <artifactId>spring-webflux</artifactId>
        </dependency>

> 启动类多添加一个注解@EnableHystrixDashboard

启动程序访问http://127.0.0.1:8764/hystrix就能看见主要的断路器工作的主要界面了。但是此时输入http://127.0.0.1:8764/hystrix.stream 设置刷新时间和监控名称，点击监控流是无法看见主界面的，具体原因还没定位到，查看源码暴露的hystrix.stream这个EndPoint虽然生成了，但是在后续执行的时候被filter排除了，原因是不在设置的暴露的EndPoint的include之中，这个还需研究一下，是新版的问题，还是由于配置导致的。但是根据hystrix.stream的原理，其只是注册了一个servlet，所以这里先手动注册这个servlet使用起来。

	@Bean
    public ServletRegistrationBean servletRegistrationBean() {
        HystrixMetricsStreamServlet streamServlet = new HystrixMetricsStreamServlet();
        ServletRegistrationBean registrationBean = new ServletRegistrationBean(streamServlet);
        registrationBean.setLoadOnStartup(1);
        registrationBean.addUrlMappings("/hystrix.stream");
        registrationBean.setName("HystrixMetricsStreamServlet");
        return registrationBean;
    }
> 每个消费者都有一个断路器面板，虽然有统计数据了，但是这样还是很麻烦，所以需要将系统中所有断路器的数据聚集起来。turbine这个组件可以做到，之后再补充其配置方法。

<h4 id="2.6" href="navigate">2.6 网关路由</h4>
> 此节介绍一下路由组件zuul的简单搭建使用。

> pom.xml文件配置如下：

	<parent>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-parent</artifactId>
        <version>2.0.0.RELEASE</version>
    </parent>

    <dependencies>
        <dependency>
            <groupId>org.springframework.cloud</groupId>
            <artifactId>spring-cloud-starter-netflix-zuul</artifactId>
        </dependency>
        <dependency>
            <groupId>org.springframework.cloud</groupId>
            <artifactId>spring-cloud-netflix-eureka-client</artifactId>
        </dependency>
        <dependency>
            <groupId>com.netflix.eureka</groupId>
            <artifactId>eureka-client</artifactId>
        </dependency>
        <dependency>
            <groupId>com.netflix.ribbon</groupId>
            <artifactId>ribbon-eureka</artifactId>
        </dependency>
    </dependencies>

    <dependencyManagement>
        <dependencies>
            <dependency>
                <groupId>org.springframework.cloud</groupId>
                <artifactId>spring-cloud-dependencies</artifactId>
                <version>Finchley.M9</version>
                <scope>import</scope>
                <type>pom</type>
            </dependency>
        </dependencies>
    </dependencyManagement>

    <repositories>
        <repository>
            <id>spring-milestones</id>
            <name>Spring Milestones</name>
            <url>https://repo.spring.io/libs-milestone</url>
            <snapshots>
                <enabled>false</enabled>
            </snapshots>
        </repository>
    </repositories>

> 启动类需要注解@EnableZuulProxy：

	@EnableZuulProxy
	@SpringBootApplication
	public class GateWay {
	
	    public static void main(String[] args) {
	        new SpringApplicationBuilder(GateWay.class).web(WebApplicationType.SERVLET).run(args);
	    }
	}
> application.yml配置：

	server:
	  port: 8767
	
	eureka:
	  client:
	    serviceUrl:
	      defaultZone: http://127.0.0.1:8761/eureka/
	
	spring:
	  application:
	    name: gateway
	
	zuul:
	  ribbon:
	    eager-load:
	      enabled: true
	  ingoredServices: '*'
	  routes:
	    consumer:
	      path: /consumer/**
	      sensitiveHeaders: Cookie,Set-Cookie,Authorization
	      serviceId: consumer-test
	      stripPrefix: true
	    provider:
	      path: /provider/**
	      serviceId: provider-test
	      stripPrefix: true
> 上面的配置使用了注册中心，zuul通过路由的path映射到serviceId，再去注册中心找到该服务的地址，最后将原地址，比如/consumer/rest?name=111代理到serviceId/rest?name=111。如果需要保留前缀，设置stripPrefix为false即可，还有一些cookie，header的设置，总体来说和nginx的配置思路相似，具体要看相关设置是如何配置的。

> 访问http://127.0.0.1:8767/consumer/rest?name=231 就可以看见其调用了相关服务的结果。

<h4 id="2.7" href="navigate">2.7 服务追踪</h4>
> 前面提到过断路器的作用和雪崩效应，实际上对于微服务而言，对于调用链的追踪也是十分重要的事情。本节介绍sleuth，其为Spring cloud实现了分布式追踪解决方案。官方文档：[这里](http://cloud.spring.io/spring-cloud-static/spring-cloud-sleuth/2.0.0.M9/multi/multi_spring-cloud-sleuth.html)
>> Span是最基础的工作单元，例如发送一个RPC请求是一个Span，Span通过一个64位的ID区分，多个Span构成一个Trace，这个就是整个请求的链路了。trace也有一个64位的ID，起始的Span(root span)的ID等于Trace的ID。

>sleuth基于zipkin，分为client和server。server主要提供客户端传输数据的接口(http模式下)和展示数据的ui界面。client主要提供采集数据的采集点，和发送数据的reporter和sender。

下面先介绍客户端的采集数据的方法，这里先介绍HTTP的方式，其是通过RestTemplateSender发送数据到server端，由AsyncReporter的build方法中开启的线程，以异步的方式发送。pom文件只需要引入一个jar包就行
	
	<dependency>
    	<groupId>org.springframework.cloud</groupId>
        <artifactId>spring-cloud-starter-zipkin</artifactId>
    </dependency>

application.yml中添加额外的参数：
	
	spring:
	  sleuth:
	    sampler:
	      probability: 1.0
	  zipkin:
    	baseUrl: http://127.0.0.1:9411/
probability的含义是样本的比例，1就是把所有的数据都发到server中，0.1就是只采集10%的数据，默认0.1。baseUrl就是zipkin的服务端地址了。默认就是http://localhost:9411/，还有些其它设置可以看ZipkinProperties类。

只配置了这些如果服务端没做好是看不到数据的（没有错误抛出，也没有日志），我们可以摒弃服务端，用控制台输出数据，自己创建一个控制台输出的Reporter对象，这样就可以验证client端是否运行正常：

	@Bean
    public Reporter<Span> reporter() {
        return Reporter.CONSOLE;
    }
这样访问任何接口，就能在控制台看见传输的数据了。要发送到服务端就注释掉这个Bean即可。原理是做了一个TracingFilter拦截请求和响应，生成对应的Span，请求结束finish掉Span，数据就通过Reporter和Sender发送到最终目的地了。

> server端的配置也十分简单，基本上都封装好了。创建一个项目，作为zipkin的服务端。顺便一提，截止目前2018/04为止，zipkin的server端基于spring-boot 1.5.xx版本，所以不要使用spring boot 2.0。会有一系列问题。所以该服务不是finchley M9版本，而是Dalston SR5，由于该服务作为单独服务，client端使用的和server使用的zipkin版本匹配即可，spring-boot版本不同问题不大。

> 首先是pom.xml配置

	<parent>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-parent</artifactId>
        <version>1.5.10.RELEASE</version>
    </parent>

    <dependencies>
        <dependency>
            <groupId>io.zipkin.java</groupId>
            <artifactId>zipkin-server</artifactId>
        </dependency>
        <dependency>
            <groupId>io.zipkin.java</groupId>
            <artifactId>zipkin-autoconfigure-ui</artifactId>
        </dependency>
    </dependencies>

    <dependencyManagement>
        <dependencies>
            <dependency>
                <groupId>org.springframework.cloud</groupId>
                <artifactId>spring-cloud-dependencies</artifactId>
                <version>Dalston.SR5</version>
                <type>pom</type>
                <scope>import</scope>
            </dependency>
        </dependencies>
    </dependencyManagement>

    <repositories>
        <repository>
            <id>spring-milestones</id>
            <name>Spring Milestones</name>
            <url>https://repo.spring.io/libs-milestone</url>
            <snapshots>
                <enabled>false</enabled>
            </snapshots>
        </repository>
    </repositories>

> 启动程序：

	@SpringBootApplication
	@EnableZipkinServer
	public class ServiceTrace {
	
	    public static void main(String[] args) {
	        new SpringApplicationBuilder(ServiceTrace.class).web(true).run(args);
	    }
	}

> 基本配置：

	server:
	  port: 9411
	
	spring:
	  application:
	    name: trace-test

> 启动服务，再访问配置了client端的web服务接口，就能在http://127.0.0.1:9411/zipkin/中看见相关的调用情况了。

下面再进行扩展一下，上述的服务端采集数据都是加载在内存中，显然这不是一个好主意，还有一个问题就是其是通过http的方式传输数据的，这个也可以采取其他方式，下面介绍使用rabbitmq进行传输数据，使用elasticsearch保存数据，及查询。

> client增加依赖和配置，使程序使用RabbitMQSender而不是RestTemplateSender。

	<dependency>
        <groupId>org.springframework.amqp</groupId>
        <artifactId>spring-rabbit</artifactId>
    </dependency>
> 配置rabbitmq的参数，主要见RabbitProperties类。

	spring:
	  rabbitmq:
	    host: 127.0.0.1
	    port: 5672
	    username: guest
	    password: guest
启动client端，访问接口，在rabbitmq的管理界面可以看见连接，以及该连接Publish了一条消息，但是消息并不会到达其发送的队列，因为该队列还没有生成，还需要把服务端启动起来，生成对应的队列才行。

> 服务端导入rabbitmq的依赖和elasticsearch的依赖：

	<dependency>
        <groupId>io.zipkin.java</groupId>
        <artifactId>zipkin-autoconfigure-collector-rabbitmq</artifactId>
    	<version>2.2.2</version>
    </dependency>
    <dependency>
       <groupId>io.zipkin.java</groupId>
       <artifactId>zipkin-autoconfigure-storage-elasticsearch-http</artifactId>
       <version>2.2.2</version>
    </dependency>
> 配置文件中添加：

	zipkin:
	  collector:
	    rabbitmq:
	      addresses: 127.0.0.1:5672
	      username: guest
	      password: guest
	  storage:
	    type: elasticsearch
	    elasticsearch:
	      hosts: 127.0.0.1:9300

> 其他的就不需要改变了，网上看到有使用@EnableZipkinStreamServer，依赖也和我的不同，尝试了一下没有成功，那个版本的配置好像是使用了spring的stream模块，暂无研究过。

> 测试方法和之前一样，访问一下client的接口，看rabbitmq队列是否有数据，再看elasticsearch中是否有数据，可以使用kibana看看，很方便。最后就是在zipkin提供的ui上查看调用情况了。和内存存储模式的不同之处在于，elasticsearch我配置完后，依赖分析那块是空的，其它功能正常。后来简单的查了下代码，好像elasticsearch的实现上就没有，只有span的，内存的依赖分析也是通过span在内存中查询得到的。所以可能就是暂时没实现，也可能是我看漏了什么。。
# 未完待续...