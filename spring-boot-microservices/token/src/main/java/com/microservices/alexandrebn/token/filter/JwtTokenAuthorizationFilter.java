package com.microservices.alexandrebn.token.filter;

import java.io.IOException;
import java.nio.file.AccessDeniedException;
import java.text.ParseException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.filter.OncePerRequestFilter;

import com.microservices.alexandrebn.core.property.JwtConfiguration;
import com.microservices.alexandrebn.token.converter.TokenConverter;
import com.microservices.alexandrebn.token.security.SecurityContextUtil;
import com.nimbusds.jose.JOSEException;
import com.nimbusds.jwt.SignedJWT;

public class JwtTokenAuthorizationFilter extends OncePerRequestFilter {
	
	protected final JwtConfiguration jwtConfiguration;
	protected final TokenConverter tokenConverter;

	public JwtTokenAuthorizationFilter(JwtConfiguration jwtConfiguration, TokenConverter tokenConverter) {
		this.jwtConfiguration = jwtConfiguration;
		this.tokenConverter = tokenConverter;
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
		
		boolean isSigned = StringUtils.equalsIgnoreCase("signed", jwtConfiguration.getType());
		
		try {
			SecurityContextUtil.setSecurityContext(isSigned ? validate(token) : decryptValidating(token));
			chain.doFilter(request, response);
		} catch (ParseException | JOSEException e) {
			e.printStackTrace();
		}
	}
	
	protected SignedJWT decryptValidating(String encryptedToken) throws ParseException, JOSEException, AccessDeniedException {
		String signedToken = tokenConverter.decryptToken(encryptedToken);
		return validate(signedToken);
	}
	
	protected SignedJWT validate(String signedToken) throws AccessDeniedException, ParseException, JOSEException {
		tokenConverter.validateTokenSignature(signedToken);
		return SignedJWT.parse(signedToken);
	}
	
}
