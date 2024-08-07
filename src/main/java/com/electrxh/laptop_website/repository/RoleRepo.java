package com.electrxh.laptop_website.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.electrxh.laptop_website.model.Role;

public interface RoleRepo extends JpaRepository<Role,Integer> {
	
	Role findByName(String name);

}
