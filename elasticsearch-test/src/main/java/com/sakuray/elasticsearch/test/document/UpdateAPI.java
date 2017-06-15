package com.sakuray.elasticsearch.test.document;

import java.io.IOException;
import java.util.HashMap;
import java.util.concurrent.ExecutionException;

import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.action.update.UpdateResponse;
import org.elasticsearch.common.xcontent.XContentFactory;
import org.elasticsearch.script.Script;
import org.elasticsearch.script.ScriptType;
import org.junit.Test;

import com.sakuray.elasticsearch.test.BaseClient;

/**
 * 更新或插入一条记录document
 * https://www.elastic.co/guide/en/elasticsearch/client/java-api/current/java-docs-update.html
 */
public class UpdateAPI extends BaseClient {

	@Test
	public void test() throws IOException, InterruptedException, ExecutionException {
		// 通过构建UpdateRequest
		UpdateRequest updateRequest = new UpdateRequest();
		updateRequest.index("twitter");
		updateRequest.type("tweet");
		updateRequest.id("1");
		updateRequest.doc(XContentFactory.jsonBuilder()
		        .startObject()
		            .field("message", "are you ok!")
		        .endObject());
		UpdateResponse response = client.update(updateRequest).get();
		System.out.println(response.status());
	}
	
	@Test
	public void test2() {
		try {
			// 如果是一个服务器上的脚本， 类型是ScriptType.FILE
			// 测试有点问题，没有修改成功
			UpdateResponse response = client.prepareUpdate("twitter", "tweet", "1")
					.setScript(new Script(ScriptType.INLINE, "mustache", "ctx._source.message = \"I'm ok!!\"",
							new HashMap<String, Object>()))
					.get();
			System.out.println(response.status());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	// doc与script不能同时使用
	@Test
	public void test3() throws IOException {
		UpdateResponse response = client.prepareUpdate("twitter", "tweet", "1")
				.setDoc(XContentFactory.jsonBuilder()
						.startObject()
							.field("message", "lucky")
						.endObject())
				.get();
		System.out.println(response.status());
	}
	
	// 更新插入 1.如果更新目标updateRequest不存在，就创建indexRequest，存在就更新updateRequest
	// 判断存在以updaterequest为主，即时indexRequest的编号不一样，只要不存在就存indexRequest。若存在就和indexRequest没关系了
	@Test
	public void test4() throws IOException, InterruptedException, ExecutionException {
		IndexRequest indexRequest = new IndexRequest("twitter", "tweet", "4")
				.source(XContentFactory.jsonBuilder()
						.startObject()
							.field("gender", "man")
							.field("message", "Joe Smith")
						.endObject());
		
		UpdateRequest updateRequest = new UpdateRequest("twitter", "tweet", "5")
				.doc(XContentFactory.jsonBuilder()
						.startObject()
							.field("message", "new operation")
						.endObject())
				.upsert(indexRequest);
		
		UpdateResponse response = client.update(updateRequest).get();
		System.out.println(response.status());
	}
}
