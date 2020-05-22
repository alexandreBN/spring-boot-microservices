package com.microservices.alexandrebn.token.security;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;

import com.microservices.alexandrebn.core.model.ApplicationUser;
import com.nimbusds.jose.JOSEException;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;

public class SecurityContextUtil {
	
	private static final Logger log = LoggerFactory.getLogger(SecurityContextUtil.class);
	
	private SecurityContextUtil() {}
	
	public static void setSecurityContext(SignedJWT signedJWT) {
		try {
			JWTClaimsSet claims = signedJWT.getJWTClaimsSet();
			
			Map<String, Object> claims2 = signedJWT.getJWTClaimsSet().getClaims();
			System.out.println(claims2);
			String username = claims.getSubject();
			
			if(username == null) {
				throw new JOSEException("Username missing from JWT");
			}
			
			List<String> authorities = claims.getStringListClaim("authorities");
			ApplicationUser applicationUser = ApplicationUser.builder()
					.setId(claims.getLongClaim("userId"))
					.setUsername(claims.getClaim("username").toString())
					.setRole(String.join(",", authorities));
			
			// Usado para validar depois os authorities nos filters
			UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(applicationUser, null, createAuthorities(authorities));
			auth.setDetails(signedJWT.serialize());
			
			// Adicionando o authentication todo lugar é possível acessar as informações do usuário autenticado
			SecurityContextHolder.getContext().setAuthentication(auth);
		} catch(Exception e) {
			log.error("Error setting security context ", e);
			SecurityContextHolder.clearContext();
		}
	}
	
	private static List<SimpleGrantedAuthority> createAuthorities(List<String> authorities) {
		return	authorities.stream().map(authoritie -> new SimpleGrantedAuthority(authoritie)).collect(Collectors.toList());
	}
}
