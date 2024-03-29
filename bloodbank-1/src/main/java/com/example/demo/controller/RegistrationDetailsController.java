package com.example.demo.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import com.example.demo.entity.RegistrationDetails;
import com.example.demo.service.RegistrationDetailsService;
import com.example.demo.service.UserService;

@Controller
public class RegistrationDetailsController {

	@Autowired
	private RegistrationDetailsService service;
	@Autowired 
	private UserService userService;
	
	@PostMapping("/register")  //1
	public String saveDetail(@ModelAttribute("detail") RegistrationDetails detail, Model model) {
	
		boolean status=userService.checkEmailExistance(detail);
		if(status) 
			return "userLogin";
//		detail.setRole("user");
		List<RegistrationDetails> saved = service.getRegistrationDetailsByEmail(detail.getEmail());
		for(RegistrationDetails ele: saved) {
			if(detail.getOtp() == ele.getOtp()) {
				ele.setFirstname(detail.getFirstname());
				ele.setLastname(detail.getLastname());
				ele.setPassword(detail.getPassword());
				ele.setRole("user");
				service.saveRegistrationDetails(ele);
				return "redirect:/registrationStatus";
			}
			
		}
		
//		service.deleteRegistrationDetail(detail.getEmail());
		model.addAttribute("otpMismatch", "Otp is not correct");
		return "userLogin";

	}
	
	@GetMapping("/getRegistrationDetails") //1
	public String getDetails(Model model) {
		List<RegistrationDetails> users = service.getRegistrationDetails();
		model.addAttribute("users", users);
//		List<RegistrationDetails> saved = service.getRegistrationDetailsByEmail("bodeddularajasekharreddy2002@gmail.com");
//    	for(RegistrationDetails detail:saved) {
//    		 System.out.println("Blood Group: " + detail.getBloodGroup());
//    		model.addAttribute("userData", detail);
//    	}
		return "details";
	}
	
	@GetMapping("/getRegistrationDetailsById/{id}") //1
	public Optional<RegistrationDetails> findDetailsById(@PathVariable("id") int id) {
		return service.getRegistrationDetailsById(id);
	}
	
	@GetMapping("/getRegistrationDetailsByEmail/{email}") //1
	public List<RegistrationDetails> findDetailsByEmail(@PathVariable("email") String email) {
		return service.getRegistrationDetailsByEmail(email);
	}
	
	@GetMapping("/getRegistrationDetailsByRole/{role}")//1
	public List<RegistrationDetails> findByLoginType(@PathVariable("role") String role) {
		return service.getRegistrationDetailsByRole(role);
	}
	
	
}
