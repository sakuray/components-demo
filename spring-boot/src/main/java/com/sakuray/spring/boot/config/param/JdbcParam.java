package com.sakuray.spring.boot.config.param;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

/**
 * 读取jdbc.properties配置文件
 */
@Component
@PropertySource(value={"classpath:jdbc.properties"})
@ConfigurationProperties(prefix="jdbc")
public class JdbcParam {

	private String driver;
	private String url;
	private String username;
	private String password;
	
	public String getDriver() {
		return driver;
	}
	public void setDriver(String driver) {
		this.driver = driver;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	
	@Override
	public String toString() {
		return "JdbcParam [driver=" + driver + ", url=" + url + ", username=" + username + ", password=" + password
				+ "]";
	}
}
