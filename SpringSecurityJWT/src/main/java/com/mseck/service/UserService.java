package com.mseck.service;


import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.mseck.model.Role;
import com.mseck.model.UserEntity;
import com.mseck.repository.UserRepository;

@Service
public class UserService implements UserDetailsService {
	
	
	@Autowired
	private UserRepository userRepository;

	Collection<GrantedAuthority> mapRoleByAuthority(List<Role> roles){
		
		return roles.stream()
					.map(role -> new SimpleGrantedAuthority(role.getName()))
					.collect(Collectors.toList());
	}
	
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		
		UserEntity userEntity = userRepository.findByUsername(username)
				.orElseThrow(() -> new UsernameNotFoundException("Username not found !") );
		
		return new User(userEntity.getUsername(), userEntity.getPassword(), mapRoleByAuthority(userEntity.getRoles()));
	}

}
