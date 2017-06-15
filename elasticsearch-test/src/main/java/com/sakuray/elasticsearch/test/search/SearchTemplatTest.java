package com.sakuray.elasticsearch.test.search;

import java.util.HashMap;
import java.util.Map;

import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.common.bytes.BytesArray;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.script.ScriptType;
import org.elasticsearch.script.mustache.SearchTemplateRequestBuilder;
import org.junit.Test;

import com.sakuray.elasticsearch.test.BaseClient;

/**
 * 搜索模板：https://www.elastic.co/guide/en/elasticsearch/reference/5.4/search-template.html
 */
public class SearchTemplatTest extends BaseClient {
	
	/**
	 需要在elasticsearch目录下config/scripts/template_gender.mustache文件。内容如下
	{
	    "template" : {
	        "query" : {
	            "match" : {
	                "gender" : "{{param_gender}}"
	            }
	        }
	    }
	}
	 */
	@Test
	public void test() {
		Map<String, Object> template_params = new HashMap<>();
		template_params.put("param_gender", "male");	// 模板中替换的字段
		SearchResponse sr = new SearchTemplateRequestBuilder(client)
			    .setScript("template_gender")  		// 脚本名称               
			    .setScriptType(ScriptType.FILE) 	// 文件类型
			    .setScriptParams(template_params)   // 替换参数         
			    .setRequest(new SearchRequest())    // 设置搜索请求          
			    .get()                                        
			    .getResponse();  
		System.out.println(sr.toString());
	}
	
	
	@Test
	public void test2() {
		// store your template in the cluster state:
		client.admin().cluster().preparePutStoredScript()
			.setLang("mustache")
			.setId("template_gender")
			.setContent(new BytesArray(
	        "{\n" +
	        "    \"template\" : {\n" +
	        "        \"query\" : {\n" +
	        "            \"match\" : {\n" +
	        "                \"user\" : \"{{param_user}}\"\n" +
	        "            }\n" +
	        "        }\n" +
	        "    }\n" +
	        "}"), XContentType.JSON).get();
		
		Map<String, Object> template_params = new HashMap<>();
		template_params.put("param_user", "kimchy");
		
		SearchResponse sr = new SearchTemplateRequestBuilder(client)
		        .setScript("template_gender")                       
		        .setScriptType(ScriptType.STORED)     
		        .setScriptParams(template_params)                   
		        .setRequest(new SearchRequest())                    
		        .get()                                              
		        .getResponse();
		System.out.println(sr.toString());
	}
	
	@Test
	public void test3() {
		Map<String, Object> template_params = new HashMap<>();
		template_params.put("param_user", "kimchy");
		// You can also execute inline templates
		try {
			SearchResponse sr = new SearchTemplateRequestBuilder(client)
					.setScript(
					"{\n" +                                  
					"        \"query\" : {\n" +
					"            \"match\" : {\n" +
					"                \"user\" : \"{{param_user}}\"\n" +
					"            }\n" +
					"        }\n" +
					"}")
					.setScriptType(ScriptType.INLINE)
					.setScriptParams(template_params)
					.setRequest(new SearchRequest())
					.get()
					.getResponse();
			System.out.println(sr.toString());
		} catch(Exception e) {
			e.printStackTrace();
		}
		
	}
}
