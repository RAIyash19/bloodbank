package com.example.demo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class WebController {
	
	@RequestMapping("/") 
	public String home () {
//		System.out.println("IN");
		return "index";
	}
	
	@RequestMapping("/userLogin")
	public String userlogin() {
		return "userLogin";
	}
	
	@RequestMapping("/userDashboard")
	public String userDashboard() {
		return "userDashboard";
	}
	
	
	

}
