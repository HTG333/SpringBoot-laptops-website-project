package com.electrxh.laptop_website.service;

import java.util.List;

import com.electrxh.laptop_website.dto.LaptopDto;


public interface LaptopService {
	
	public List<LaptopDto>findAllLaptops();
	public void saveLaptop(LaptopDto lap);
	public List<LaptopDto> userContributions();
	public LaptopDto findLaptop(int lId);
	public void updateLaptop(LaptopDto l);
	public void deleteLaptop(int lId);
	public List<LaptopDto>searchLaptops(String query);
	public List<LaptopDto>LaptopsByType(String query);

}
