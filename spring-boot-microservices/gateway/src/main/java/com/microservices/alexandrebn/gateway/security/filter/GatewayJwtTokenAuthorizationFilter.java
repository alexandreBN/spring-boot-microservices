package com.microservices.alexandrebn.gateway.security.filter;

import java.io.IOException;
import java.text.ParseException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.microservices.alexandrebn.core.property.JwtConfiguration;
import com.microservices.alexandrebn.token.converter.TokenConverter;
import com.microservices.alexandrebn.token.filter.JwtTokenAuthorizationFilter;
import com.microservices.alexandrebn.token.security.SecurityContextUtil;
import com.netflix.zuul.context.RequestContext;
import com.nimbusds.jose.JOSEException;
import com.nimbusds.jwt.SignedJWT;

public class GatewayJwtTokenAuthorizationFilter extends JwtTokenAuthorizationFilter {
	
	public GatewayJwtTokenAuthorizationFilter(JwtConfiguration jwtConfiguration, TokenConverter tokenConverter) {
		super(jwtConfiguration, tokenConverter);
	}
	
	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
			throws ServletException, IOException {
		String header = request.getHeader(jwtConfiguration.getHeader().getName());
		
		if(header == null || !header.startsWith(jwtConfiguration.getHeader().getPrefix())) {
			chain.doFilter(request, response);
			return;
		}
		
		String token = header.replace(jwtConfiguration.getHeader().getPrefix(), "").trim();
		
		try {
			String signedToken = tokenConverter.decryptToken(token);
			tokenConverter.validateTokenSignature(signedToken);
			
			SecurityContextUtil.setSecurityContext(SignedJWT.parse(signedToken));
			
			if(jwtConfiguration.getType().equalsIgnoreCase("signed")) {
				RequestContext.getCurrentContext().addZuulRequestHeader("Authorization", jwtConfiguration.getHeader().getPrefix() + signedToken );
			}
			
		} catch (ParseException | JOSEException e1) {
			// TODO remove the stack trace and log error
			e1.printStackTrace();
		}
		
		chain.doFilter(request, response);
	}
}
