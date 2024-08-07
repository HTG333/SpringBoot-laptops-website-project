package com.electrxh.laptop_website.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.electrxh.laptop_website.model.Laptop;
import com.electrxh.laptop_website.model.UserEntity;

public interface LaptopRepo extends JpaRepository<Laptop, Integer>{
	
	@Query("SELECT l FROM  Laptop l where l.model LIKE CONCAT('%',:query,'%')")
	public List<Laptop>searchLaptops(String query);

    public List<Laptop>findLaptopsByType(String type);
	
    @Query("SELECT l FROM Laptop l WHERE l.createdBy = :user")
    List<Laptop> findLaptopsAddedByUser(UserEntity user);

}
