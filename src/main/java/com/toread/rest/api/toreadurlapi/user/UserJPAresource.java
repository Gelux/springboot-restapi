package com.toread.rest.api.toreadurlapi.user;


import java.net.URI;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.toread.rest.api.toreadurlapi.errorhandling.UserNotFoundException;

@RestController
public class UserJPAresource {
	
	@Autowired
	private UserRepository userRepo;
	
	@Autowired
	private UrlRepository urlsRepo;
	
	@GetMapping("/users")
	public List<User> retrieveAllUsers(){
		return userRepo.findAll();
	}
	
	@GetMapping("/users/email/{email}")
	public int getUserId(@PathVariable String email){
		List<User> users = userRepo.findAll();
		
		Iterator<User> urlIterator = users.iterator();
		
		while(urlIterator.hasNext()) {
			User userAux = urlIterator.next();
			System.out.println(userAux.getEmail());
			if (userAux.getEmail().equals(email)) {
				return userAux.getId();
			}
		}
		
		return 0;
		
	}
	
	@GetMapping("/users/{id}")
	public User retrieveUserById(@PathVariable int id){
		Optional<User> user = userRepo.findById(id);
		
		if(!user.isPresent())
			throw new UserNotFoundException("id-" + id);	
		
		return user.get();
	}
	
	@GetMapping("/users/{id}/url")
	public List<Urls> retrieveUserUrls(@PathVariable int id){
		Optional<User> user = userRepo.findById(id);
		
		if(!user.isPresent())
			throw new UserNotFoundException("id-" + id);	
		
		return user.get().getUrls();
	}
	
	@GetMapping("/users/{id}/url/{id2}")
	public Urls retrieveUserSpecificUrl(@PathVariable int id, @PathVariable int id2){
		Optional<User> user = userRepo.findById(id);
		
		if(!user.isPresent())
			throw new UserNotFoundException("id-" + id);	
		
		List<Urls> urls = user.get().getUrls();
		
		Iterator<Urls> urlIterator = urls.iterator();
		
		while(urlIterator.hasNext()) {
			Urls urlAux = urlIterator.next();
			if (urlAux.getId() == id2) {
				return urlAux;
			}
		}
		
		return null;
	}
	
	@DeleteMapping("/users/{id}")
	public void deleteUser(@PathVariable int id) {
		userRepo.deleteById(id);
	}
	
	@DeleteMapping("/users/{id}/url/{id2}")
	public void deleteUserUrl(@PathVariable int id,@PathVariable int id2) {
		Optional<User> user = userRepo.findById(id);
		
		if(!user.isPresent())
			throw new UserNotFoundException("id-" + id);	
		
		List<Urls> urls = user.get().getUrls();
		
		Iterator<Urls> urlIterator = urls.iterator();
		
		while(urlIterator.hasNext()) {
			Urls urlAux = urlIterator.next();
			if (urlAux.getId() == id2) {
				urlsRepo.deleteById(urlAux.getId());
			}
		}
	}
	
	@PostMapping("/users")
	public ResponseEntity<Object> createUser(@Valid @RequestBody User user) {
		User savedUser = userRepo.save(user);

		URI location = ServletUriComponentsBuilder.fromCurrentRequest()
									.path("/{id}")
									.buildAndExpand(savedUser.getId()).toUri();
		return ResponseEntity.created(location).build();
	}
	
	@PostMapping("/users/{id}/url")
	public ResponseEntity<Object> createUrl(@Valid @PathVariable int id, @Valid @RequestBody Urls url) {
		Optional<User> user = userRepo.findById(id);
		
		if(!user.isPresent())
			throw new UserNotFoundException("id-" + id);

		User userOptional = user.get();
		
		url.setUser(userOptional);
		
		urlsRepo.save(url);
		
		URI location = ServletUriComponentsBuilder.fromCurrentRequest()
									.path("/{id}")
									.buildAndExpand(url.getId()).toUri();
		return ResponseEntity.created(location).build();
	}
}	
