package com.example.demo.service;

import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Random;

import javax.imageio.spi.RegisterableService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import com.example.demo.emailService.SendEmailService;
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
	
	@Autowired
	private SendEmailService emailService;
	
	
	
	
	public int verifyLogin(RegistrationDetails received) {
		
		BCryptPasswordEncoder bcrypt = new BCryptPasswordEncoder();
		List<RegistrationDetails> saved = service.getRegistrationDetailsByEmail(received.getEmail());
		for (RegistrationDetails detail:saved) {
			if (detail.getEmail().equals(received.getEmail()) && detail.getRole().equals("user")) {
				System.out.println(detail);
				// matches(raw password, hashed password)
				if (bcrypt.matches(received.getPassword(), detail.getPassword()))  {
//				if(received.getPassword().equals(detail.getPassword()))
					return 1;	// Login success
				}
				else 
					return -1;	// password incorrect
			}
			else {
				return 0;
			}
		}
		
		return 0;	// Invalid credential means Details not found
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
	
	public RegistrationDetails updateProfile(RegistrationDetails received) {
		
		List<RegistrationDetails> saved = service.getRegistrationDetailsByEmail(received.getEmail());
		for (RegistrationDetails detail:saved) {
			detail.setLastname(received.getLastname());
			detail.setFirstname(received.getFirstname());
			service.updateUserProfile(detail);
			System.out.println("name : " + received.getEmail());
			return detail;
		}
		
		return null;
		
	}

	public List<PatientDetails> getBloodRequestsDetails(String email) {
		
		List<PatientDetails> saved= patientService.getPatientsDetailsByEmail(email);
		List<PatientDetails> patients = new ArrayList<>();
		for (PatientDetails detail: saved) {
			//if (detail.getStatus()==1)
				patients.add(detail);
		}
		
		return patients;
		
	}

	public String findBloodDonationsCount(String email) {
		List<DonorDetails> saved = donateService.getDonorsDetailsByEmail(email);
		int count=0;
		for (DonorDetails detail: saved) {
			if (detail.getStatus()==1)
				count++;
		}
		System.out.println("count : "+count);
		return "Blood Donation Count : " + count;
	}
	
	public String findBloodRequestsCount(String email) {
		
		List<PatientDetails> saved = patientService.getPatientsDetailsByEmail(email);
		int count=0;
		for(PatientDetails detail : saved) {
			if (detail.getStatus()==1)
				count++;
		}
		return "Blood requests count : " + count;
	}

	public List<DonorDetails> getDonateRequestsDetails(String email) {
		List<DonorDetails> saved= donateService.getDonorsDetailsByEmail(email);
		List<DonorDetails> donors = new ArrayList<>();
		for (DonorDetails detail: saved) {
			//if (detail.getStatus()==1)
				donors.add(detail);
		}
		
		return donors;
	}

	public String donateRequest(DonorDetails received) {
		String email=received.getEmail();
		List<RegistrationDetails> saved1 = service.getRegistrationDetailsByEmail(received.getEmail());
		LocalDate currentDate = LocalDate.now();

        // Define the desired date format
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        // Format the local date using the specified formatter
        String formattedDate = currentDate.format(formatter);
		
		if (saved1.isEmpty())
			return "email not dound";
		for (RegistrationDetails detail: saved1) {
			//DonorDetails received = new DonorDetails();
			received.setEmail(detail.getEmail());
			received.setBloodGroup(detail.getBloodGroup());
//			received.setCity(detail.getCity());
			received.setDateOfBirth(detail.getDateOfBirth());
			received.setFirstname(detail.getFirstname());
			received.setLastname(detail.getLastname());
			received.setGender(detail.getGender());
			received.setDateOfDonation(formattedDate); 
			
		}
		
		received.setStatus((byte) 0);
		String dob=received.getDateOfBirth();
		
		//DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");	        // Parse the string to obtain a LocalDate object
	    LocalDate dob_format = LocalDate.parse(dob, formatter);
		Period period = Period.between(dob_format, LocalDate.now());

	        // Access the difference in years, months, and days
	    int years = period.getYears();
	        
        if (years < 18) {
        	return "hey kid  " + received.getEmail() + ",  you are not allowed to Donate Blood, age should be minimum 18 years";
        }
        if (years > 65)
        	return "Older people are not allowe to donate";
        List<DonorDetails> saved = donateService.getDonorDetailsByEmailAndStatus(received.getEmail(), (byte) 0);
        long minDays=91;
        for (DonorDetails detail: saved) {	// to get the difference between recent blood donation date and current date
        	if (detail.getStatus() == 0)
        		return "You can't make a request again without the last request getting verified";
        	
        	LocalDate lastDonation=LocalDate.parse(detail.getDateOfDonation(), formatter);
//        	LocalDate currentDate = LocalDate.parse(received.getDateOfDonation(), formatter);
        	long daysDifference = ChronoUnit.DAYS.between(lastDonation, LocalDate.now());
        	minDays = daysDifference;
//        	System.out.println("days: " + minDays);
        }
        if (minDays <= 90)
        	return "Less than in 90 days period of time is not allowed to donate blood again";
        
        received.setStatus((byte) 0);
        donateService.saveDonorDetails(received);
		
		emailService.sendEmail(email,"This is Confidential", "This is  Body of Email\n OTP is ");
		return "You are eligible to donate, request needs to be accepted by admin";
	}

	public String bloodRequest(PatientDetails received) {
		
		List<PatientDetails> saved = patientService.getPatientDetailsByEmailAndStatus(received.getEmail(), (byte) 0);
		
		for (PatientDetails detail: saved) {
			if (detail.getStatus() == 0)
				return "You can't made blood request again until the last request being verified";
		}
		
		
		if(received.getBloodUnits() > 5) {
			return "Not allowed to take blood more than 5 units";
		}
		List<Inventory> inventory = inventoryService.findByBloodGroup(received.getBloodGroup());
		int bloodUnits=0;
		for (Inventory detail: inventory) {
			if(detail.getQuantity() != 0) {
				bloodUnits += 1;
//				System.out.println("count: " + bloodUnits);.
				
			}
		}
		if (received.getBloodUnits() > bloodUnits)
			return "There is no enough blood in the Inventory";
		received.setStatus((byte) 0);
		patientService.savePatientDetails(received);
		
		return "Blood is available, admin has to accept your request";
	}
	
	public int sendOtp(String email) { 
	
	   
		
		Random random = new Random();
		System.out.println("started email");
        // Generate a random 6-digit number
        int otp = 100000 + random.nextInt(900000);
		emailService.sendEmail(email,"This is Confidential", "This is  Body of Email\n OTP is "+otp);
		List<RegistrationDetails> saved = service.getRegistrationDetailsByEmail(email);
		for (RegistrationDetails detail : saved) {
			detail.setEmail(email);
			detail.setOtp(otp);
			detail.setPassword("121345");
			service.saveRegistrationDetails(detail);
			return 1;
		}
		RegistrationDetails reg = new RegistrationDetails();
		reg.setEmail(email);
		reg.setOtp(otp);
		reg.setPassword("121345");
		service.saveRegistrationDetails(reg);
		
//		System.out.println("Successful");
		return 0;
	}

	public int resetPassword(String email, int otp,String password, Model model) {
		List<RegistrationDetails> saved = service.getRegistrationDetailsByEmail(email);
		for (RegistrationDetails detail:saved) {
			if(otp == detail.getOtp())
			{
				detail.setPassword(password);
				service.saveRegistrationDetails(detail);
				return 1;
			}
		}
	
		return 0;
	}
	
	public int forgetPasswordSendOtp(String email) {
			int status = 0;
			
			List<RegistrationDetails> saved = service.getRegistrationDetailsByEmail(email);
			for(RegistrationDetails test : saved) {
				if(test.getEmail().equals(email)) {
					return 1; // user email id already exist
				}
				
				//return 0;
			}
			
			Random random = new Random();
			System.out.println("started email");
	        // Generate a random 6-digit number
	        int otp = 100000 + random.nextInt(900000);
			emailService.sendEmail(email,"This is Confidential", "This is  Body of Email\n OTP is "+otp);
			RegistrationDetails reg = new RegistrationDetails();
			reg.setEmail(email);
			reg.setOtp(otp);
			reg.setPassword("121345");
			service.saveRegistrationDetails(reg);
			
//			System.out.println("Successful");
			return 0;
			
		}

	}
