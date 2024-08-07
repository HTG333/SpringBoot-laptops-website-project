package com.electrxh.laptop_website.security;

import java.util.stream.Collectors;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.electrxh.laptop_website.model.UserEntity;
import com.electrxh.laptop_website.repository.UserRepo;

@RequiredArgsConstructor
@Service
public class CustomUserDetailsService implements UserDetailsService{
	

	private final UserRepo userRep;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		UserEntity user=userRep.findFirstByUsername(username);
		
		if(user != null) {

            return new User(

								user.getUsername(),
								user.getPassword(),
								user.getRoles().stream().map((role)->new SimpleGrantedAuthority(role.getName()))
								.collect(Collectors.toList())
									);
					
			
		} else {
			
			throw new UsernameNotFoundException("Invalid Username or Password");
		}
		
		
	}
	
	
	

}
