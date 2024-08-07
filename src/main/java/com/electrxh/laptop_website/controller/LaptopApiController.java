package com.electrxh.laptop_website.controller;

import com.electrxh.laptop_website.dto.LaptopDto;
import com.electrxh.laptop_website.model.Role;
import com.electrxh.laptop_website.model.UserEntity;
import com.electrxh.laptop_website.security.SecurityUtil;
import com.electrxh.laptop_website.service.LaptopService;
import com.electrxh.laptop_website.service.UserServ;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/laptops")
public class LaptopApiController {

    private final LaptopService lapServ;
    private final UserServ userServ;


    @GetMapping("/view-laptops")
    public ResponseEntity<List<LaptopDto>> getAllLaptops() {
        List<LaptopDto> lap = lapServ.findAllLaptops();
        return ResponseEntity.ok(lap);
    }

    @PostMapping("/add-laptop")
    public ResponseEntity<String> addLaptop(@Valid  @RequestBody LaptopDto laptop,
                                            BindingResult result){


        String username = SecurityUtil.getSessionUser();
        UserEntity user=userServ.findByName(username);

        if(user==null){
            return new ResponseEntity<>("You are not authenticated!",
                    HttpStatus.FORBIDDEN);}

        if (result.hasErrors()) {
            return new ResponseEntity<>("Invalid Input",HttpStatus.BAD_REQUEST);
        }

        lapServ.saveLaptop(laptop);
        return new ResponseEntity<>("Your Laptop Added Successfully!",
                                    HttpStatus.CREATED);


    }


    @DeleteMapping("/delete-laptop/{lap-id}")
    public ResponseEntity<String> deleteLaptop(
            @PathVariable("lap-id") int lapId){



        String username = SecurityUtil.getSessionUser();
        UserEntity user=userServ.findByName(username);

        if(user==null){
            return new ResponseEntity<>("You are not authenticated!",
                    HttpStatus.FORBIDDEN);}

        List<String>userRoles=user.getRoles().stream().map(Role::getName).toList();

        if(userRoles.contains("Admin")){

        lapServ.deleteLaptop(lapId);
        return ResponseEntity.ok("The Laptop deleted successfully");

        }

        return new ResponseEntity<>("your account is unauthorized for this operation"
                ,HttpStatus.FORBIDDEN);
    }

    @GetMapping("/laptop-details/{lap-id}")
    public ResponseEntity<?>getLaptopDetails(
            @PathVariable("lap-id") int lapId){
        String username = SecurityUtil.getSessionUser();
        UserEntity user=userServ.findByName(username);

        if(user==null){
            return new ResponseEntity<>("You are not authenticated!",
                    HttpStatus.FORBIDDEN);}

        LaptopDto lap=lapServ.findLaptop(lapId);
        return ResponseEntity.ok(lap);
    }

    @GetMapping("/search-laptops/{lap-query}")
    public ResponseEntity<List<LaptopDto>> searchLaptops(
            @PathVariable("lap-query") String query){

        List<LaptopDto> lap=lapServ.searchLaptops(query);

        return ResponseEntity.ok(lap);
    }


    @PutMapping("/edit-laptop/{lap-id}")
    public ResponseEntity<String>editLaptopInfo(@PathVariable("lap-id") int lapId,
                                                @Validated @RequestBody LaptopDto laptop,
                                                BindingResult result
                                                ){

        String username = SecurityUtil.getSessionUser();
        UserEntity user=userServ.findByName(username);

        if(user==null){
            return new ResponseEntity<>("You are not authenticated!",
                    HttpStatus.FORBIDDEN);}

        List<String>userRoles=user.getRoles().stream().map(Role::getName).toList();

        if(userRoles.contains("Admin")){

            if (result.hasErrors()) {
                return new ResponseEntity<>("Invalid Input",HttpStatus.BAD_REQUEST);
            }

            laptop.setLapId(lapId);
            lapServ.updateLaptop(laptop);
            return ResponseEntity.ok("Laptop Info successfully modified");

        }

        return new ResponseEntity<>("your account is unauthorized for this operation"
                ,HttpStatus.FORBIDDEN);

    }


    @GetMapping("/top-laptops")
    public ResponseEntity<List<LaptopDto>> getTopLaptops(){

        List<LaptopDto> lap=lapServ.LaptopsByType("Top");

        return ResponseEntity.ok(lap);
    }

    @GetMapping("/laptops-offers")
    public ResponseEntity<List<LaptopDto>> getLaptopsOffers(){

        List<LaptopDto> lap=lapServ.LaptopsByType("Deal");

        return ResponseEntity.ok(lap);
    }




}