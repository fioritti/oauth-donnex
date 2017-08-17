package org.donnex.user.security;

import org.donnex.user.service.UserServiceImpl;
import org.springframework.security.core.userdetails.User;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

public class TokenHandler {

	private final String secret;
	private final UserServiceImpl userService;

	public TokenHandler(String secret, UserServiceImpl userService) {
		this.secret = secret;
		this.userService = userService;
	}

	public org.donnex.user.model.User parseUserFromToken(String token) {
		String username = Jwts.parser().setSigningKey(secret).parseClaimsJws(token).getBody().getSubject();
		return (org.donnex.user.model.User) userService.loadUserByUsername(username);
	}

	public String createTokenForUser(User user) {
		return Jwts.builder().setSubject(user.getUsername()).signWith(SignatureAlgorithm.HS256, secret).compact();
	}
}