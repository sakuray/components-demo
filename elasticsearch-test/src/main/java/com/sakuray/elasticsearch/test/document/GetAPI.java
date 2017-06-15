package com.sakuray.elasticsearch.test.document;

import org.elasticsearch.action.get.GetResponse;
import org.junit.Test;

import com.sakuray.elasticsearch.test.BaseClient;

/**
 * 获取json document
 * https://www.elastic.co/guide/en/elasticsearch/client/java-api/current/java-docs-get.html
 */
public class GetAPI extends BaseClient {

	// client的操作都是异步的，获取的时候默认情况下是在另一个线程进行的，如果需要在此线程中执行，需要设置operationThreaded为false
	@Test
	public void test() {
		GetResponse response = client.prepareGet("twitter", "tweet", "1")
				.setOperationThreaded(false)
				.get();
		System.out.println(response.getSourceAsString());
	}
}
