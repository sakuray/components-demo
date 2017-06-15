# elasticsearch的Java API使用的研究示例
	此例子基于elasticsearch5.4.1,Java8
这个是环境搭建[链接](https://www.elastic.co/cn/downloads/elasticsearch)

## 概述

点这里是[官方说明](https://www.elastic.co/guide/en/elasticsearch/client/java-api/current/java-api.html)

所有的elasticsearch操作都是通过使用一个客户端client对象完成的。通常上所有操作都是异步的，可能是通过一个listener监听器，或者是返回一个future。

此外，通过client的操作可以被累积并通过[bulk](https://www.elastic.co/guide/en/elasticsearch/client/java-api/current/java-docs-bulk.html)进操作。

你可以通过java client做很多事情：
* 在一个现有的cluster上执行标准的索引，获取，删除和查询操作
* 在一个运行中的cluster上执行管理任务

获得client十分简单，最常用的方法是创建一个TransportClient，连接上一个cluster。

**注意：**client的版本要与node的版本一致，此例子使用了5.4.1版本的elasticsearch，api版本也是这个。

简介就到这里了，具体使用依旧可以看上面所给的官方说明，本文也会对其进行测试。