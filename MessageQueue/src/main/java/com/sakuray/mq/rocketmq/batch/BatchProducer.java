package com.sakuray.mq.rocketmq.batch;

import java.util.ArrayList;
import java.util.List;

import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.common.message.Message;

public class BatchProducer {

	public static void main(String[] args) throws Exception {
		DefaultMQProducer producer = new DefaultMQProducer("simple");
		producer.setNamesrvAddr("localhost:9876");
		producer.start();
		String topic = "BatchTest";
		List<Message> messages = new ArrayList<>();
		messages.add(new Message(topic, "TagA", "OrderID001", "Hello world 0".getBytes()));
		messages.add(new Message(topic, "TagA", "OrderID002", "Hello world 1".getBytes()));
		messages.add(new Message(topic, "TagA", "OrderID003", "Hello world 2".getBytes()));
		producer.send(messages);
		
		// 不知道是否超过1MB，简答的解决方法
		ListSplitter splitter = new ListSplitter(messages);
		while (splitter.hasNext()) {
			List<Message> listItem = splitter.next();
			producer.send(listItem);
		}
		
		producer.shutdown();
	}
}
