package org.donnex.user.security;

import java.util.List;

import org.donnex.user.model.User;
import org.donnex.user.repository.RoleRepository;
import org.donnex.user.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service("customUserDetailsService")
public class CustomUserDetailsService implements UserDetailsService {
	
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;

    @Autowired
    public CustomUserDetailsService(UserRepository userRepository, RoleRepository roleRepository) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user=userRepository.findByEmail(username);
        if(user == null) {
            throw new UsernameNotFoundException("No user with username: " + username);
        } else {
            List<String> userRoles = roleRepository.findRoleByUserName(username);
            return new CustomUserDetails(user, userRoles);
        }
    }
}
