package com.microservices.alexandrebn;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import com.microservices.alexandrebn.core.property.JwtConfiguration;

@SpringBootApplication
@EntityScan({ "com.microservices.alexandrebn.core.model" })
@EnableJpaRepositories({ "com.microservices.alexandrebn.core.repository" })
@EnableConfigurationProperties(value = JwtConfiguration.class)
@ComponentScan("com.microservices.alexandrebn")
public class SpringBootMsProjectApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpringBootMsProjectApplication.class, args);
	}
}
