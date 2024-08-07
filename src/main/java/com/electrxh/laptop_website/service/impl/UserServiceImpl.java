package com.electrxh.laptop_website.service.impl;
import java.util.Collections;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.electrxh.laptop_website.dto.RegistrationDto;
import com.electrxh.laptop_website.model.Role;
import com.electrxh.laptop_website.model.UserEntity;
import com.electrxh.laptop_website.repository.RoleRepo;
import com.electrxh.laptop_website.repository.UserRepo;
import com.electrxh.laptop_website.service.UserServ;

@RequiredArgsConstructor
@Service
public class UserServiceImpl implements UserServ{

	private final UserRepo userRep;
	private final RoleRepo roleRep;
	private final PasswordEncoder passEncoder;

	@Override
	public void saveUser(RegistrationDto reg) {
		UserEntity user=new UserEntity();
		user.setUsername(reg.getUsername());
		user.setEmail(reg.getEmail());
		user.setPassword(passEncoder.encode( reg.getPassword()));
		
		Role role=roleRep.findByName("User");
		
		user.setRoles(Collections.singletonList(role));
		
		userRep.save(user);
	}

	@Override
	public UserEntity findByMail(String email) {
        return userRep.findByEmail(email);
	}
	
	@Override
	public UserEntity findByName(String name) {
        return userRep.findByUsername(name);
	}


}
