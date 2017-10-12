package com.sakuray.mq.rocketmq.filter;

import java.io.UnsupportedEncodingException;
import java.util.List;

import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyContext;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import org.apache.rocketmq.client.consumer.listener.MessageListenerConcurrently;
import org.apache.rocketmq.common.MixAll;
import org.apache.rocketmq.common.message.MessageExt;
import org.apache.rocketmq.remoting.common.RemotingHelper;

public class FilterConsumer {

	public static void main(String[] args) throws Exception {
		DefaultMQPushConsumer consumer = new DefaultMQPushConsumer("filter");
        consumer.setNamesrvAddr("localhost:9876");
//        consumer.subscribe("TopicTest", MessageSelector.bySql("a between 0 and 3")); // filter 订阅
        String path = FilterConsumer.class.getClassLoader().getResource("MyFilter.java").getPath();
        String filterCode = MixAll.file2String(path); 
        System.out.println(path);
        System.out.println(filterCode);
        consumer.subscribe("TopicTest", "com.sakuray.mq.rocketmq.filter.MyFilter", filterCode);
        consumer.registerMessageListener(new MessageListenerConcurrently() {
			@Override
			public ConsumeConcurrentlyStatus consumeMessage(List<MessageExt> msgs, ConsumeConcurrentlyContext context) {
				for(MessageExt msg : msgs) {
					try {
						System.out.println(new String(msg.getBody(),RemotingHelper.DEFAULT_CHARSET));
					} catch (UnsupportedEncodingException e) {
						e.printStackTrace();
					}
				}
				return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
			}
		});
        consumer.start();
        System.out.println("OVER");
	}
}
