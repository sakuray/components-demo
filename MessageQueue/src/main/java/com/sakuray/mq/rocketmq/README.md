# rocketMQ

## 简介
rocketMQ是由阿里巴巴开发的一款消息中间件技术，之后交给apache进行后续开发维护，广泛的运用在阿里巴巴的业务中，经过了实际的商业考验。

参考文档[点击这里](https://rocketmq.apache.org/docs/quick-start/)
下载地址[点击这里](https://rocketmq.apache.org/dowloading/releases/)

本次测试基于目前最新版本4.1.0-incubating

## 安装
将项目下载下来之后解压，需要JDK1.8

运行NameServer:

	1. 设置环境变量：ROCKETMQ_HOME  如：D:\Program Files\rocketmq-all-4.1.0-incubating
	2. 确认设置了环境变量JAVA_HOME
	3. 双击bin目录下的mqnamesrv.cmd，如果启动异常，可能是读取路径的问题，需要修改脚本，参见步骤4，如果没有问题，继续步骤5
	4. 修改runserver.cmd脚本，注释set BASE_DIR 相关的3行（前面加rem就是注释）。添加set BASE_DIR=%ROCKETMQ_HOME%\  set LIB_DIR="%BASE_DIR%lib"这两行代码在注释的下面，修改一行成set "JAVA_OPT=%JAVA_OPT% -Djava.ext.dirs=%LIB_DIR%"
	5. 命令行下运行mqbroker.cmd -n localhost:9876，一样可能出现路径不对的问题，和步骤4一样。这里可能出现内存不足的情况，需要额外修改set "JAVA_OPT=%JAVA_OPT% -server -Xms2g -Xmx2g -Xmn1g" 减小内存配置，例如：set "JAVA_OPT=%JAVA_OPT% -server -Xms128m -Xmx256m -Xmn64m" 根据自己的机器适当调整.这么处理后还是会有问题,classpath的设置是有问题的，如果环境变量中设置过了classpath，改成set CLASSPATH="%CLASSPATH%;%BASE_DIR%conf;"，没有设置过直接加个""。
	6. 测试是否成功。修改tools中的内容，基本步骤和4相同，再添加一项set NAMESRV_ADDR=localhost:9876。生产者tools.cmd org.apache.rocketmq.example.quickstart.Producer。消费者：tools.cmd org.apache.rocketmq.example.quickstart.Consumer
	
吐槽下rocketmq的windows脚本写的真糟糕... 而且控制台没有日志。日志文件默认在C:\Users\xxx\logs\rocketmqlogs下，可以在conf目录下配置

## 简单的消息类型及发送
### RocketMQ的三种消息
RocketMQ发送消息有三种：
	
	1. 可靠的同步发送
	2. 可靠的异步发送
	3. 单向传输（像UDP，存在潜在的丢消息和最大吞吐量）
具体参见rocketmq.simple包

### 顺序消息
RocketMQ提供了有顺序FIFO的顺序消息，具体发送参见rocketmq.order

### 广播消息
广播给所有订阅了topic的消费者，如果你想要所有订阅者都接受消息：rocketmq.broadcasting

### 定时消息
定时消息只有等到指定时间才会发送消息，具体适用见：rocketmq.schedule

### 批量发送
使用批量发送是有限制的，Messageb必须要有相同的topic和waitStoreMsgOK,不支持定时
此外，发送的消息批量一次不要超过1MB，如果你不确定消息是否超过1MB，也有办法解决，具体使用方法见:rocketmq.batch


### 过滤接收消息
[参考文章](https://github.com/apache/rocketmq-site/blob/master/_posts/2017-04-26-filter-messages-by-sql92-in-rocketmq.md)
通常情况下tag是非常简单且有用的，但是一个消息只能有一个tag，在一些复杂的情景下这就有了很大的局限，此时，你可以使用SQL92来过滤出你需要的消息。

**语法**

	1.数值表达式  > >= < <= = BETWEEN
	2.字符串表达式 = <> IN
	3.IS NULL 和 IS NOT NULL
	4.逻辑表达式 AND, OR, NOT
	
具体使用见rocketmq.filter
出现：CODE: 1  DESC: The broker does not support consumer to filter message by SQL92,暂未解决，该功能没有使用成功