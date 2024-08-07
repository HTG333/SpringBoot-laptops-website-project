package com.electrxh.laptop_website.controller;


import java.util.List;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import com.electrxh.laptop_website.dto.LaptopDto;
import com.electrxh.laptop_website.model.UserEntity;
import com.electrxh.laptop_website.security.SecurityUtil;
import com.electrxh.laptop_website.service.LaptopService;
import com.electrxh.laptop_website.service.UserServ;

import jakarta.validation.Valid;
import jakarta.websocket.server.PathParam;

@RequiredArgsConstructor
@Controller
public class LaptopController {


	private final LaptopService lapServ;
	private final UserServ userServ;

	@GetMapping("/home")
	public String getLaptopsInfo(Model m) {

		List<LaptopDto> lap = lapServ.findAllLaptops();
        lap.sort((l1, l2) -> l2.getCreatedOn().compareTo(l1.getCreatedOn()));
		UserEntity user = new UserEntity();

		String username = SecurityUtil.getSessionUser();

		if (username != null) {

			user = userServ.findByName(username);
			m.addAttribute("user", user);
		}

		m.addAttribute("user", user);
		m.addAttribute("laptop", lap);
		m.addAttribute("laptop_date_sorted", lap);

		return "home";
	}

	@GetMapping("/profile")
	public String profileInfo(Model m) {

		List<LaptopDto> lap = lapServ.userContributions();
		UserEntity user = new UserEntity();

		String username = SecurityUtil.getSessionUser();

		if (username != null) {

			user = userServ.findByName(username);

			m.addAttribute("user", user);
		}

		m.addAttribute("user", user);
		m.addAttribute("laptop", lap);

		return "contributions";
	}

	@GetMapping("/delete/{lapId}")
	public String deleteLaptop(@PathVariable("lapId") int lId) {

		lapServ.deleteLaptop(lId);

		return "redirect:/home";
	}

	@GetMapping("/search")
	public String laptopSearch(@PathParam(value = "q") String q, Model m) {

		List<LaptopDto> lap = lapServ.searchLaptops(q);
		UserEntity user = new UserEntity();

		String username = SecurityUtil.getSessionUser();

		if (username != null) {

			user = userServ.findByName(username);
			m.addAttribute("user", user);
		}

		m.addAttribute("user", user);

		m.addAttribute("laptop", lap);

		return "search-tech";
	}

	@GetMapping("/add_laptop")
	public String addLaptop(Model m) {

		LaptopDto lap = new LaptopDto();
		UserEntity user = new UserEntity();

		String username = SecurityUtil.getSessionUser();

		if (username != null) {

			user = userServ.findByName(username);
			m.addAttribute("user", user);
		}

		m.addAttribute("user", user);
		m.addAttribute("laptop", lap);
		return "add-new-tech";
	}

	@PostMapping("/add_laptop")
	public String saveLaptop(@Valid @ModelAttribute("laptop") LaptopDto lap, BindingResult result, Model m) {
		if (result.hasErrors()) {

			String username = SecurityUtil.getSessionUser();
			if (username != null) {
				UserEntity user = userServ.findByName(username);
				m.addAttribute("user", user);
			}
		    m.addAttribute("scrollToErrorSection", true);
			return "add-new-tech";
		}
		lapServ.saveLaptop(lap);
		return "redirect:/add_laptop?success";
	}

	@GetMapping("/laptop_details/{lapId}")
	public String laptopDetails(@PathVariable("lapId") int lId, Model m) {

		LaptopDto l = lapServ.findLaptop(lId);
		UserEntity user = new UserEntity();

		String username = SecurityUtil.getSessionUser();

		if (username != null) {

			user = userServ.findByName(username);
			m.addAttribute("user", user);
		}

		m.addAttribute("user", user);
		m.addAttribute("laptop", l);
		return "tech-details";
	}

	@GetMapping("/view_tech")
	public String laptopView(Model m) {
		List<LaptopDto> l = lapServ.findAllLaptops();
		UserEntity user = new UserEntity();

		String username = SecurityUtil.getSessionUser();

		if (username != null) {

			user = userServ.findByName(username);
			m.addAttribute("user", user);
		}

		m.addAttribute("user", user);
		m.addAttribute("result", l);

		return "view-tech";
	}

	@GetMapping("/edit/{lapId}")
	public String laptopEdit(@PathVariable("lapId") int lId, Model m) {

		LaptopDto lap = lapServ.findLaptop(lId);
		UserEntity user = new UserEntity();

		String username = SecurityUtil.getSessionUser();

		if (username != null) {

			user = userServ.findByName(username);
			m.addAttribute("user", user);
		}

		m.addAttribute("user", user);
		m.addAttribute("laptop", lap);
		return "edit-tech";
	}

	@PostMapping("/edit/{lapId}")
	public String laptopUpdate( @PathVariable("lapId") int lId,@Valid @ModelAttribute("laptop") LaptopDto lap,
			BindingResult result, Model m) {

		if (result.hasErrors()) {
			
			String username = SecurityUtil.getSessionUser();
			if (username != null) {
				UserEntity user = userServ.findByName(username);
				m.addAttribute("user", user);
			}

			 m.addAttribute("scrollToErrorSection", true);
			return "edit-tech";
		}
		lap.setLapId(lId);
		lapServ.updateLaptop(lap);
		return "redirect:/home?e_success";
	}

}
