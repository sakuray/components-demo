package com.sakuray.elasticsearch.test.document;

import org.elasticsearch.action.delete.DeleteResponse;
import org.junit.Test;

import com.sakuray.elasticsearch.test.BaseClient;

/**
 * 根据指定的index和id删除一个json document
 * https://www.elastic.co/guide/en/elasticsearch/client/java-api/current/java-docs-delete.html
 * https://www.elastic.co/guide/en/elasticsearch/reference/5.4/docs-delete.html
 */
public class DeleteAPI extends BaseClient {
	
	/**
			{
			    "_shards" : {
			        "total" : 10,
			        "failed" : 0,
			        "successful" : 10
			    },
			    "found" : true,
			    "_index" : "twitter",
			    "_type" : "tweet",
			    "_id" : "1",
			    "_version" : 2,
			    "result": "deleted"
			}
	 */
	@Test
	public void test() {
		DeleteResponse response = client.prepareDelete("twitter", "tweet", "1").get();
		System.out.println(response.status());
	}

}
