package com.example.demo.webcontroller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.example.demo.entity.RegistrationDetails;
import com.example.demo.service.RegistrationDetailsService;

import jakarta.servlet.http.HttpSession;

@Controller
public class UserDashboard {

    @GetMapping("/dashboard")
    public String showDashboard() {
        return "dashboard";
    }

//    @GetMapping("/profile")
//    public String showProfile() {
//        return "profile";
//    }
    
    @Autowired
    private RegistrationDetailsService service;
    
    @GetMapping("/userProfile") 
	public String userProfile(HttpSession session, Model model) {
    	if (session.getAttribute("userEmail") == null) {
            // Session is valid, return the Thymeleaf template name for the user home page
            return "userLogin";
        } 
    	List<RegistrationDetails> saved = service.getRegistrationDetailsByEmail((String) session.getAttribute("userEmail"));
    	for(RegistrationDetails detail:saved) {
    		 System.out.println("Blood Group: " + detail.getBloodGroup());
    		model.addAttribute("userData", detail);
    	}
		return "userProfile";
	}

    @GetMapping("/bloodRequest")
    public String showBloodRequests() {
        return "bloodRequest";
    }
    
    @GetMapping("/userBloodRequestHistory")
    public String showBloodRequestsHistory() {
        return "userBloodRequestHistory";
    }
    
    @GetMapping("/userDonationRequestHistory")
    public String showDonationRequestsHistory() {
        return "userDonationRequestHistory";
    }
    

    @GetMapping("/donationRequest")
    public String showDonationRequests(HttpSession session, Model model) {
    	if (session.getAttribute("userEmail") == null) {
            // Session is valid, return the Thymeleaf template name for the user home page
            return "userLogin";
        } 
    	model.addAttribute("email", (String) session.getAttribute("email"));
    	
        return "donationRequest";
    }
    
    @GetMapping("/details")
    public String getMethodName() {
        return "details";
    }
    
    
}

