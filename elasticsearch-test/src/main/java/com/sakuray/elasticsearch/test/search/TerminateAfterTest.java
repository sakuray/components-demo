package com.sakuray.elasticsearch.test.search;

import org.elasticsearch.action.search.SearchResponse;
import org.junit.Test;

import com.sakuray.elasticsearch.test.BaseClient;

/**
 * 每个shard设置了最大值，如果达到最大值，查询操作会提前中止。如果设置了，你可以检查操作是否提前中止，通过isTerminatedEarly
 * https://www.elastic.co/guide/en/elasticsearch/client/java-api/current/java-search-terminate-after.html
 */
public class TerminateAfterTest extends BaseClient {

	@Test
	public void test() {
		SearchResponse sr = client.prepareSearch("twitter")
				.setTerminateAfter(2)
				.get();
		if(sr.isTerminatedEarly()) {
			System.out.println("到达搜索数量，提前中止搜索");
		}
	}
}
