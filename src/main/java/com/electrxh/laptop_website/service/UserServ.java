package com.electrxh.laptop_website.service;

import com.electrxh.laptop_website.dto.RegistrationDto;
import com.electrxh.laptop_website.model.UserEntity;

public interface UserServ {
	
	public void saveUser(RegistrationDto reg);
	public UserEntity findByMail(String email);
	public UserEntity findByName(String name);

}
