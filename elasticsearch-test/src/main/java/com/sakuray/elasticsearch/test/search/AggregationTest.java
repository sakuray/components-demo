package com.sakuray.elasticsearch.test.search;

import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.bucket.histogram.DateHistogramInterval;
import org.elasticsearch.search.aggregations.bucket.histogram.Histogram;
import org.elasticsearch.search.aggregations.bucket.terms.Terms;
import org.elasticsearch.search.aggregations.bucket.terms.Terms.Bucket;
import org.junit.Test;

import com.sakuray.elasticsearch.test.BaseClient;

/**
 * 聚合查询：https://www.elastic.co/guide/en/elasticsearch/client/java-api/current/java-aggs.html
 * https://www.elastic.co/guide/en/elasticsearch/reference/5.4/search-aggregations.html
 */
public class AggregationTest extends BaseClient {

	// 此功能有点像sql的分组统计
	// buckets 满足某个条件的集合
	// metrics 指标 计数，计算平均值等
	// matrix 多个字段,就是多个条件，挑出满足的
	// pipeline 将多个聚合通过他们的聚合指标聚合在一起
	@Test
	public void test() {
		SearchResponse sr = client.prepareSearch()
				.setQuery(QueryBuilders.matchAllQuery())
				.addAggregation(AggregationBuilders.terms("user").field("_type"))
				.addAggregation(AggregationBuilders.dateHistogram("postDate").field("postDate").dateHistogramInterval(DateHistogramInterval.MINUTE))
				.get();
		// 获取片面结果
		Terms user = sr.getAggregations().get("user");
		Histogram postDate = sr.getAggregations().get("postDate");
		for(Bucket bucket : user.getBuckets()) {
			System.out.println(bucket.getKeyAsString());
		}
		for(org.elasticsearch.search.aggregations.bucket.histogram.Histogram.Bucket bucket : postDate.getBuckets()) {
			System.out.println(bucket.getKeyAsString());
		}
		
	}
}
