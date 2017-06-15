package com.sakuray.elasticsearch.test.client;

import java.net.InetSocketAddress;

import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.elasticsearch.transport.client.PreBuiltTransportClient;
import org.junit.Test;

public class TransportClientTest {
	
	/**
	 * TransportClient连接远程的cluster是通过transport模块。
	 */
	@SuppressWarnings("resource")
	@Test
	public void test() {
		/**
		 * client.transport.ignore_cluster_name  true忽略连接的node的cluster名称校验
		 * client.transport.ping_timeout 		    等待一个node结果返回的时间，默认5s
		 * client.transport.nodes_sampler_interval  sample或ping node lists的周期，默认5s
		 */
		Settings settings = Settings.builder()
				.put("cluster.name", "elasticsearch")	// 设置连接的集群名称
				.put("client.transport.sniff", true)	// 设置网络嗅探，会将集群中的ip自动添加到客户端，并且有新的ip加入时也会添加
				.build();
		
		TransportClient client = new PreBuiltTransportClient(settings)
				.addTransportAddress(new InetSocketTransportAddress(new InetSocketAddress("127.0.0.1", 9200)));
		System.out.println(client.nodeName());
		client.close();
	}

}
