package com.example.demo.service;


import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.demo.emailService.SendEmailService;
import com.example.demo.entity.DonorDetails;
import com.example.demo.entity.Inventory;
import com.example.demo.entity.PatientDetails;
import com.example.demo.entity.RegistrationDetails;


@Service
public class AdminService {

	
	@Autowired
	private RegistrationDetailsService service;
	
	@Autowired
	private DonorDetailsService donorService;
	
	@Autowired
	private InventoryService inventoryService;
	
	@Autowired
	private PatientDetailsService patientService;
	
	@Autowired
	private SendEmailService emailService;
	
//	public byte verifyLogin(RegistrationDetails received) {
//		
//		BCryptPasswordEncoder bcrypt = new BCryptPasswordEncoder();
//		System.out.println("yyyyyyy");
//		List<RegistrationDetails> saved = service.getRegistrationDetailsByRole("admin"); //received.getRole("admin");
//		System.out.println(saved);
//		for (RegistrationDetails detail: saved) {
//			System.out.println(detail);
//			if (detail.getEmail().equals(received.getEmail())) {
//				System.out.println(detail.getEmail());
//				
//				if (bcrypt.matches(received.getPassword(), detail.getPassword()))
//				{
//					return 1; // Successful admin login
//				}
//				else 
//					return -1; // Password incorrect
//			}
//			else {
//				return 0;
//		}
//		 // Details not found
//	}
//		return 0;
//	}
	public byte verifyLogin(RegistrationDetails received) {
	    BCryptPasswordEncoder bcrypt = new BCryptPasswordEncoder();
	    System.out.println(received.getEmail());
	    
	    List<RegistrationDetails> saved = service.getRegistrationDetailsByRole("admin");

	    System.out.println(saved);

	    for (RegistrationDetails detail : saved) {
	        System.out.println(detail);

	        if (detail.getEmail().equals(received.getEmail())) {
	            System.out.println(detail.getEmail());

	            if (bcrypt.matches(received.getPassword(), detail.getPassword())) {
	                return 1; // Successful admin login
	            } else {
	                return -1; // Password incorrect
	            }
	        }
	    }

	    // If no matching email is found
	    return 0;
	}

	
	public List<DonorDetails> getDonationRequests() {
		return donorService.getDonordetailsByStatus((byte) 0);
	}

// reject status maake it null funcvtion
	
	public String acceptDonationRequest(DonorDetails received) {
		String email=received.getEmail();
		List<DonorDetails> saved = donorService.getDonorsDetailsByEmail(received.getEmail());
		for (DonorDetails detail:saved) {
			if (detail.getStatus()==1) {
				return "why are you accepting again and again, go and some other work";
			}
			if (detail.getStatus() == 0) {
				detail.setStatus((byte) 1);
			}
			Inventory inv = new Inventory();
			inv.setBloodGroup(detail.getBloodGroup());
			inv.setDateOfDonation(detail.getDateOfDonation());
			inv.setQuantity(1);
			inventoryService.saveInventory(inv);
			donorService.saveDonorDetails(detail);
		}
		//
		emailService.sendEmail(email,"this is to inform you", "your blood donation request is accepted");
		return "Donation request accepted for the user with email " + received.getEmail();
	}


	public  List<PatientDetails> viewBloodRequest() {
		return patientService.getPatientDetailsByStatus((byte) 0);
	}


	public String acceptBloodRequest(PatientDetails received) {
		String email=received.getEmail();
		List<PatientDetails> saved = patientService.getPatientsDetailsByEmail(received.getEmail());
		
		
		for (PatientDetails detail: saved) {
			if (detail.getStatus()==1) {
				return "why are you accepting again and again, go and some other work";
			}
			
			if (detail.getStatus() == 0)
				detail.setStatus((byte) 1);
			
			
			int units = detail.getBloodUnits();
			List<Inventory> saved1 = inventoryService.getInventoryDetailsByBloodGroup(detail.getBloodGroup());
			
			for (Inventory inv:saved1) {
				if (inv.getQuantity()!= 0) {
					units--;
					inventoryService.deleteInventory(inv);
					if (units == 0)
						break;
				}	
			}
			
			patientService.savePatientDetails(detail);
		}
		emailService.sendEmail(email,"this is to inform you", "your blood request is accepted");
		return "Given " + received.getBloodUnits() + "units or blood successfully";
	}


	public String rejectDonationRequest(DonorDetails detail) {
		String email=detail.getEmail();
		List<DonorDetails> saved= donorService.getDonorsDetailsByEmail(detail.getEmail());
		List<DonorDetails> donors = new ArrayList<>();
		for (DonorDetails detail1: saved) {
			if(detail1.getStatus()==0) {
				//donors.add(detail1);
				//saveDonorDetails(detail);
				
				detail1.setStatus((byte) -1);
				donorService.updateStatus(detail1);
//				Inventory inventory = convertDonorDetailsToInventory(detail);
//				inventoryrepo.updateStatus(inventory);
				
				return " Blood Donatio request rejected";
			}
			
		}
		emailService.sendEmail(email,"this is to inform you", "your blood donation request is rejected");
		
		return "no Blood Donation request is there to reject";
		
	}


	public String rejectBloodRequest(PatientDetails detail) {
		String email=detail.getEmail();
		List<PatientDetails> saved= patientService.getPatientsDetailsByEmail(detail.getEmail());
		//List<DonorDetails> donors = new ArrayList<>();
		for (PatientDetails detail1: saved) {
			if(detail1.getStatus()==0) {
				//donors.add(detail1);
				//saveDonorDetails(detail);
				
				detail1.setStatus((byte) -1);
				patientService.updateStatus(detail1);
//				Inventory inventory = convertDonorDetailsToInventory(detail);
//				inventoryrepo.updateStatus(inventory);
				
				return "Blood request rejected";
			}
			
		}
		emailService.sendEmail(email,"this is to inform you", "your blood request is rejected");
		return "no Blood request is there to reject";
	}

}
