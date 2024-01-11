package com.example.demo.webcontroller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.example.demo.entity.RegistrationDetails;
import com.example.demo.service.RegistrationDetailsService;

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
	public String userProfile(Model model) {
    	List<RegistrationDetails> saved = service.getRegistrationDetailsByEmail("bodeddularajasekharreddy2002@gmail.com");
    	for(RegistrationDetails detail:saved) {
    		 System.out.println("Blood Group: " + detail.getBloodGroup());
    		model.addAttribute("userData", detail);
    	}
		return "userProfile";
	}

    @GetMapping("/blood-requests")
    public String showBloodRequests() {
        return "bloodRequests";
    }

    @GetMapping("/donation-requests")
    public String showDonationRequests() {
        return "donationRequests";
    }
    
    @GetMapping("/details")
    public String getMethodName() {
        return "details";
    }
    
}

