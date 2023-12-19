package com.example.demo.service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.entity.DonorDetails;
import com.example.demo.repository.DonorDetailsRepository;

import jakarta.transaction.Transactional;

@Service
public class DonorDetailsService {
	
	@Autowired
	private DonorDetailsRepository repo;
	
	public List<DonorDetails> getDonorDetails(){
		return repo.findAll();
	
	}
	
	public List<DonorDetails> findByemail(String email) {
        return repo.findByEmail(email);
	}
	
	
	
    @Transactional
    public List<DonorDetails> checkEligibility(List<DonorDetails> donors) {
        List<DonorDetails> eligible = new ArrayList<>();
        
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");  
        Date date = new Date();  
        String todayDate = formatter.format(date);

        for (DonorDetails donorlist : donors) {
            String donationDate = donorlist.getDateOfDonation();
            long days = findDifference(donationDate, todayDate);

            if (days > 90) {
                String id = donorlist.getEmail();
                System.out.println(id);
                //repo.deleteByBloodId(id);
               // deletedItems.add(donorlist);
                eligible.add(donorlist);
            }
        }

        return eligible;
    }

    static long findDifference(String donationDate, String todayDate) {
        long daysDifference = 0;
        
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            Date date1 = sdf.parse(donationDate);
            Date date2 = sdf.parse(todayDate);
            long timeDifference = date2.getTime() - date1.getTime();
            daysDifference = (timeDifference / (1000 * 60 * 60 * 24)) % 365;
            System.out.println("The Blood sample is " + daysDifference + " days older.");
        } catch (Exception e) {
            System.out.print(e);
        }
        
        return daysDifference;
    }

	public DonorDetails saveDonorDetails(DonorDetails detail) {
		return repo.save(detail);
	}
	
	// for the purpose of Admin
	public List<DonorDetails> getDonorsDetails() {
		return repo.findAll();
	}
	
	// for the purpose of User
	public List<DonorDetails> getDonorsDetailsByEmail(String email) {
		return repo.findByEmail(email);
	}
    
    

}
