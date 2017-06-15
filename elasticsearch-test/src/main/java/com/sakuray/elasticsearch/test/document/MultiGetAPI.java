package com.sakuray.elasticsearch.test.document;

import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.get.MultiGetItemResponse;
import org.elasticsearch.action.get.MultiGetResponse;
import org.junit.Test;

import com.sakuray.elasticsearch.test.BaseClient;

/**
 * 获得一系列的documents
 * https://www.elastic.co/guide/en/elasticsearch/client/java-api/current/java-docs-multi-get.html
 * https://www.elastic.co/guide/en/elasticsearch/reference/5.4/docs-multi-get.html
 */
public class MultiGetAPI extends BaseClient {

	@Test
	public void test() {
		MultiGetResponse multiGetResponse = client.prepareMultiGet()
				.add("twitter", "tweet", "1")					// 获取一个document
				.add("twitter", "tweet", "2", "3", "4")			// 获取多个
				.add("another", "type", "foo")					// 获取其它索引的
				.get();
		
		for (MultiGetItemResponse itemResponse : multiGetResponse) { 
		    GetResponse response = itemResponse.getResponse();
		    if(response == null) {
		    	System.out.println("有不存在的索引");
		    	continue;
		    }
		    if (response.isExists()) {                      
		        String json = response.getSourceAsString(); 
		        System.out.println(json);
		    } else {
		    	System.out.println(response.getIndex() + ":" + response.getType()
		    		+ ":" + response.getId() +"不存在");
		    }
		}
	}
}
