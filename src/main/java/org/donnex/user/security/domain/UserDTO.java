package org.donnex.user.security.domain;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.donnex.user.domain.Role;

public class UserDTO {

	private String username;
	private List<String> authorities;

	public UserDTO() {
	}

	public UserDTO(String username, Set<Role> roles) {
		this.username = username;
		this.authorities = roles.stream().map(Role::getAuthority).collect(Collectors.toList());
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public List<String> getAuthorities() {
		return authorities;
	}

	public void setAuthorities(List<String> authorities) {
		this.authorities = authorities;
	}

}
