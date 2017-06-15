package com.sakuray.elasticsearch.test.document;

import java.io.IOException;

import org.elasticsearch.action.ActionListener;
import org.elasticsearch.action.bulk.byscroll.BulkByScrollResponse;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.reindex.DeleteByQueryAction;
import org.junit.Test;

import com.sakuray.elasticsearch.test.BaseClient;

/**
 * 基于一个查询的结果，删除这个结果里面所给的一系列document
 * https://www.elastic.co/guide/en/elasticsearch/client/java-api/current/java-docs-delete-by-query.html
 */
public class DeleteByQuery extends BaseClient {

	@Test
	public void test() {
		BulkByScrollResponse response = DeleteByQueryAction.INSTANCE.newRequestBuilder(client)
				.filter(QueryBuilders.matchQuery("user", "kimchy"))		// 1.构建查询条件
				.source("twitter")										// 2.查询的索引,没有这个索引就会报异常
				.get();													// 3.执行操作
		
		long deleted = response.getDeleted();							// 4.删除的记录数
		System.out.println(deleted);
	}
	
	@Test
	public void test2() throws IOException {
		// 删除操作可能耗时很久，要使用异步处理，可以提供一个监听器
		DeleteByQueryAction.INSTANCE.newRequestBuilder(client)
	    	.filter(QueryBuilders.matchQuery("user", "kimchy"))                  
	    	.source("twitter")
	    	.execute(new ActionListener<BulkByScrollResponse>() {

				@Override
				public void onResponse(BulkByScrollResponse response) {
					long deleted = response.getDeleted();
					System.out.println(deleted);
				}
	
				@Override
				public void onFailure(Exception e) {
					
				}
			});
		System.in.read();
	}
}
