package org.donnex.user.service;

import java.util.Arrays;
import java.util.HashSet;

import javax.annotation.PostConstruct;

import org.donnex.user.model.Role;
import org.donnex.user.model.User;
import org.donnex.user.repository.RoleRepository;
import org.donnex.user.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service("userService")
public class UserServiceImpl {
	
	@PostConstruct
	public void init() {
		Role role = new Role();
		role.setRole("ADMIN");
		
		User user = new User();
		user.setActive(1);
		user.setEmail("guileme13@gmail.com");
		user.setLastName("fioritti");
		user.setName("guilherme");
		user.setPassword(passwordencoder.encode("nova1010"));
		user.setRoles(new HashSet<>(Arrays.asList(role)));
		
		userRepository.save(user);
		
		Role role2 = new Role();
		role2.setRole("COMMON");
		
		User user2 = new User();
		user2.setActive(1);
		user2.setEmail("joao@donnex.com");
		user2.setLastName("silva");
		user2.setName("joao");
		user2.setPassword(passwordencoder.encode("nova1010"));
		user2.setRoles(new HashSet<>(Arrays.asList(role2)));
		
		userRepository.save(user2);
		
		
		
	}

	@Autowired
	private UserRepository userRepository;
	@Autowired
    private RoleRepository roleRepository;
    @Autowired
    private PasswordEncoder passwordencoder;
	
	public User findUserByEmail(String email) {
		return userRepository.findByEmail(email);
	}

	public void saveUser(User user) {
		user.setPassword(passwordencoder.encode(user.getPassword()));
        user.setActive(1);
        Role userRole = roleRepository.findByRole("ADMIN");
        user.setRoles(new HashSet<Role>(Arrays.asList(userRole)));
		userRepository.save(user);
	}

	public User loadUserByUsername(String username) {
		return userRepository.findByEmail(username);
	}


}
