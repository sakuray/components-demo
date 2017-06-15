package com.sakuray.elasticsearch.test.search;

import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.common.unit.TimeValue;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.sort.FieldSortBuilder;
import org.elasticsearch.search.sort.SortOrder;
import org.junit.Test;

import com.sakuray.elasticsearch.test.BaseClient;

/**
 * scroll介绍：https://www.elastic.co/guide/en/elasticsearch/reference/5.4/search-request-scroll.html
 * https://www.elastic.co/guide/en/elasticsearch/client/java-api/current/java-search-scrolling.html
 */
public class ScrollTest extends BaseClient{

	// 一个search请求通常返回一页数据，但是scroll API可以被用于获取大量数据甚至是全部数据，就像使用游标一样
	// scroll不是用于用户的实时请求，而是为了处理大量的数据
	// 比如为了重置一个索引的内容到一个新的索引使用不同的配置
	
	// scroll返回的结果会同时影响所以的状态，一旦一个初始search请求创建，像一个快照。之后对文档的改变只会影响之后的search请求
	// 第一次需要index 和type 之后会拿到一个返回ID，用于下一次，像游标位置一样。注意每次最好释放上一次的资源
	
	@Test
	public void test() {
		QueryBuilder qb = QueryBuilders.termQuery("user", "kimchy");
		SearchResponse scrollResp = client.prepareSearch("twitter")
				.addSort(FieldSortBuilder.DOC_FIELD_NAME, SortOrder.ASC)
				.setScroll(new TimeValue(60000))
				.setQuery(qb)
				.setSize(10).get();
		// scroll until no hits return
		do{
			System.out.println("-----获取一批数据-----");
			for(SearchHit hit : scrollResp.getHits().getHits()) {
				// 处理hit
				System.out.println(hit.getSourceAsString());
			}
			scrollResp = client.prepareSearchScroll(scrollResp.getScrollId()).setScroll(new TimeValue(60000)).execute().actionGet();
		} while(scrollResp.getHits().getHits().length != 0);
	}
}
