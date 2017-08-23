package org.donnex.user.controller;

import java.util.List;

import org.donnex.user.domain.User;
import org.donnex.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

@RestController
@RequestMapping("/users")
public class UserController {

	@Autowired
	private UserService userService;
	

	@PostMapping()
	public ResponseEntity<Void> registerUser(@RequestBody User user, UriComponentsBuilder ucBuilder) {
		userService.saveUser(user);
		HttpHeaders headers = new HttpHeaders();
		headers.setLocation(ucBuilder.path("/users/{id}").buildAndExpand(user.getId()).toUri());
		return new ResponseEntity<Void>(headers, HttpStatus.CREATED);
	}

	@GetMapping(value = "/{id}")
	public ResponseEntity<User> getUser(@PathVariable("id") long id,Authentication authentication) {
		User user = userService.findById(id);
		if (user == null) {
			System.out.println("User with id " + id + " not found");
			return new ResponseEntity<User>(HttpStatus.NOT_FOUND);
		}
		if(!user.getEmail().equals(authentication.getPrincipal())) {
			return new ResponseEntity<User>(HttpStatus.FORBIDDEN);
		}
		return new ResponseEntity<User>(user, HttpStatus.OK);
	}
	
	@Secured("ROLE_ADMIN")
	@GetMapping()
	public ResponseEntity<List<User>> getUser() {
		List<User> allUsers = userService.findAll();
		return new ResponseEntity<List<User>>(allUsers, HttpStatus.OK);
	}

}
