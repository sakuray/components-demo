package com.sakuray.elasticsearch.test.search;

import org.elasticsearch.action.search.MultiSearchResponse;
import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.index.query.QueryBuilders;
import org.junit.Test;

import com.sakuray.elasticsearch.test.BaseClient;

/**
 * 多条件查询：https://www.elastic.co/guide/en/elasticsearch/reference/5.4/search-multi-search.html
 * https://www.elastic.co/guide/en/elasticsearch/client/java-api/current/java-search-msearch.html
 */
public class MultiSearchAPI extends BaseClient {
	
	@Test
	public void test() {
		SearchRequestBuilder srb1 = client.prepareSearch()
				.setQuery(QueryBuilders.queryStringQuery("main")).setSize(1);
		SearchRequestBuilder srb2 = client.prepareSearch()
				.setQuery(QueryBuilders.matchQuery("user", "kimchy")).setSize(1);
		
		MultiSearchResponse sr = client.prepareMultiSearch()
				.add(srb1)
				.add(srb2)
				.get();
		long nbHits = 0;
		for(MultiSearchResponse.Item item: sr.getResponses()) {
			SearchResponse response = item.getResponse();
			nbHits += response.getHits().getTotalHits();
			System.out.println(nbHits);
		}
	}
}
