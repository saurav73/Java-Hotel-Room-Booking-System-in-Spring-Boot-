package com.HotelRoom.models;

import java.util.List;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;



@Entity
@Table(name="Admin")
public class Admin {
	
		 @Id
		    @GeneratedValue(strategy = GenerationType.IDENTITY)
		    private int adminId;
		    
		    private String firstName;
		    private String lastName;
		    private String password;
		    private String phoneNumber;
		    private String email;
		    
			public int getAdminId() {
				return adminId;
			}

			public void setAdminId(int adminId) {
				this.adminId = adminId;
			}

			public String getFirstName() {
				return firstName;
			}

			public void setFirstName(String firstName) {
				this.firstName = firstName;
			}

			public String getLastName() {
				return lastName;
			}

			public void setLastName(String lastName) {
				this.lastName = lastName;
			}

			public String getPassword() {
				return password;
			}

			public void setPassword(String password) {
				this.password = password;
			}

			public String getPhoneNumber() {
				return phoneNumber;
			}

			public void setPhoneNumber(String phoneNumber) {
				this.phoneNumber = phoneNumber;
			}

			public String getEmail() {
				return email;
			}

			public void setEmail(String email) {
				this.email = email;
			}

			
		    
		    
		    
		    

}
