package com.mseck.controller;

import java.util.Collections;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.mseck.configuration.JwtGenerator;
import com.mseck.dto.AuthResponseDTO;
import com.mseck.dto.UserDTO;
import com.mseck.exception.ErrorResponseDTO;
import com.mseck.model.Role;
import com.mseck.model.UserEntity;
import com.mseck.repository.RoleRepository;
import com.mseck.repository.UserRepository;


@RestController
@RequestMapping("/api/auth")
public class AuthController {
	
	@Autowired 
	private UserRepository userRepository;
	
	@Autowired
	private RoleRepository roleRepository;	
	@Autowired
	private AuthenticationManager authenticationManager;
	
	@Autowired
	private PasswordEncoder passwordEncoder;
	
	@Autowired
	private JwtGenerator jwtGenerator;
	
	@PostMapping("/register")
	public ResponseEntity<String> register(@RequestBody UserDTO userDto){
		
		if(userRepository.existsByUsername(userDto.getUsername())) {
			return new ResponseEntity<>("Username is taken !", HttpStatus.BAD_REQUEST);
		}
		
		UserEntity user = new UserEntity();
		user.setUsername(userDto.getUsername());
		user.setPassword(passwordEncoder.encode(userDto.getPassword()));
		
		Role roles = roleRepository.findByName("USER").get();
		user.setRoles(Collections.singletonList(roles));
		
		userRepository.save(user);
		
		return new ResponseEntity<>("User registered success !", HttpStatus.OK);
		
	}
	
	@PostMapping("/login")
	public ResponseEntity<Object> login(@RequestBody UserDTO userDto){
		
		try {
	        Authentication authentication = authenticationManager.authenticate(
	                new UsernamePasswordAuthenticationToken(userDto.getUsername(), userDto.getPassword())
	        );

	        SecurityContextHolder.getContext().setAuthentication(authentication);
	        String token = jwtGenerator.generateToken(authentication);
	        return new ResponseEntity<>(new AuthResponseDTO(token), HttpStatus.OK);

	    } catch (BadCredentialsException ex) {
	        return new ResponseEntity<>(new ErrorResponseDTO("Invalid username or password"), HttpStatus.UNAUTHORIZED);
	        
	    } catch (UsernameNotFoundException ex) {
	        return new ResponseEntity<>(new ErrorResponseDTO("User not found"), HttpStatus.NOT_FOUND);
	    }
		
	}
	
	@PostMapping("/logout")
    public ResponseEntity<String> logout() {
        SecurityContextHolder.clearContext();
        return new ResponseEntity<>("User logged out success!", HttpStatus.OK);
    }

}
