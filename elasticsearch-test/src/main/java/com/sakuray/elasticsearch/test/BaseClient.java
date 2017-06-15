package com.sakuray.elasticsearch.test;

import java.net.InetSocketAddress;

import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.elasticsearch.transport.client.PreBuiltTransportClient;
import org.junit.After;
import org.junit.Before;

public class BaseClient {
	
	protected TransportClient client;

	@SuppressWarnings("resource")
	@Before
	public void getClient() {
		Settings settings = Settings.builder()
				.put("cluster.name", "elasticsearch")	// 设置连接的集群名称
				.put("client.transport.sniff", true)	// 设置网络嗅探，会将集群中的ip自动添加到客户端，并且有新的ip加入时也会添加
				.build();
		client = new PreBuiltTransportClient(settings)
				.addTransportAddress(new InetSocketTransportAddress(new InetSocketAddress("127.0.0.1", 9300)));
	}
	
	@After
	public void closeClient() {
		client.close();
	}
}
