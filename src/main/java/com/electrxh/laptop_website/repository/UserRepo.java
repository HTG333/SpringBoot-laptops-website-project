package com.electrxh.laptop_website.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.electrxh.laptop_website.model.UserEntity;

public interface UserRepo  extends JpaRepository<UserEntity,Integer>{

	UserEntity findByEmail(String email);
	UserEntity findByUsername(String username);
	UserEntity findFirstByUsername(String username);
	
}
