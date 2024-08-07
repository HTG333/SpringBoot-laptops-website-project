package com.electrxh.laptop_website.service.impl;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import com.electrxh.laptop_website.dto.LaptopDto;
import com.electrxh.laptop_website.model.Laptop;
import com.electrxh.laptop_website.model.UserEntity;
import com.electrxh.laptop_website.repository.LaptopRepo;
import com.electrxh.laptop_website.repository.UserRepo;
import com.electrxh.laptop_website.security.SecurityUtil;
import com.electrxh.laptop_website.service.LaptopService;


@RequiredArgsConstructor
@Service
public class LaptopServiceImpl implements LaptopService{
	

	private final LaptopRepo lapRepo;
	private final UserRepo userRepo;


	@Override
	public List<LaptopDto> findAllLaptops() {
		
		List<Laptop> lap_list=lapRepo.findAll();
		
		return lap_list.stream().map(this::mapToDto).collect(Collectors.toList());
	}

	@Override
	public void saveLaptop(LaptopDto lap) {
		String name=SecurityUtil.getSessionUser();
		UserEntity user=userRepo.findByUsername(name);
		lap.setCreatedBy(user);
		Laptop l=mapToLap(lap);
		lapRepo.save(l);
	}
	
	
	@Override
	public List<LaptopDto> userContributions() {
		
		String name=SecurityUtil.getSessionUser();
		UserEntity user=userRepo.findByUsername(name);
		
		List<Laptop> lap_list=lapRepo.findLaptopsAddedByUser(user);
		
		return lap_list.stream().map(this::mapToDto).collect(Collectors.toList());
	}

	@Override
	public LaptopDto findLaptop(int lId) {
		
		Optional<Laptop> l=lapRepo.findById(lId);
        return l.map(this::mapToDto).orElse(null);
    }

	@Override
	public void updateLaptop(LaptopDto lap) {
		
		String name=SecurityUtil.getSessionUser();
		UserEntity user=userRepo.findByUsername(name);
		Laptop l=mapToLap(lap);
		l.setCreatedBy(user);	
		
		lapRepo.save(l);
		
		
	}

	@Override
	public void deleteLaptop(int lId) {
	
		Optional<Laptop> l=lapRepo.findById(lId);
        l.ifPresent(lapRepo::delete);
		
	}

	@Override
	public List<LaptopDto> searchLaptops(String query) {
		
		List<Laptop> lap_list=lapRepo.searchLaptops(query);
		
		return lap_list.stream().map(this::mapToDto).collect(Collectors.toList());
		
	}

	@Override
	public List<LaptopDto> LaptopsByType(String query) {
		List<Laptop> lap_list=lapRepo.findLaptopsByType(query);

		return lap_list.stream().map(this::mapToDto).collect(Collectors.toList());
	}


	private LaptopDto  mapToDto(Laptop lap) {

        return LaptopDto.builder().lapId(lap.getLapId())
				.model(lap.getModel())
				.brand(lap.getBrand())
				.cpu(lap.getCpu())
				.budget(lap.getBudget())
				.hdd(lap.getHdd())
				.ssd(lap.getSsd())
				.devPhotoUrl(lap.getDevPhotoUrl())
				.display(lap.getDisplay())
				.ram(lap.getRam())
				.graphics(lap.getGraphics())
				.rate(lap.getRate())
				.notes(lap.getNotes())
				.type(lap.getType())
				.createdBy(lap.getCreatedBy())
				.createdOn(lap.getCreatedOn())
				.updatedOn(lap.getUpdatedOn())
				.build();
	}
	
	
	private Laptop  mapToLap(LaptopDto lap) {

        return Laptop.builder().lapId(lap.getLapId())
				.model(lap.getModel())
				.brand(lap.getBrand())
				.cpu(lap.getCpu())
				.budget(lap.getBudget())
				.hdd(lap.getHdd())
				.ssd(lap.getSsd())
				.devPhotoUrl(lap.getDevPhotoUrl())
				.display(lap.getDisplay())
				.ram(lap.getRam())
				.graphics(lap.getGraphics())
				.rate(lap.getRate())
				.notes(lap.getNotes())
				.type("standard")
				.createdBy(lap.getCreatedBy())
				.createdOn(lap.getCreatedOn())
				.updatedOn(lap.getUpdatedOn())
				.build();
	}

}
