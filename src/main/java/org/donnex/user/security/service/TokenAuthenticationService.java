package org.donnex.user.security.service;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.donnex.user.security.domain.UserDTO;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

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

	public static void addAuthentication(HttpServletResponse response, UserDTO dto) {
		String JWT = null;
		try {
			JWT = Jwts.builder().setSubject(new ObjectMapper().writeValueAsString(dto))
					.setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
					.signWith(SignatureAlgorithm.HS512, SECRET).compact();
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}

		response.addHeader(HEADER_STRING, TOKEN_PREFIX + " " + JWT);
	}

	public static Authentication getAuthentication(HttpServletRequest request) {
		String token = request.getHeader(HEADER_STRING);

		if (token != null) {
			// faz parse do token
			String user = Jwts.parser().setSigningKey(SECRET).parseClaimsJws(token.replace(TOKEN_PREFIX, "")).getBody()
					.getSubject();
			try {
				UserDTO dto = new ObjectMapper().readValue(user, UserDTO.class);
				List<SimpleGrantedAuthority> authorities = dto.getAuthorities().stream()
						.map(role -> new SimpleGrantedAuthority(role)).collect(Collectors.toList());
				return new UsernamePasswordAuthenticationToken(dto.getUsername(), null, authorities);
			} catch (Exception e) {
				e.printStackTrace();
			}

		}
		return null;
	}

}