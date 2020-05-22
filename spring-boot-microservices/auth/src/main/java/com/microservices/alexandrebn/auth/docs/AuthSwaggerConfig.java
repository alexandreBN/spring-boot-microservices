package com.microservices.alexandrebn.auth.docs;

import org.springframework.context.annotation.Configuration;

import com.microservices.alexandrebn.core.docs.BaseSwaggerConfig;

import springfox.documentation.swagger2.annotations.EnableSwagger2;

@Configuration
@EnableSwagger2
public class AuthSwaggerConfig extends BaseSwaggerConfig {

	public AuthSwaggerConfig() {
		super("com.microservices.alexandrebn.auth.endpoint.controller");
	}
	
}
