package com.example.demo.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.entity.DonorDetails;
import com.example.demo.entity.Inventory;
import com.example.demo.entity.PatientDetails;
import com.example.demo.entity.RegistrationDetails;
import com.example.demo.service.AdminService;
import com.example.demo.service.DonorDetailsService;
import com.example.demo.service.InventoryService;
import com.example.demo.service.PatientDetailsService;

@RestController
public class AdminController {
	
	
	@Autowired
	private AdminService service;
	
	@Autowired 
	private InventoryService inventoryService;
	
	@Autowired
	private DonorDetailsService donorDetailsService;
	
	@Autowired
	private PatientDetailsService patientDetailsService;
	
	@PostMapping("/verifyAdminLogin")
	public String verifyAdminLogin(@RequestBody RegistrationDetails received) {
		byte status = service.verifyLogin(received);
		if (status == 1)
			return "Admin Login Successful";
		else if (status == -1)
			return "Password incorrect";
		else
			return "Invalid credentials, there is no admin with mail \"" + received.getEmail() + "\"" ;
		
	}
//	
//	@GetMapping
//	public String getdetails
  @GetMapping("/getInventoryDetails")
	public List<Inventory> getDetails() {
		return inventoryService.getInventoryDetails();
		
	}
	
  
  @GetMapping("/getInventoryDetailsByBloodGroup/{BloodGroup}")
	public List<Inventory> findDetailsByBloodGroup(@PathVariable("BloodGroup") String bloodGroup) {
		return inventoryService.findByBloodGroup(bloodGroup);
		
	}
  
  
  @GetMapping("//getInventoryQuantityByBloodGroup")
  public List<Object[]> getQuantityByBloodGroup() {
      return inventoryService.findQuantityByBloodGroup();
  }
  
 
  @GetMapping("/getDonorDetails")
  public List<DonorDetails> getDonorDetails(){
	  return donorDetailsService.getDonorDetails();
  }
  
  @GetMapping("/getDonorDetailsByEmial/{email}")
  
  public List<DonorDetails> getDonorDetailsByEmail(@PathVariable("email") String email){
	  return donorDetailsService.findByemail(email);
  }
  
  @GetMapping("/getPatientDetailsByEmail/{email}")
	public List<PatientDetails> getPatientDetailsByEmail(@PathVariable String email) {
		return patientDetailsService.getPatientsDetailsByEmail(email);
	}
	
  
  @GetMapping("/clearExpiry")
  public List<Inventory> checkBlood() {
	  List<Inventory> donors = inventoryService.getInventoryDetails();
	  return inventoryService.checkForOldBloodSamples(donors);
  }
  
  @GetMapping("/eligibleToDonate")
  public List<DonorDetails> checkEligibility(){
  List<DonorDetails> donors = donorDetailsService.getDonorDetails();
  
      return donorDetailsService.checkEligibility(donors);
  }
  
  
  
}
