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
import com.example.demo.entity.LoggedInUser;
import com.example.demo.entity.PatientDetails;
import com.example.demo.entity.RegistrationDetails;
import com.example.demo.service.AdminService;
import com.example.demo.service.DonorDetailsService;
import com.example.demo.service.InventoryService;
import com.example.demo.service.PatientDetailsService;

import com.example.demo.service.RegistrationDetailsService;
import com.example.demo.service.UserService;

import jakarta.servlet.http.HttpSession;


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
	
//	@GetMapping("/verifyAdminLogin")
//	public String verifyAdminLogin(@ModelAttribute("received") RegistrationDetails received, Model model) {
//		System.out.println(received.getEmail());
//		 int status = service.verifyLogin(received);
//        // System.out.println(status);
//		 System.out.println(status
//				 );
//        if (status == 1) {
//            // If login is successful, return the Thymeleaf template name for redirection
//            //return "redirect:/dashboard_u";
//        	  //return "userDashboard";
////        	 LoggedInUser loggedInUser = new LoggedInUser();
////             loggedInUser.setEmail(received.getEmail());
////             session.setAttribute("loggedInUser", loggedInUser);
//        	
//
//        	return "redirect:/adminHome";
//        } 
//        else if (status == 0) {
//        	model.addAttribute("invalidMail", "this email is not admin");
//        	return "adminLogin";
//            
//        }
////        else if(status == -1) {
////            // If login fails, add an error message to the model and stay on the login page
////            model.addAttribute("error", "Invalid username or password");
////        }
//        else if (status == -1) {
//            // If login fails, add an error message to the model and stay on the login page
//            model.addAttribute("error", "Invalid username or password");
//
//        }
//        return "adminLogin";
//        }
	
	
	
	@GetMapping("/verifyAdminLogin")
    public String verifyAdminLogin(@ModelAttribute("received") RegistrationDetails received, 
                                   HttpSession session, Model model) {
		System.out.println(received.getEmail());
        int status = service.verifyLogin(received);

        if (status == 1) {
            // If login is successful, set the email as a session attribute
            session.setAttribute("loggedInUserEmail", received.getEmail());

            // Redirect to the admin home page
            return "redirect:/adminHome";
        } else if (status == 0) {
            model.addAttribute("invalidMail", "This email is not admin");
            return "adminLogin";
        } else if (status == -1) {
            model.addAttribute("error", "Invalid username or password");
        }

        // If login fails, stay on the login page
        return "adminLogin";
    }
	
//
//	 @GetMapping("/adminHome")
//    public String adminHome(Model model, HttpSession session) {
//        // Retrieve the loggedInUser from the session
//		 
//		 String email = (String) session.getAttribute("loggedInUserEmail");
//
//		    // Pass the user's email to the Thymeleaf template
//		    model.addAttribute("email", email);
//		 
////        LoggedInUser loggedInUser = (LoggedInUser) session.getAttribute("loggedInUser");
//
//        // Pass the user's email to the Thymeleaf template
////        model.addAttribute("email", loggedInUser.getEmail());
//
//        return "adminHome";
//    }

	
	
	
	
	
//	@GetMapping("/viewProfileDetail")//1
//	public String viewProfileDetail(@ModelAttribute("detail") RegistrationDetails detail, Model model) {
//		/*
//		 * List<RegistrationDetails> userDetails = service.getAllUserDetails();
//		 * model.addAttribute("users", userDetails);
//		 */
//		
//		
//		model.addAttribute("user", service.getProfileDetails(detail));
//		
//		
//		return "adminfetch";
//	
//	}
	
	@GetMapping("/viewProfileDetail")
	public String viewProfileDetail(HttpSession session, Model model) {
	    // Retrieve the email from the session
	    String adminEmail = (String) session.getAttribute("loggedInUserEmail");
	    System.out.println(adminEmail);
	    System.out.println(adminEmail);
	    System.out.println(adminEmail);
	    System.out.println(adminEmail);

	    if (adminEmail == null) {
	        // Handle the case where the admin is not logged in
	        return "redirect:/adminLogin";  // Redirect to the admin login page or handle appropriately
	    }

	    // Add logic to fetch user details based on the admin's email
	    List<RegistrationDetails> userDetails = registrationDetailsService.getRegistrationDetailsByEmail(adminEmail);
	    for(RegistrationDetails a:userDetails) {
	    	System.out.println(a.getBloodGroup());
	    	System.out.println(a.getCity());
	    	System.out.println(a.getFirstname());
	    }

	    // Add the user details to the model
	    model.addAttribute("user", userDetails);

	    // Return the Thymeleaf template name
	    return "adminfetch";
	}

	
	

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
	public String updateUserProfile(@ModelAttribute("detail") RegistrationDetails detail, Model model)
	{
	  System.out.println(detail.getEmail());
	  service.updateUserProfile(detail);
//		return new ResponseEntity<RegistrationDetails>(detail, HttpStatus.OK);
	  return "redirect:/adminProfile";
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
  
  
  

