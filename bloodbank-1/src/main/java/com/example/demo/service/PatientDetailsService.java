package com.example.demo.service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.entity.DonorDetails;
import com.example.demo.entity.Inventory;
import com.example.demo.entity.PatientDetails;
import com.example.demo.repository.PatientDetailsRepository;

import jakarta.transaction.Transactional;

@Service
public class PatientDetailsService {
	
	@Autowired
	private PatientDetailsRepository repo;
	
	public PatientDetails savePatientDetails(PatientDetails details) {
		return repo.save(details);
	}
	
	public List<PatientDetails> getPatientDetails() {
		return repo.findAll();
	}
	
	public Optional<PatientDetails> getPatientDetailsByUser(int id) {
		return repo.findById(id);
	}

//	public Optional<PatientDetails> getPatientDetailsByEmail(String email) {
//		// TODO Auto-generated method stub
//		
//		return repo.findByEmail(email);
//	}
//	

//	------------------------------------------------
	
	public List<PatientDetails> getPatientsDetails() {
		return repo.findAll();
	}
	
	public Optional<PatientDetails> getPatientsDetailsByUser(int id) {
		return repo.findById(id);
	}

	public List<PatientDetails> getPatientsDetailsByEmail(String email) {
		// TODO Auto-generated method stub
		
		return repo.findByEmail(email);
	}
	
	
	
}
