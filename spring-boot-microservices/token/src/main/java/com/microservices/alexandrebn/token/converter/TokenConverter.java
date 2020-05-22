package com.microservices.alexandrebn.token.converter;

import java.nio.file.AccessDeniedException;
import java.text.ParseException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.spi.LoggerFactoryBinder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.microservices.alexandrebn.core.property.JwtConfiguration;
import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.JWEObject;
import com.nimbusds.jose.KeyLengthException;
import com.nimbusds.jose.crypto.DirectDecrypter;
import com.nimbusds.jose.crypto.RSASSAVerifier;
import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jwt.SignedJWT;

@Service
public class TokenConverter {
	
	@Autowired
	private JwtConfiguration jwtConfiguration;

	private final Logger log = LoggerFactory.getLogger(TokenConverter.class);
	
	public String decryptToken(String encryptedToken) throws ParseException, JOSEException {
		log.info("Attempt to decrypt JWT token");
		JWEObject jweObject = JWEObject.parse(encryptedToken);
		
		DirectDecrypter directDecrypter = new DirectDecrypter(jwtConfiguration.getPrivateKey().getBytes());
		
		jweObject.decrypt(directDecrypter);
		
		log.info("Token decrypted, return signed token");
		
		return jweObject.getPayload().toSignedJWT().serialize();
	}
	
	public void validateTokenSignature(String signedToken) throws ParseException, AccessDeniedException, JOSEException {
		log.info("Starting method to validate token signature...");
		
		SignedJWT signedJWT = SignedJWT.parse(signedToken);
		
		log.info("Token parsed!  Retrieving public key from signed token");
		
		RSAKey publicKey = RSAKey.parse(signedJWT.getHeader().getJWK().toJSONObject());
		
		log.info("Public key retrieved, validating signature...");
		
		if(!signedJWT.verify(new RSASSAVerifier(publicKey))) {
			throw new AccessDeniedException("Invalid token signature!");
		}
		
		log.info("The token has a valid signature");
	}
}
