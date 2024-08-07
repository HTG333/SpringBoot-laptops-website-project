package com.electrxh.laptop_website.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import com.electrxh.laptop_website.dto.RegistrationDto;
import com.electrxh.laptop_website.model.UserEntity;
import com.electrxh.laptop_website.security.SecurityUtil;
import com.electrxh.laptop_website.service.UserServ;

import jakarta.validation.Valid;


@RequiredArgsConstructor
@Controller
public class AuthController {
	

	private final UserServ userServ;
	
	@GetMapping("/login_reg")
	public String loginReg(Model m) {
		
		
		String username=SecurityUtil.getSessionUser();
		
		if(username!=null) {
			
			return "redirect:/home";
		}
		
		RegistrationDto reg=new RegistrationDto();
		
		m.addAttribute("reg_user",reg);


		return "login-reg";
	}
	
	
	@PostMapping("/reg/save")
	public String userSave(@Valid @ModelAttribute("reg_user") RegistrationDto reg,BindingResult result) {
		
		UserEntity existingUserByMail=userServ.findByMail(reg.getEmail());
		
		if(existingUserByMail !=null &&
		   existingUserByMail.getEmail()!=null &&
		   !existingUserByMail.getEmail().isEmpty()) {
			
			return "redirect:/login_reg?fail=true";
			
														}
		
		
		UserEntity existingUserByName=userServ.findByName(reg.getUsername());
		
		if(existingUserByName !=null &&
		   existingUserByName.getUsername()!=null &&
		   !existingUserByName.getUsername().isEmpty()) {
			
			return "redirect:/login_reg?fail=true";		
			
														}
		
		
		if(result.hasErrors()) {return "redirect:/login_reg?reg_fail=true";	}
		
		
		
		
		userServ.saveUser(reg);
		reg=new RegistrationDto();
		return "redirect:/login_reg?success";
		
	}

	

}
