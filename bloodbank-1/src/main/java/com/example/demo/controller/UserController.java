package com.example.demo.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.entity.DonorDetails;
import com.example.demo.entity.PatientDetails;
import com.example.demo.entity.RegistrationDetails;
import com.example.demo.service.UserService;

@RestController
public class UserController {
	
	@Autowired
	private UserService loginService;
	
	
	@PostMapping("/verifyLogin")
	public String verifyLogin(@RequestBody RegistrationDetails received) {
		return  loginService.verifyLogin(received);
	}
	
	@GetMapping("/viewProfileDetails/{email}")
	public List<RegistrationDetails> viewProfileDetails(@PathVariable("email") String email) {
		return loginService.getProfileDetails(email);
	}
	
	@GetMapping("/viewBloodRequestDetails/{email}")
	public List<PatientDetails> viewBloodRequestsDetails(@PathVariable("email") String email) {
		return loginService.getBloodRequestsDetails(email);
	}
	
	@GetMapping("/viewDonateRequestDetails/{email}")
	public List<DonorDetails> getDonateRequestsDerails(@PathVariable("email") String email) {
		return loginService.getDonateRequestsDetails(email);
	}
	
	@GetMapping("/viewBloodDonationCount/{email}")
	public String getBloodDonationCount(@PathVariable("email") String email) {
		return loginService.findBloodDonationsCount(email);
	}
	
	@GetMapping("/viewBloodRequestCount/{email}")
	public String getBloodRequestCount(@PathVariable("email") String email) {
		return loginService.findBloodRequestsCount(email);
	}
	
	@PostMapping("/bloodDonationRequest")
	public String donateRequest(@RequestBody DonorDetails received) {
		System.out.println(received);
		return loginService.donateRequest(received);
	}
	
	@PostMapping("/bloodRequest")
	public String bloodRequest(@RequestBody PatientDetails detail) {
		return loginService.bloodRequest(detail);
	}
	

}
