package com.microservices.alexandrebn.core.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ApplicationUser implements AbstractEntity {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@EqualsAndHashCode.Include
	private Long id;
	
	@NotNull(message = "The field 'username' is mandatory")
	@Column(nullable = false)
	private String username;
	
	@NotNull(message = "The field 'password' is mandatory")
	@ToString.Exclude
	@Column(nullable = false)
	private String password;
	
	@NotNull(message = "The field 'role' is mandatory")
	@Column(nullable = false)
	private String role = "USER";
	
	public ApplicationUser() {};
	public ApplicationUser(ApplicationUser applicationUser) {
		this.id = applicationUser.getId();
		this.username = applicationUser.getUsername();
		this.password = applicationUser.getPassword();
		this.role = applicationUser.getRole();
	}

	@Override
	public Long getId() {
		return id;
	}

	public String getUsername() {
		return username;
	}

	public ApplicationUser setUsername(String username) {
		this.username = username;
		return this;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public ApplicationUser setId(Long id) {
		this.id = id;
		return this;
	}

	public String getRole() {
		return role;
	}

	public ApplicationUser setRole(String role) {
		this.role = role;
		return this;
	}
	
	public static ApplicationUser builder() {
		return new ApplicationUser();
	}
}
