package com.example.demo.entity;

import java.util.Date;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class PatientDetails {
	

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE)
	private long id;
	private String email;
	private String firstname;
	private String lastname;
	private String gender;
	private Date dateOfBirth;		// date of birth
	
	//private String bloodType;
	private String bloodGroup;
	private String city;
	//private String Address;
	private Date dateOfBlood; //date of blood request
	private boolean status;
	public Date getDateOfBlood() {
		return dateOfBlood;
	}
	public void setDateOfBlood(Date dateOfBlood) {
		this.dateOfBlood = dateOfBlood;
	}
	public boolean isStatus() {
		return status;
	}
	public void setStatus(boolean status) {
		this.status = status;
	}
	
	
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getFirstname() {
		return firstname;
	}
	public void setFirstname(String firstname) {
		this.firstname = firstname;
	}
	public String getLastname() {
		return lastname;
	}
	public void setLastname(String lastname) {
		this.lastname = lastname;
	}
	public String getGender() {
		return gender;
	}
	public void setGender(String gender) {
		this.gender = gender;
		
	}
	
	public Date getDateOfBirth() {
		return dateOfBirth;
	}
	public void setDateOfBirth(Date dateOfBirth) {
		this.dateOfBirth = dateOfBirth;
	}
	
//	public String getBloodType() {
//		return bloodType;
//	}
//	public void setBloodType(String bloodType) {
//		this.bloodType = bloodType;
//	}
	public String getBloodGroup() {
		return bloodGroup;
	}
	public void setBloodGroup(String bloodGroup) {
		this.bloodGroup = bloodGroup;
	}
	public String getCity() {
		return city;
	}
	public void setCity(String city) {
		this.city = city;
	}
//	public String getAddress() {
//		return Address;
//	}
//	public void setAddress(String address) {
//		Address = address;
//	}
	

}
