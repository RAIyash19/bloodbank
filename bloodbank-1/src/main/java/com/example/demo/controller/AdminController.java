package com.example.demo.controller;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
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
import com.example.demo.service.RegistrationDetailsService;
import com.example.demo.service.UserService;

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
	
	@Autowired
	private RegistrationDetailsService registrationDetailsService;
	
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
  public Map<String, Integer> getBloodGroupCounts() {
      return inventoryService.findQuantityByBloodGroup();
  }
//  public List<Inventory> getQuantityByBloodGroup() {
//      return inventoryService.findQuantityByBloodGroup();
//  }
//  
 
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
  
  @PostMapping("/updateDetails")
	public ResponseEntity<RegistrationDetails> updateUserProfile(@RequestBody RegistrationDetails user) throws Exception
	{
	  registrationDetailsService.updateUserProfile(user);
		return new ResponseEntity<RegistrationDetails>(user, HttpStatus.OK);
	}
}
  
  
  

