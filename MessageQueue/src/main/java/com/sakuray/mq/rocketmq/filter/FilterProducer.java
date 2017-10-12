package com.sakuray.mq.rocketmq.filter;

import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.remoting.common.RemotingHelper;

public class FilterProducer {

	public static void main(String[] args) throws Exception {
		DefaultMQProducer producer = new DefaultMQProducer("filter");
		producer.setNamesrvAddr("localhost:9876");
		producer.start();
		for(int i = 0; i < 5; i++) {
			Message msg = new Message("TopicTest", "filter",
					("Hello RocketMQ " + i).getBytes(RemotingHelper.DEFAULT_CHARSET));
			msg.putUserProperty("a", String.valueOf(i)); // filter设置
			SendResult sendResult = producer.send(msg);
			System.out.println(sendResult);
		}
		producer.shutdown();
	}
}
