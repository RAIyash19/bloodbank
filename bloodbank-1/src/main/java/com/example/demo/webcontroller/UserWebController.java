package com.example.demo.webcontroller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.demo.entity.LoggedInUser;

import jakarta.servlet.http.HttpSession;

@Controller
public class UserWebController {
	
	@GetMapping("/") 
	public String home () {
		System.out.println("IN");
		return "index";
	}
	
	@GetMapping("/userLogin")
	public String userlogin() {
		return "userLogin";
	}
	
	@GetMapping("/dashboard_u" )
	public String userDashboard() {
		return "dashboard_u";
	}

	 @GetMapping("/userRegistration")
	    public String userRegistration() {
	        return "userRegistration";
	    }
	
	@GetMapping("/registrationStatus")
	public String registrationStatus() {
		return "registrationStatus";
	}
	
//	@GetMapping("/userHome")
//	public String userHome() {
//		return "userHome";
//	}
	@GetMapping("/forgetPassword")
	public String forgetPassword() {
		return "forgetPassword";
	}
	@GetMapping("/forgetPasswordadmin")
		public String forgetPasswordadmin() {
			return "forgetPasswordadmin";
		}
	
	@GetMapping("/userHome")
	public String check(Model model) {
//		int numberOfDonations = 5; // Fetch from backend;
//		        int numberOfRequests = 10;// Fetch from backend;
//		        String username ="raja"; // Fetch from backend;
//
//		        // Add data to the Thymeleaf model
//		        model.addAttribute("username", username);
//		        model.addAttribute("donations", numberOfDonations);
//		        model.addAttribute("requests", numberOfRequests);

		return "userHome";
	}
	
	@GetMapping("/userProfile") 
	public String userProfile() {
		return "userProfile";
	}
	
	
	
	@GetMapping("/adminLogin")
	public String adminLogin(){
	
		return "adminLogin";
	}
	
//	@GetMapping("/adminHome")
//	public String adminHome() {
//		return "adminHome";
//	}
	
	@GetMapping("/adminHome")
    public String adminHome(Model model, HttpSession session) {
		 String email = (String) session.getAttribute("loggedInUserEmail");

		    // Pass the user's email to the Thymeleaf template
		    model.addAttribute("email", email);
		 

        return "adminHome";
    }
	
	@GetMapping("/adminProfile")
	public String adminProfile() {
		return "adminProfile";
	}
	
	
	@GetMapping("/adminfetch")
	public String adminfetch(Model model, HttpSession session) {
		String email = (String) session.getAttribute("loggedInUserEmail");
		
	    model.addAttribute("email", email);
		
		return "adminfetch";
	}
	@GetMapping("/profileDisplay")
	public String profileDisplay() {
		return "profileDisplay";
	}
	
	
}
