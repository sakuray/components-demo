# spring-boot框架基本练习
此demo基于springboot 1.5.7.RELEASE版本，使用h2数据库，主要展示一个基本的SpringBoot框架的使用

## logback日志配置

springboot默认使用logback来记录日志,默认使用的配置文件在spring-boot-1.5.7.RELEASE.jar里面的logging模块下的logback子包中：默认使用base.xml配置文件。

但是可以通过修改application.properties配置文件中的logging.config来指定自定义的日志配置项。具体配置方法不进行介绍，base提供了console和file两个输出目标，一般来说够用，具体也是在application.properties中进行配置,具体不进行介绍，自定义日志的方法看项目。

application.properties中与日志相关的配置如下(application所有的配置点击这里[配置项](https://docs.spring.io/spring-boot/docs/current/reference/html/common-application-properties.html))：

	1. logging.config= # 日志配置文件的位置，如：classpath:logback.xml
	2. logging.exception-conversion-word=%wEx # 记录异常时使用的转换词。
	3. logging.file= # 日志文件名称，如：`myapp.log`
	4. logging.level.*= # 日志服务级别映射.如：`logging.level.org.springframework=DEBUG`
	5. logging.path= # 日志文件位置.如：`/var/log`
	6. logging.pattern.console= # 控制台输出格式，只支持默认的logback设置
	7. logging.pattern.file= # 日志文件输出格式，只支持默认的logback设置.
	8. logging.pattern.level= # 日志级别的格式，默认%5p，只支持默认的logback设置.
	9. logging.register-shutdown-hook=false # 文件系统初始化时注册一个hook
	
## 读取配置文件

springboot默认的配置文件是application.properties，其初始化会扫描这个文件中的内容。自定义的配置项要与实体类进行映射使用有很多种方法，这里只介绍其中一种。

早期版本的springboot好像会扫描所有的资源文件，现在版本的貌似不行（没有深入研究，只是不同版本相同配置产生了不一样的效果得出的），如果不是从application.properties中配置的话，需要指定一下其读取的文件。具体参见jdbc.properties与类JdbcParam的映射配置。

	@Component
	@PropertySource(value={"classpath:jdbc.properties"})
	@ConfigurationProperties(prefix="jdbc")
	public class JdbcParam {
	}
这个配置应该很好理解，首先需要被spring管理（@Component），第二个就是指定来源文件（@PropertySource，如果是在application.properties配置的，可以不需要这个配置），最后就是配置项的前缀了（@ConfigurationProperties），配置项除去前缀的后面内容，就是该类的字段名，spring会将其一一映射。

## bean配置
springboot bean的配置和spring没什么区别，这里需要注意的是filter的配置，有两种方法，一种是通过在启动类上添加@ServletComponentScan，并在filter上添加@WebFilter来处理，另一种则是通过SpringBoot提供的FilterRegistrationBean来注册filter.这里当然介绍第二种方法。详细见FilterBean类的配置方法，比较简单。

## web基本编写
参见test包，里面是一个基本的样例