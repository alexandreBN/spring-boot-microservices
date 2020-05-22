package com.microservices.alexandrebn.auth.endpoint.controller;

import java.security.Principal;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.microservices.alexandrebn.core.model.ApplicationUser;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping("user")
@Api(value = "Endpoint to manage User's information")
public class UserInfoController {
	@GetMapping(path = "info", produces = { MediaType.APPLICATION_JSON_VALUE })
	@ApiOperation(value = "Will retrieve information from authentication user token based", response = ApplicationUser.class)
	public ResponseEntity<ApplicationUser> getUserInfo(Principal principal) {
		ApplicationUser applicationUser = (ApplicationUser) ((UsernamePasswordAuthenticationToken) principal).getPrincipal();
		return ResponseEntity.ok(applicationUser);
	}
}