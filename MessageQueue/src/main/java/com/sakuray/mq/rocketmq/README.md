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
再吐槽下日志配置控制台输出：RocketmqBroker的logger添加<appender-ref ref="STDOUT"/> 在<root>上添加没有作用

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

## 核心概念

### Producer
一个Producer发送由商业应用系统产生的消息到brokers中去，RocketMQ提供多种发送范例：同步，异步和单边发送

producer是线程安全的

#### Producer Group
相同角色的Producer分成1组，相同组的不同Producer实例可以通过一个broker关联，进行commit或者roll back在一个事务中，防止最初的producer在交易后崩溃

通常，group没什么作用。但是如果你使用了事务，你需要注意它。默认情况下，你只能创建一个producer在一个group中，在同一个JVM。这已经足够了。

注意：考虑到提供Producer在发送消息时足够高效，每个组只有一个实例producer，从而避免不必要的producer实例初始化

#### Async Sending
通常发送是阻塞的，直到响应返回，如果你关系性能，推荐使用send(msg,callback)使用异步方式。

#### SendStatus
发送消息后，你会获得一个包含SendStatus的SendResult对象。首先，假设Message的isWaitStoreMsgOK是true(默认true)。如果不是，通常我们会获得SEND_OK，没有异常抛出。下面是每个状态的描述：

`FLUSH_DISK_TIMEOUT`

如果broker设置MessageStoreConfig配置FlushDiskType=`SYNC_FLUSH`(默认是`ASYNC_FLUSH`)，并且Broker没有完成刷新syncFlushTimeout(默认5秒)，你会获得这个状态

`FLUSH_SLAVE_TIMEOUT`

如果broker的角色是`SYNC_MASTER`(默认是`ASYNC_MASTER`)，并且slave broker没有完成同步master，syncFlushTimeout(默认5秒)，你会获得这个状态

`SLAVE_NOT_AVAILABLE`

如果broker角色是`SYNC_MASTER`(默认是`ASYNC_MASTER`)，但是没有配置slave，你会获得这个状态

`SEND_OK`

这个状态并不意味者可靠，确保没有消息丢失，你需要启用`SYNC_MASTER`或`SYNC_FLUSH`

Duplication or Missing

如果你获得`FLUSH_DISK_TIMEOUT`，`FLUSH_SLAVE_TIMEOUT`，此刻broker刚好挂掉了，你的消息可能丢失了。此刻，你有两个选择，继续下去，这样会造成消息丢失。另一个重发，会造成重复消息。通常建议重发，并找到一种方法处理重复消息。除非你认为丢失了消息也不要紧。但是牢记，`SLAVE_NOT_AVAILABLE`状态重发是没有用的。此时，你需要保持场景并通知Cluster Manager。

#### Timeout
客户端发送请求给Broker,等到响应超过最大响应时间，没有返回，客户端需要抛出超时异常。默认是3s，可以通过send(msg,timeout)修改。注意，不推荐等待时间太短，broker需要时间刷新磁盘或者同步slave。同时，这个值有帮助在超过syncFlushTimeout很多的时候，broker返回`FLUSH_SLAVE_TIMEOUT`或`FLUSH_SLAVE_TIMEOUT`在超时前

### Consumer
一个Consumer从brokers中拉取所有的消息，并放入应用中。在用户应用方面，提供了两种Consumer:
#### PullConsumer
PullConsumer主动从brokers拉取消息。一旦一个批次的消息被获取，我们的应用就会启动消费过程。
#### PushConsumer
PushConsumer封装了消息的获取，处理过程和维护其他工作，提供了一个回调接口来给用户实现当消息到达时的处理。
#### Consumer Group
和上面的Producer Group差不多，同一角色的Consumer被分成一个组。Consumer Group是一个很好的概念，使得在实现负载均衡，容错，消息处理方面变得很简单。

注意：同一个消费组里面的消费者必须由相同的topic订阅

#### MessageListener
Orderly

消费者会锁住每一个消息队列保证是按顺序消费的，这会造成一些性能损失，但是很有用在关注消息顺序时，不推荐抛出异常，可以返回`ConsumeOrderlyStatus.SUSPEND_CURRENT_QUEUE_A_MOMENT`

Concurrently

并发消费消息，推荐使用，性能较好，但是不推荐抛出异常，你可以返回`ConsumeConcurrentlyStatus.RECONSUME_LATER`

#### Thread Number
consumer内部使用ThreadPoolExecutor来处理消费，你可以通过setConsumeThreadMin或者setConsumeThreadMax改变线程数

#### ConsumeFromWhere
当一个新的Consumer Group创建的时候，你需要决定是否需要broker中存在的消费历史的消息。`CONSUME_FROM_LAST_OFFSET`会忽略历史消息，并消费之后的消息。`CONSUME_FROM_FIRST_OFFSET`会消费所有存在的消息，`CONSUME_FROM_TIMESTAMP`可以指定一个时间戳，消费指定时间之后的消息。 

### Topic
Topic是用户Producer发送消息和Consumer获取消息的类别。可能有0~N个生产者往一个Topic发送消息，0~N个消费群订阅一个Topic。而一个producer又可以往多个Topic发送消息，一个消费群又可能订阅多个Topic。相互直接没有太多束缚。

### Message
消息就是信息的传递，一个消息必须有一个topic，就像发送邮件需要一个地址。消息可以有可选的tag和额外的键值对。例如：你可以在消息上设置一个业务key，在broker服务上查找这个消息诊断开发期间的问题。

#### Message Model
	
	1.Clustering
	2.Broadcasting

#### Message Queue
Topic被分成1~N个子topic，就是消息队列

#### Message Order
当DefaultMQPushConsumer使用的时候，你就要决定消费消息的方式：orderly或者concurrently

1.Orderly

有序消费消息意味着其按照producers每个消息队列发送的顺序进行消费。如果你处理的是全局序列是强制性的场景，确保你使用的topic只有一个消息队列

注意：如果指定为顺序消费，最大的并发消费数是消费组订阅的消息队列的数量

2.Concurrently

并发消费时，最大的并发数只被为每一个消费端设置的最大线程池数量

注意：在这个模式下消息的顺序不进行保证

#### Message Size
我们推荐消息的大小不要超过512K

### Tag
Tag,可以被视为子topic，提供额外的灵活度。使用tag，来自同一个业务模块不同目的的消息可以有相同的topic和不同的tag。tag可以使代码更清晰整洁。tag也能够促进查询系统

### broker
broker使RocketMQ关键的组件，它接收来自producers的消息，保存并准备处理来自consumers获取消息请求。它也保存消息对应的目标数据，包括用户组，消费进度offsets和topic/queue的信息

#### broker role
broker的角色有：`ASYNC_MASTER`，`SYNC_MASTER`，SLAVE。
如果不能容忍消息丢失，使用`SYNC_MASTER`并且添加一个SLAVE，
如果觉得丢失可以，使用`ASYNC_MASTER`加SLAVE，
如果只是想变得简单，可以使用`ASYNC_MASTER`不加SLAVE

FlushDiskType：

推荐`ASYNC_FLUSH`，`SYNC_FLUSH`方式代价过高，太多性能损失，如果想要可靠，推荐`SYNC_MASTER`+SLAVE

### Name Server
NameServer作为路由信息提供者，Producer和Consumer通过这个查找topics的对应broker列表