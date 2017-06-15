package com.sakuray.elasticsearch.test.document;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.common.xcontent.XContentFactory;
import org.elasticsearch.rest.RestStatus;
import org.junit.Test;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sakuray.elasticsearch.test.BaseClient;

/**
 * index api允许为在一个特定的index中搜索一个指定的json片段
 * https://www.elastic.co/guide/en/elasticsearch/client/java-api/current/java-docs-index.html#java-docs-index-thread
 * https://www.elastic.co/guide/en/elasticsearch/reference/5.4/docs-index_.html
 */
public class IndexAPI extends BaseClient {
	
	/**
	 * 生成JSON 片段的方式
	 */
	// 1.一般方法：使用byte[]或String
	public String manually() {
		String json = "{" + 
				"\"user\":\"kimchy\"," +
				"\"postDate\":\"2013-01-30\"," +
				"\"message\":\"trying out Elasticsearch\"" +
				"}";
		return json;
	}

	// 2.使用map
	public Map<String, Object> map() {
		Map<String, Object> json = new HashMap<String, Object>();
		json.put("user", "kimchy");
		json.put("postDate", new Date());
		json.put("message", "trying out Elasticsearch");
		return json;
	}
	
	// 3.序列化bean
	public byte[] serialize() {
		ObjectMapper mapper = new ObjectMapper();
		byte[] json = null;
		try {
			json = mapper.writeValueAsBytes(new query("kimchy", new Date(), "trying out Elasticsearch"));
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
		return json;
	}
	
	// 4.使用elasticsearch helpers
	public XContentBuilder helpers() {
		XContentBuilder builder = null;
		try {
			builder = XContentFactory.jsonBuilder()
					.startObject()								// 也有startArray方法  endArray
						.field("user", "kimchy")
						.field("postDate", new Date())
						.field("message", "trying out Elasticsearch")
					.endObject();
			System.out.println("helpers构建的json是:"+builder.string());
		} catch (IOException e) {
			e.printStackTrace();
		}
		return builder;
	}
	
	@SuppressWarnings("unused")
	private class query {
		private String user;
		private Date postDate;
		private String message;
		
		public query(String user, Date postDate, String message) {
			this.user = user;
			this.postDate = postDate;
			this.message = message;
		}
		
		public String getUser() {
			return user;
		}
		public void setUser(String user) {
			this.user = user;
		}
		public Date getPostDate() {
			return postDate;
		}
		public void setPostDate(Date postDate) {
			this.postDate = postDate;
		}
		public String getMessage() {
			return message;
		}
		public void setMessage(String message) {
			this.message = message;
		}
		
	}
	
	
	@Test
	public void test() {
		IndexResponse response = client.prepareIndex("twitter", "tweet", "2")
				.setSource(helpers())		// 这个是往里面写一个数据
				.get();						// 获取写的结果
		/**
		 * 注意使用json string的话，不需要给id
		 */
		/*IndexResponse response2 = client.prepareIndex("log4j-2017.06.14", "log4j_type")
				.setSource(manually())
				.get();*/
		
		
		/**
			{
			    "_shards" : {
			        "total" : 2,			// 一共进行了多少次shard copies（primary and replica shards）操作了
			        "failed" : 0,			// 
			        "successful" : 2		// 成功次数，至少是1
			    },
			    "_index" : "twitter",
			    "_type" : "tweet",
			    "_id" : "1",
			    "_version" : 1,
			    "created" : true,
			    "result" : created
			}
		 */
		String _index = response.getIndex();
		String _type = response.getType();
		String _id = response.getId();
		long _version = response.getVersion();
		RestStatus status = response.status();
		System.out.println(_index+
				">>" + _type +
				">>" + _id +
				">>" + _version +
				">>" + status);
	}
}
