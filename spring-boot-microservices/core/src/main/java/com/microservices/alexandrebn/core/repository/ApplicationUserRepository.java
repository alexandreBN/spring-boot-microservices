package com.microservices.alexandrebn.core.repository;

import org.springframework.data.repository.PagingAndSortingRepository;

import com.microservices.alexandrebn.core.model.ApplicationUser;

public interface ApplicationUserRepository extends PagingAndSortingRepository<ApplicationUser, Long> {
	ApplicationUser findByUsername(String username);
}
