package com.sakuray.elasticsearch.test.search;

import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.search.SearchType;
import org.elasticsearch.index.query.QueryBuilders;
import org.junit.Test;

import com.sakuray.elasticsearch.test.BaseClient;

/**
 * 查询的API
 * https://www.elastic.co/guide/en/elasticsearch/client/java-api/current/java-search.html
 */
public class SearchAPI extends BaseClient {

	@Test
	public void test() {
		// 最短的search，查询所有
		SearchResponse response = client.prepareSearch().get();
		System.out.println(response.toString());
	}
	
	@Test
	public void test1() {
		try {
			// 不能填不存在的索引
			SearchResponse response = client.prepareSearch("twitter", "log4j-2017.06.14")
					.setTypes("tweet", "log4j_type")
					.setSearchType(SearchType.DFS_QUERY_THEN_FETCH)
					.setQuery(QueryBuilders.termQuery("user", "kimchy"))	// 查询
					.setPostFilter(QueryBuilders.prefixQuery("message", "trying"))
					.setFrom(0).setSize(60).setExplain(true)
					.get();
			System.out.println(response.toString());
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
}
