package com.example.demo.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.entity.DonorDetails;
import com.example.demo.entity.PatientDetails;
import com.example.demo.entity.RegistrationDetails;
import com.example.demo.service.RegistrationDetailsService;
import com.example.demo.service.UserService;

@Controller
public class UserController {
	
	@Autowired
	private UserService loginService;
	
	@Autowired
	private RegistrationDetailsService registerService;
	
	
	@GetMapping("/verifyUserLogin")//1
	public String verifyLogin(@ModelAttribute("received") RegistrationDetails received, Model model) {
        int status = loginService.verifyLogin(received);

        if (status == 1) {
            // If login is successful, return the Thymeleaf template name for redirection
            //return "redirect:/dashboard_u";
        	  //return "userDashboard";
        	return "redirect:/check";
        } 
        else if (status == 0) {
        	model.addAttribute("message", "Email is invalid");
        }
        else {
            // If login fails, add an error message to the model and stay on the login page
            model.addAttribute("error", "Invalid password");
        }
        return "userLogin"; 
    }
	
	@PostMapping("/sendOTP/{email}")
	@ResponseStatus(HttpStatus.OK)
	@ResponseBody
	
	public void sendOtp(@PathVariable("email") String email, Model model) {
		int status = loginService.sendOtp(email);
		if (status ==1) {
			model.addAttribute("message", "User aleady existing");
		}
		System.out.println("request mapped " + email);
		
	}
	

	
	@PostMapping("/resetPassword")
//	public String resetPassword(@PathVariable("email") String email,@PathVariable("otp") int otp,@PathVariable("password") String password, Model model) {
	public String resetPassword(@ModelAttribute("detail") RegistrationDetails detail, Model model) {
		
		int status = loginService.resetPassword(detail.getEmail(), detail.getOtp(),detail.getPassword(), model);
		if (status==1) 
			return "redirect:/userLogin";
		return "foregetPassword";
	}
	
	@GetMapping("/viewProfileDetails/{email}")//1
	public List<RegistrationDetails> viewProfileDetails(@PathVariable("email") String email) {
		return loginService.getProfileDetails(email);
	}
	
	@PostMapping("/updateUserDetails")//1
	public String updateProfile(@ModelAttribute("received") RegistrationDetails received, Model model) {
		 System.out.println("update");
		 System.out.println(received);
		 loginService.updateProfile(received);
		 model.addAttribute("userData", received);
		 model.addAttribute("userUpdate", "Details updated successfully");
		 return "userProfile";
	}
	
//	@GetMapping("/getDonationAndBloodCount/{email}")
//	@ResponseStatus(HttpStatus.OK)
//	@ResponseBody
//	public void getDonationAndBloodCount(@PathVariable("email") String email) {
//		
//		int bloodRequests = loginService.getBloodRequestsCount(email);
//		int donationRequests = loginService.getDonationCount(email);
//		
//	}
	
	@GetMapping("/viewBloodRequestDetails/{email}")//1
	public List<PatientDetails> viewBloodRequestsDetails(@PathVariable("email") String email) {
		return loginService.getBloodRequestsDetails(email);
	}
	
	@GetMapping("/viewDonateRequestDetails/{email}")//1------------------------------------------------------
	public String getDonateRequestsDerails(@PathVariable("email") String email) {
		
		 loginService.getDonateRequestsDetails(email);
		 return "userProfile";
	}
	
	@GetMapping("/viewAcceptedBloodDonationCount/{email}")//1
	public String getBloodDonationCount(@PathVariable("email") String email) {
		return loginService.findBloodDonationsCount(email);
	}
	
	@GetMapping("/viewAcceptedBloodRequestCount/{email}") // acepted requests
	public String getBloodRequestCount(@PathVariable("email") String email) {
		return loginService.findBloodRequestsCount(email);
	}
	
	@PostMapping("/bloodDonationRequest")//1
	public String donateRequest(@RequestBody DonorDetails received) {
		System.out.println(received);
		return loginService.donateRequest(received);
	}
	
	@PostMapping("/bloodRequest")  //1
	public String bloodRequest(@RequestBody PatientDetails detail) {
		return loginService.bloodRequest(detail);
	}
	
	
	

}
