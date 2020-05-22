package com.microservices.alexandrebn.core.property;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.NestedConfigurationProperty;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "jwt.config")
public class JwtConfiguration {

	private String loginUrl = "/login/**";
	
	@NestedConfigurationProperty
	private Header header = new Header();
	
	private int expiration = 3600;
	private String privateKey = "bq7i9TaQmvFB7QTKhZQYUj01nwn94AQP";
	private String type = "encrypted";

	public String getLoginUrl() {
		return loginUrl;
	}

	public void setLoginUrl(String loginUrl) {
		this.loginUrl = loginUrl;
	}

	public Header getHeader() {
		return header;
	}

	public void setHeader(Header header) {
		this.header = header;
	}

	public int getExpiration() {
		return expiration;
	}
	
	public String getPrivateKey() {
		return privateKey;
	}
	
	public String getType() {
		return type;
	}
	
	public void setType(String type) {
		this.type = type;
	}

	public static class Header {
		private String name = "Authorization";
		private String prefix = "Bearer ";
		
		public String getName() {
			return name;
		}
		
		public String getPrefix() {
			return prefix;
		}
	}
}
