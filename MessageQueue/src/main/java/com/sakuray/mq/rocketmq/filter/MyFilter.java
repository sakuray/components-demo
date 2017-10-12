package com.sakuray.mq.rocketmq.filter;

import org.apache.rocketmq.common.filter.FilterContext;
import org.apache.rocketmq.common.filter.MessageFilter;
import org.apache.rocketmq.common.message.MessageExt;

public class MyFilter implements MessageFilter {

	@Override
	public boolean match(MessageExt msg, FilterContext context) {
		String property = msg.getUserProperty("a");
		if(property != null) {
			int id = Integer.parseInt(property);
			if( id >= 0 && id <= 3) {
				return true;
			}
		}
		return false;
	}

}
