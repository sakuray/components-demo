package com.sakuray.elasticsearch.test.document;

import java.io.IOException;
import java.util.Date;
import java.util.Iterator;

import org.elasticsearch.action.bulk.BulkItemResponse;
import org.elasticsearch.action.bulk.BulkRequestBuilder;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.common.xcontent.XContentFactory;
import org.junit.Test;

import com.sakuray.elasticsearch.test.BaseClient;

/**
 * 允许在一个请求中添加或删除多个document
 * https://www.elastic.co/guide/en/elasticsearch/client/java-api/current/java-docs-bulk.html
 */
public class BulkAPI extends BaseClient{

	@Test
	public void test() throws IOException {
		BulkRequestBuilder bulkRequest = client.prepareBulk();
		bulkRequest.add(client.prepareIndex("twitter", "tweet", "6")
		        .setSource(XContentFactory.jsonBuilder()
		                    .startObject()
		                        .field("user", "kimchy")
		                        .field("postDate", new Date())
		                        .field("message", "trying out Elasticsearch")
		                    .endObject()
		                  )
		        );

		bulkRequest.add(client.prepareIndex("twitter", "tweet", "7")
		        .setSource(XContentFactory.jsonBuilder()
		                    .startObject()
		                        .field("user", "kimchy")
		                        .field("postDate", new Date())
		                        .field("message", "another post")
		                    .endObject()
		                  )
		        );

		BulkResponse bulkResponse = bulkRequest.get();
		if (bulkResponse.hasFailures()) {
		    // process failures by iterating through each bulk response item
		} 
		Iterator<BulkItemResponse> it = bulkResponse.iterator();
		BulkItemResponse bir;
		while(it.hasNext()) {
			bir = it.next();
			System.out.println(bir.getId());
		}
	}
}
