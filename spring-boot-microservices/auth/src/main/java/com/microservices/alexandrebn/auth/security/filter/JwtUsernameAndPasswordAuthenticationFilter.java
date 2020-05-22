package com.microservices.alexandrebn.auth.security.filter;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.Collections;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.microservices.alexandrebn.core.model.ApplicationUser;
import com.microservices.alexandrebn.core.property.JwtConfiguration;
import com.microservices.alexandrebn.token.creator.TokenCreator;
import com.nimbusds.jose.JOSEException;
import com.nimbusds.jwt.SignedJWT;

import lombok.SneakyThrows;

public class JwtUsernameAndPasswordAuthenticationFilter extends UsernamePasswordAuthenticationFilter {
	
	private final AuthenticationManager authenticationManager;
	private final JwtConfiguration jwtConfiguration;
	private final TokenCreator tokenCreator;
	
	private Logger log = LoggerFactory.getLogger(JwtUsernameAndPasswordAuthenticationFilter.class);
	
	public JwtUsernameAndPasswordAuthenticationFilter(AuthenticationManager authenticationManager, JwtConfiguration jwtConfiguration, TokenCreator tokenCreator) {
		this.authenticationManager = authenticationManager;
		this.jwtConfiguration = jwtConfiguration;
		this.tokenCreator = tokenCreator;
	}

	@Override
	@SneakyThrows
	public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response){
		ApplicationUser applicationUser;

		System.out.println(authenticationManager == null);
		try {
			applicationUser = new ObjectMapper().readValue(request.getInputStream(), ApplicationUser.class);
		} catch (IOException e) {
			log.info(e.getMessage());
			throw new UsernameNotFoundException("Unable to retrieve the username or password");
		}

		if(applicationUser == null) {
			throw new UsernameNotFoundException("Unable to retrieve the username or password");	
		}

		log.info("Creating the authentication object for the user '{}' and calling UserDetailServiceImpl loadUserByUsername", applicationUser.getUsername());

		UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(applicationUser.getUsername(), applicationUser.getPassword(), Collections.emptyList());
		usernamePasswordAuthenticationToken.setDetails(applicationUser);

		return authenticationManager.authenticate(usernamePasswordAuthenticationToken);
	}
	
	@Override
	protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain,
			Authentication auth) throws IOException, ServletException {

		log.info("Authentication was success for the user '{}'", auth.getName());

		try {
			SignedJWT signedJWT = tokenCreator.createSignedJWT(auth);
			String encryptToken = tokenCreator.encryptToken(signedJWT);
			
			log.info("Token generated successfully, adding it to the response header");
			
			response.addHeader("Access-Control-Expose-Headers", "XSRF-TOKEN " + jwtConfiguration.getHeader().getName());
			response.addHeader(jwtConfiguration.getHeader().getName(), jwtConfiguration.getHeader().getPrefix()  + encryptToken);
		} catch (NoSuchAlgorithmException | JOSEException e) {
			log.info("Internal Authentication Error");
			log.info(e.getMessage());
			response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
			
		}
	}
	
	@Override
	protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response,
			AuthenticationException failed) throws IOException, ServletException {
		log.info(failed.getMessage());
		super.unsuccessfulAuthentication(request, response, failed);
	}

}
