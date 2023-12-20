package com.example.demo.service;


import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.entity.DonorDetails;
import com.example.demo.entity.Inventory;
import com.example.demo.entity.RegistrationDetails;


@Service
public class AdminService {

	
	@Autowired
	private RegistrationDetailsService service;
	
	@Autowired
	private DonorDetailsService donorService;
	
	@Autowired
	private InventoryService inventoryService;
	
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
		return donorService.getDonordetailsByStatus(false);
	}


	public String acceptDonationRequest(DonorDetails received) {
		
		List<DonorDetails> saved = donorService.getDonorsDetailsByEmail(received.getEmail());
		for (DonorDetails detail:saved) {
			if (detail.isStatus() == false) {
				detail.setStatus(true);
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

}
