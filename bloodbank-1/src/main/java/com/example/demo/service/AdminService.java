package com.example.demo.service;


import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.entity.RegistrationDetails;


@Service
public class AdminService {

	
	@Autowired
	private RegistrationDetailsService service;
	public byte verifyLogin(RegistrationDetails received) {
		
		List<RegistrationDetails> saved = service.getRegistrationDetailsByRole("admin");
		for (RegistrationDetails detail: saved) {
			if (detail.getEmail().equals(received.getEmail())) {
				if (detail.getPassword().equals(received.getPassword()))
					return 1; // Successful admin login
				else 
					return -1; // Password incorrect
			}
		}
		return 0; // Details not found
	}

}
