package com.sakuray.mq.rocketmq.simple;

import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.remoting.common.RemotingHelper;

public class OnewayProducer {

	public static void main(String[] args) throws Exception {
		DefaultMQProducer producer = new DefaultMQProducer("simple");
		producer.setNamesrvAddr("localhost:9876");
		producer.start();
		for (int i = 0; i < 100; i++) {
			Message msg = new Message("TopicTest", "TagA", ("Hello RocketMQ" + i).getBytes(RemotingHelper.DEFAULT_CHARSET));
			producer.sendOneway(msg);
		}
		producer.shutdown();
	}
}
