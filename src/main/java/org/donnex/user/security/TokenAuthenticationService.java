package org.donnex.user.security;

import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

public class TokenAuthenticationService {
	
	// EXPIRATION_TIME = 10 dias
	static final long EXPIRATION_TIME = 860_000_000;
	static final String SECRET = "MySecret";
	static final String TOKEN_PREFIX = "Bearer";
	static final String HEADER_STRING = "Authorization";
	
	static void addAuthentication(HttpServletResponse response, UserAuthentication usetAuthentication) {
		String JWT = null;
		try {
			JWT = Jwts.builder()
					.setSubject(new ObjectMapper().writeValueAsString(usetAuthentication))
					.setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
					.signWith(SignatureAlgorithm.HS512, SECRET)
					.compact();
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
		
		response.addHeader(HEADER_STRING, TOKEN_PREFIX + " " + JWT);
	}
	
	static Authentication getAuthentication(HttpServletRequest request) {
		String token = request.getHeader(HEADER_STRING);
		
		if (token != null) {
			// faz parse do token
			String user = Jwts.parser()
					.setSigningKey(SECRET)
					.parseClaimsJws(token.replace(TOKEN_PREFIX, ""))
					.getBody()
					.getSubject();
			
			try {
				UserAuthentication userAuthentication = new ObjectMapper().readValue(user, UserAuthentication.class);
				if (userAuthentication != null) {
					return new UsernamePasswordAuthenticationToken(userAuthentication.getPrincipal(), null, userAuthentication.getAuthorities());
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			
		}
		return null;
	}
	
}