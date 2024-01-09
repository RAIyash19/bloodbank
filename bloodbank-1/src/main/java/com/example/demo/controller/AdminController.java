package com.example.demo.controller;

import java.util.List;

import java.util.Map;
import java.util.Optional;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
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


@Controller
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
	
	@GetMapping("/verifyAdminLogin")
	public String verifyAdminLogin(@ModelAttribute("received") RegistrationDetails received, Model model) {
		 int status = service.verifyLogin(received);
         System.out.println(status);
        if (status == 1) {
            // If login is successful, return the Thymeleaf template name for redirection
            //return "redirect:/dashboard_u";
        	  //return "userDashboard";
        	return "redirect:/adminHome";
        } 
        else if (status == 0) {
        	model.addAttribute("invalidMail", "Invalid credentials");
            
        }
        else {
            // If login fails, add an error message to the model and stay on the login page
            model.addAttribute("error", "Invalid username or password");
        }
        return "adminLogin";
		
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
  
  
//  @GetMapping("//getInventoryQuantityByBloodGroup")
//  public Map<String, Integer> getBloodGroupCounts() {
//      return inventoryService.getInventoryByBloodGroup();
//  }
//  public List<Inventory> getQuantityByBloodGroup() {
//      return inventoryService.findQuantityByBloodGroup();
//  }
//  
  
//  @GetMapping("/inventory/{bloodGroup}")
//  public List<Inventory> getInventoryByBloodGroup(@PathVariable String bloodGroup) {
//      return inventoryService.getInventoryByBloodGroup(bloodGroup);
//  }
  
  @GetMapping("/getCountByBloodGroup/{bloodGroup}")
  public Map<String, Long> getCountByBloodGroup(@PathVariable String bloodGroup) {
      return inventoryService.getCountByBloodGroup(bloodGroup);
  }
 
  @GetMapping("/getDonorDetails")
  public List<DonorDetails> getDonorDetails(){
	  return donorDetailsService.getDonorDetails();
  }
  
  @GetMapping("/getDonorDetailsByEmial/{email}") 
  public List<DonorDetails> getDonorDetailsByEmail(@PathVariable("email") String email){
	  return donorDetailsService.findByemail(email);
  }
  
//  @GetMapping("/getDonorDetailsByEmial/{email}")
//	public List<PatientDetails> getPatientDetailsByEmail(@PathVariable String email) {
//		return patientDetailsService.getPatientsDetailsByEmail(email);
//	}
	
  
  @GetMapping("/clearExpiry")//1
  public List<Inventory> checkBlood() {
	  List<Inventory> donors = inventoryService.getInventoryDetails();
	  return inventoryService.checkForOldBloodSamples(donors);
  }
  
  @GetMapping("/eligibleToDonate")//1
  public List<DonorDetails> checkEligibility(){
  List<DonorDetails> donors = donorDetailsService.getDonorDetails();
  
      return donorDetailsService.checkEligibility(donors);
  }
  
  @PostMapping("/updateDetails")//1
	public ResponseEntity<RegistrationDetails> updateUserProfile(@RequestBody RegistrationDetails user) throws Exception
	{
	  registrationDetailsService.updateUserProfile(user);
		return new ResponseEntity<RegistrationDetails>(user, HttpStatus.OK);
	}

  
  	@GetMapping("/viewDonationRequests")//1
  	public List<DonorDetails> viewDonationRequests() {
  		return service.getDonationRequests();
  	}

	@PostMapping("/acceptDonationRequest")//1
	public String acceptDonationRequest(@RequestBody DonorDetails received){
		return service.acceptDonationRequest(received);
	}
	
	
	
	@GetMapping("/viewBloodRequests")//1
	public List<PatientDetails> viewBloodRequest(){
		return  service.viewBloodRequest();
	}
	
	@PostMapping("/acceptBloodRequest")//1
	public String acceptBloodRequest(@RequestBody PatientDetails received) {
		return service.acceptBloodRequest(received);
	}
	

	
	
	
	 @PostMapping("/rejectBloodDonationRequest")//1
	public String rejectDonationRequest(@RequestBody DonorDetails detail ) {
		return service.rejectDonationRequest(detail);
		
	}
	
	@PostMapping("/rejectBloodRequest")//1
	public String rejectBloodRequest(@RequestBody PatientDetails detail ) {
		return service.rejectBloodRequest(detail);
		
	}
	
}
  
  
  

