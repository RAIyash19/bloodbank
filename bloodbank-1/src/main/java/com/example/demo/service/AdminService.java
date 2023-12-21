package com.example.demo.service;


import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
	
	
	public List<DonorDetails> getDonationRequests() {
		return donorService.getDonordetailsByStatus((byte) 0);
	}

// reject status maake it null funcvtion
	
	
	public String acceptDonationRequest(DonorDetails received) {
		
		
		
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
		
		return "Donation request accepted for the user with email " + received.getEmail();
	}


	public  List<PatientDetails> viewBloodRequest() {
		return patientService.getPatientDetailsByStatus((byte) 0);
	}


	public String acceptBloodRequest(PatientDetails received) {
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
		return "Given " + received.getBloodUnits() + "units or blood successfully";
	}


	public String rejectDonationRequest(DonorDetails detail) {
		
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
		
		return "no Blood Donation request is there to reject";
		
	}


	public String rejectBloodRequest(PatientDetails detail) {
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
		
		return "no Blood request is there to reject";
	}

}
