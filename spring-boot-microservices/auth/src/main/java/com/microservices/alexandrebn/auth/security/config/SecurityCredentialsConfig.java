package com.microservices.alexandrebn.auth.security.config;

import javax.servlet.Filter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.BeanIds;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.microservices.alexandrebn.auth.security.filter.JwtUsernameAndPasswordAuthenticationFilter;
import com.microservices.alexandrebn.token.config.SecurityTokenConfig;
import com.microservices.alexandrebn.token.converter.TokenConverter;
import com.microservices.alexandrebn.token.creator.TokenCreator;
import com.microservices.alexandrebn.token.filter.JwtTokenAuthorizationFilter;

@EnableWebSecurity
public class SecurityCredentialsConfig extends SecurityTokenConfig {

	@Autowired
	private TokenCreator tokenCreator;

	@Autowired
	private TokenConverter tokenConverter;

	@Autowired
	private UserDetailsService userDetailsService;

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http
			.addFilter(jwtUsernameAndPasswordAuthenticationFilter())
			.addFilterAfter(new JwtTokenAuthorizationFilter(jwtConfiguration, tokenConverter), UsernamePasswordAuthenticationFilter.class);

		super.configure(http);
	}

	private Filter jwtUsernameAndPasswordAuthenticationFilter() throws Exception {
		return new JwtUsernameAndPasswordAuthenticationFilter(authenticationManagerBean(), jwtConfiguration, tokenCreator);
	}

	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder());
	}

	@Bean
	public BCryptPasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Bean(name = BeanIds.AUTHENTICATION_MANAGER)
	@Override
	public AuthenticationManager authenticationManagerBean() throws Exception {
		return super.authenticationManagerBean();
	}
}
