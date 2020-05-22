package com.microservices.alexandrebn.gateway.security.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.microservices.alexandrebn.gateway.security.filter.GatewayJwtTokenAuthorizationFilter;
import com.microservices.alexandrebn.token.config.SecurityTokenConfig;
import com.microservices.alexandrebn.token.converter.TokenConverter;

@EnableWebSecurity
public class SecurityConfig extends SecurityTokenConfig {

	@Autowired
	private TokenConverter tokenConverter;
	
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.addFilterAfter(new GatewayJwtTokenAuthorizationFilter(jwtConfiguration, tokenConverter), UsernamePasswordAuthenticationFilter.class);
		super.configure(http);
	}
}
