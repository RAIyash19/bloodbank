package com.example.demo.webcontroller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class UserDashboard {

    @GetMapping("/dashboard")
    public String showDashboard() {
        return "dashboard";
    }

    @GetMapping("/profile")
    public String showProfile() {
        return "profile";
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

