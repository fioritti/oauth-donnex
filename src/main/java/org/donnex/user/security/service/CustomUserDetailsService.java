package org.donnex.user.security.service;

import org.donnex.user.domain.User;
import org.donnex.user.security.domain.CustomUserDetails;
import org.donnex.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailsService implements UserDetailsService {

	@Autowired
	private UserService userService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user=userService.loadUserByUsername(username);
        if(user == null) {
            throw new UsernameNotFoundException("No user with username: " + username);
        } else {
            return new CustomUserDetails(user);
        }
    }
}
