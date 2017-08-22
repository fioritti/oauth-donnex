package org.donnex.user.security.configuration;

import org.donnex.user.security.filter.JWTAuthenticationFilter;
import org.donnex.user.security.filter.JWTLoginFilter;
import org.donnex.user.security.service.CustomUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(securedEnabled = true)
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

	
	@Autowired
	private CustomUserDetailsService userDetailsService;
	
	@Autowired
	private AuthenticationEntryPoint authenticationEntryPoint;
	
	@Autowired
    public void configAuthentication(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService).passwordEncoder(passwordencoder());
    }


	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http
			.csrf()
			.disable()
	 		.exceptionHandling()
	 		.authenticationEntryPoint(authenticationEntryPoint)
	 		.and()
		 	.sessionManagement()
		 	.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
		 	.and()
	 		.authorizeRequests()
	 		.antMatchers(HttpMethod.GET,"/users").permitAll()
	 		.anyRequest().authenticated();
		
		 http
         // We filter the api/login requests
         .addFilterBefore(new JWTLoginFilter("/login", authenticationManager()),
                 UsernamePasswordAuthenticationFilter.class)
         // And filter other requests to check the presence of JWT in header
         .addFilterBefore(new JWTAuthenticationFilter(),
                 UsernamePasswordAuthenticationFilter.class);
			
		
		
	}
	
    @Bean(name="passwordEncoder")
    public PasswordEncoder passwordencoder() {
        return new BCryptPasswordEncoder();
    }
	

}
