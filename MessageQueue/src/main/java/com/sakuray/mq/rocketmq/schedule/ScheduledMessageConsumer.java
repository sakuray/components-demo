package com.sakuray.mq.rocketmq.schedule;

import java.util.List;

import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyContext;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import org.apache.rocketmq.client.consumer.listener.MessageListenerConcurrently;
import org.apache.rocketmq.common.message.MessageExt;

public class ScheduledMessageConsumer {

	public static void main(String[] args) throws Exception {
		DefaultMQPushConsumer consumer = new DefaultMQPushConsumer("schedule");
		consumer.subscribe("TestTopic", "*");
		consumer.setNamesrvAddr("localhost:9876");
		consumer.registerMessageListener(new MessageListenerConcurrently() {
			@Override
			public ConsumeConcurrentlyStatus consumeMessage(List<MessageExt> msgs, ConsumeConcurrentlyContext context) {
				 for (MessageExt message : msgs) {
	                    // Print approximate delay time period
	                    System.out.println("Receive message[msgId=" + message.getMsgId() + "] "
	                            + (System.currentTimeMillis() - message.getStoreTimestamp()) + "ms later");
	                }
	                return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
			}
        });
		consumer.start();
		System.out.println("start");
	}
}
