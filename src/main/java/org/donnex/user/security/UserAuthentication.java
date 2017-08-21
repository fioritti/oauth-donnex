package org.donnex.user.security;

import java.util.Collection;
import java.util.Set;

import org.donnex.user.model.Role;
import org.donnex.user.model.User;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown=true)
public class UserAuthentication implements Authentication{

	private static final long serialVersionUID = 1L;
	private final User user;
	private boolean authentication = true;
	
	public UserAuthentication() {
		this.user = null;
	};
	
	public UserAuthentication(User user) {
		super();
		this.user = user;
	}
	
	public UserAuthentication(String email,Set<Role> roles) {
		this.user = new User();
		this.user.setEmail(email);
		this.user.setRoles(roles);
	}
	

	@Override
	public String getName() {
		return user.getName();
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return (Collection<? extends GrantedAuthority>) user.getRoles();
	}

	@Override
	public Object getCredentials() {
		return user.getPassword();
	}

	@JsonIgnore
	@Override
	public Object getDetails() {
		return user;
	}

	@Override
	public Object getPrincipal() {
		return user.getEmail();
	}

	@Override
	public boolean isAuthenticated() {
		return authentication;
	}

	@Override
	public void setAuthenticated(boolean isAuthenticated) throws IllegalArgumentException {
		this.authentication = isAuthenticated;
	}

}
