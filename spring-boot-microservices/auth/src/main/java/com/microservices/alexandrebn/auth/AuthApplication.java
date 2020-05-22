package com.microservices.alexandrebn.auth;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import com.microservices.alexandrebn.core.property.JwtConfiguration;

@SpringBootApplication
@EnableConfigurationProperties(value = JwtConfiguration.class)
@EntityScan({ "com.microservices.alexandrebn.core.model" })
@EnableJpaRepositories({ "com.microservices.alexandrebn.core.repository" })
@ComponentScan("com.microservices.alexandrebn")
@EnableEurekaClient
public class AuthApplication {
	public static void main(String[] args) {
		SpringApplication.run(AuthApplication.class, args);
	}
}
