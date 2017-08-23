package org.donnex.user.service;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

import javax.annotation.PostConstruct;

import org.donnex.user.domain.Role;
import org.donnex.user.domain.User;
import org.donnex.user.repository.RoleRepository;
import org.donnex.user.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {
	
	@PostConstruct
	public void init() {
		Role role = new Role("ROLE_ADMIN");
		
		User user = new User();
		user.setActive(true);
		user.setEmail("guileme13@gmail.com");
		user.setName("guilherme");
		user.setLastName("fioritti");
		user.setPassword(passwordencoder.encode("nova1010"));
		user.setRoles(new HashSet<>(Arrays.asList(role)));
		
		userRepository.save(user);
		
		roleRepository.save(new Role("ROLE_USER"));
		
	}

	@Autowired
	private UserRepository userRepository;
	
	@Autowired
    private RoleRepository roleRepository;
	
    @Autowired
    private PasswordEncoder passwordencoder;
    
    public List<User> findAll(){
    	return userRepository.findAll();
    }
    
    public User findById(Long id) {
    	return userRepository.findOne(id);
    }
	
	public User findUserByEmail(String email) {
		return userRepository.findByEmail(email);
	}

	public void saveUser(User user) {
		user.setPassword(passwordencoder.encode(user.getRegisterPassword()));
        user.setActive(true);
        Role userRole = roleRepository.findByRole("ROLE_USER");
        user.setRoles(new HashSet<Role>(Arrays.asList(userRole)));
		userRepository.save(user);
	}

	public User loadUserByUsername(String username) {
		return userRepository.findByEmail(username);
	}


}
