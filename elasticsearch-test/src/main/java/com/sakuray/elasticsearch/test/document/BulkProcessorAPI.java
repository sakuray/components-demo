package com.sakuray.elasticsearch.test.document;

import java.io.IOException;
import java.util.Date;

import org.elasticsearch.action.bulk.BackoffPolicy;
import org.elasticsearch.action.bulk.BulkProcessor;
import org.elasticsearch.action.bulk.BulkProcessor.Listener;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.delete.DeleteRequest;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.common.unit.ByteSizeUnit;
import org.elasticsearch.common.unit.ByteSizeValue;
import org.elasticsearch.common.unit.TimeValue;
import org.elasticsearch.common.xcontent.XContentFactory;
import org.junit.Test;

import com.sakuray.elasticsearch.test.BaseClient;

/**
 * https://www.elastic.co/guide/en/elasticsearch/client/java-api/current/java-docs-bulk-processor.html
 */
public class BulkProcessorAPI extends BaseClient {

	@Test
	public void test() throws IOException {
		BulkProcessor bulkProcessor = BulkProcessor.builder(client, new Listener() {	// 添加客户端
			
			@Override
			public void beforeBulk(long executionId, BulkRequest request) {
				System.out.println("准备中"+executionId+":"+request.numberOfActions());	// bulk执行前
			}
			
			@Override
			public void afterBulk(long executionId, BulkRequest request, Throwable failure) {
				System.out.println("完成有失败"+executionId+":"+request.numberOfActions());		// 执行失败抛出异常
			}
			
			@Override
			public void afterBulk(long executionId, BulkRequest request, BulkResponse response) {
				System.out.println("完成成功"+executionId+":"+request.numberOfActions());		// 可以检查response是否有失败 response.hasFailures()
			}
		})
		.setBulkActions(10000)	// 10000给请求执行一次，默认1000
		.setBulkSize(new ByteSizeValue(5, ByteSizeUnit.MB))	// 每5MBflush一次字节  默认5mb
		.setFlushInterval(TimeValue.timeValueSeconds(5))	// 5sflush一次，不管request的数量 默认没有
		.setConcurrentRequests(0)	//0 意味单线程 1为一个并发被允许
		.setBackoffPolicy(BackoffPolicy.exponentialBackoff(TimeValue.timeValueMillis(100), 3))
		.build();
		
		bulkProcessor.add(new DeleteRequest("twitter", "tweet", "1"));
		bulkProcessor.add(new IndexRequest("twitter", "tweet", "1").source(XContentFactory.jsonBuilder()
				.startObject()								// 也有startArray方法  endArray
				.field("user", "kimchy")
				.field("postDate", new Date())
				.field("message", "trying out Elasticsearch")
			.endObject()));
		
		bulkProcessor.flush();
//		bulkProcessor.awaitClose(10, TimeUnit.MINUTES);
		bulkProcessor.close();	// 会刷入所有剩余的请求，且对其它周期flush隐藏，如果配置了flushInterval
		
		client.admin().indices().prepareRefresh().get();
		SearchResponse response = client.prepareSearch().get();
		System.out.println(response.toString());
	}
}
