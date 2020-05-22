package com.microservices.alexandrebn.auth.security.user;

import static org.springframework.security.core.authority.AuthorityUtils.commaSeparatedStringToAuthorityList;

import java.util.Collection;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.microservices.alexandrebn.core.model.ApplicationUser;
import com.microservices.alexandrebn.core.repository.ApplicationUserRepository;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class UserDetailsServiceImpl implements UserDetailsService {

	@Autowired
	private ApplicationUserRepository applicationUserRepository;

	private Logger log = LoggerFactory.getLogger(UserDetailsServiceImpl.class);

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		log.info("Searching user by username '{}'", username);

		ApplicationUser applicationUser = applicationUserRepository.findByUsername(username);
		log.info("Application user found '{}'", applicationUser);

		if (applicationUser == null) {
			throw new UsernameNotFoundException(String.format("Application user '%s' not found", username));
		}

		return new CustomUserDetails(applicationUser);
	}

	private static final class CustomUserDetails extends ApplicationUser implements UserDetails {

		CustomUserDetails(ApplicationUser applicationUser) {
			super(applicationUser);
		}

		@Override
		public Collection<? extends GrantedAuthority> getAuthorities() {
			return commaSeparatedStringToAuthorityList("ROLE_" + this.getRole());
		}

		@Override
		public boolean isAccountNonExpired() {
			return true;
		}

		@Override
		public boolean isAccountNonLocked() {
			return true;
		}

		@Override
		public boolean isCredentialsNonExpired() {
			return true;
		}

		@Override
		public boolean isEnabled() {
			return true;
		}

	}

}
