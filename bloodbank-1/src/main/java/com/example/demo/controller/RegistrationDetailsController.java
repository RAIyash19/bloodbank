package com.example.demo.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.entity.RegistrationDetails;
import com.example.demo.service.RegistrationDetailsService;
import com.example.demo.service.UserService;

@RestController
public class RegistrationDetailsController {

	
	@Autowired
	private RegistrationDetailsService service;
	@Autowired 
	private UserService registerService;
	
	@PostMapping("/addRegistrationDetails")
	public String saveDetail(@RequestBody RegistrationDetails detail) {
		
		boolean status= registerService.checkEmailExistance(detail);
		if (status)
			return "User is already having account, try to login";
		detail.setRole("user");
		return service.saveRegistrationDetails(detail);
	}
	
	@GetMapping("/getRegistrationDetails")
	public List<RegistrationDetails> getDetails() {
		return service.getRegistrationDetails();
		
	}
	
	@GetMapping("/getRegistrationDetailsById/{id}")
	public Optional<RegistrationDetails> findDetailsById(@PathVariable("id") int id) {
		return service.getRegistrationDetailsById(id);
	}
	
	@GetMapping("/getRegistrationDetailsByEmail/{email}")
	public List<RegistrationDetails> findDetailsByEmail(@PathVariable("email") String email) {
		return service.getRegistrationDetailsByEmail(email);
	}
	
	@GetMapping("/getRegistrationDetailsByRole/{role}")
	public List<RegistrationDetails> findByLoginType(@PathVariable("role") String role) {
		return service.getRegistrationDetailsByRole(role);
	}
	
	
	
	
	
}
