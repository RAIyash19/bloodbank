package com.example.demo.service;

import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.entity.DonorDetails;
import com.example.demo.entity.Inventory;
import com.example.demo.entity.PatientDetails;
import com.example.demo.entity.RegistrationDetails;

@Service
public class UserService {
	
	@Autowired
	private RegistrationDetailsService service;
	
	@Autowired
	private PatientDetailsService patientService;
	
	@Autowired
	private DonorDetailsService donateService;
	
	@Autowired
	private InventoryService inventoryService;
	
	public String verifyLogin(RegistrationDetails received) {
		
		List<RegistrationDetails> saved = service.getRegistrationDetailsByRole("user");
		for (RegistrationDetails detail:saved) {
			if (detail.getEmail().equals(received.getEmail())) {
				if (detail.getPassword().equals(received.getPassword())) {
					return "Login Success";	// Login success
				}
				else 
					return "Password incorrect";	// password incorrect
			}
		}
		
		return "invalid credentials";	// Invalid credential means Details not found
	}

	public boolean checkEmailExistance(RegistrationDetails received) {
		
		List<RegistrationDetails> saved = service.getRegistrationDetailsByRole("user");
		for (RegistrationDetails detail: saved) {
			if(detail.getEmail().equals(received.getEmail()))
				return true;	// Mail is already registered not possible to re register
		}
		return false;
	}

	public List<RegistrationDetails> getProfileDetails(String email) {
		
		return service.getRegistrationDetailsByEmail(email);
//		for (RegistrationDetails detail: saved) {
//			detail.toString();
//		}
//		return null;
	}

	public List<PatientDetails> getBloodRequestsDetails(String email) {
		
		List<PatientDetails> saved= patientService.getPatientsDetailsByEmail(email);
		List<PatientDetails> patients = new ArrayList<>();
		for (PatientDetails detail: saved) {
			if (detail.isStatus())
				patients.add(detail);
		}
		
		return patients;
		
	}

	public String findBloodDonationsCount(String email) {
		List<DonorDetails> saved = donateService.getDonorsDetailsByEmail(email);
		int count=0;
		for (DonorDetails detail: saved) {
			if (detail.isStatus())
				count++;
		}
		System.out.println("count : "+count);
		return "Blood Donation Count : " + count;
	}
	
	public String findBloodRequestsCount(String email) {
		
		List<PatientDetails> saved = patientService.getPatientsDetailsByEmail(email);
		int count=0;
		for(PatientDetails detail : saved) {
			if (detail.isStatus())
				count++;
		}
		return "Blood requests count : " + count;
	}

	public List<DonorDetails> getDonateRequestsDetails(String email) {
		List<DonorDetails> saved= donateService.getDonorsDetailsByEmail(email);
		List<DonorDetails> donors = new ArrayList<>();
		for (DonorDetails detail: saved) {
			if (detail.isStatus())
				donors.add(detail);
		}
		
		return donors;
	}

	public String donateRequest(DonorDetails received) {
		received.setStatus(false);
		String dob=received.getDateOfBirth();
		
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

	        // Parse the string to obtain a LocalDate object
	    LocalDate dob_format = LocalDate.parse(dob, formatter);
		Period period = Period.between(dob_format, LocalDate.now());

	        // Access the difference in years, months, and days
	    int years = period.getYears();
	        
        if (years < 18) {
        	return "Minors will not be allowed to Donate Blood, age should be minimum 18 years";
        }
        List<DonorDetails> saved = donateService.getDonorsDetailsByEmail(received.getEmail());
        long minDays=91;
        for (DonorDetails detail: saved) {	// to get the difference between recent blood donation date and current date
        	LocalDate lastDonation=LocalDate.parse(detail.getDateOfDonation(), formatter);
//        	LocalDate currentDate = LocalDate.parse(received.getDateOfDonation(), formatter);
        	long daysDifference = ChronoUnit.DAYS.between(lastDonation, LocalDate.now());
        	minDays = daysDifference;
//        	System.out.println("days: " + minDays);
        }
        if (minDays <= 90)
        	return "Less than in 90 days period of time is not allowed to donate blood again";
        
        received.setStatus(false);
        donateService.saveDonorDetails(received);
		return "You are eligible to donate, request needs to be accepted by admin";
	}

	public String bloodRequest(PatientDetails received) {
		
		if(received.getBloodUnits() > 5) {
			return "Not allowed to take blood more than 5 units";
		}
		List<Inventory> inventory = inventoryService.findByBloodGroup(received.getBloodGroup());
		int bloodUnits=0;
		for (Inventory detail: inventory) {
			if(detail.getQuantity() != 0) {
				bloodUnits += 1;
//				System.out.println("count: " + bloodUnits);
			}
		}
		if (received.getBloodUnits() > bloodUnits)
			return "There is no enough blood in the Inventory";
		received.setStatus(false);
		patientService.savePatientDetails(received);
		
		return "Blood is available, admin has to accept your request";
	}

}
