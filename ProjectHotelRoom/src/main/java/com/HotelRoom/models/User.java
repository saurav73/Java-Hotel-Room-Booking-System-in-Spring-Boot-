package com.HotelRoom.models;



import jakarta.persistence.*;

import lombok.AllArgsConstructor;
import lombok.*;
;



@Entity
@Table(name="User")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class User {
	
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int userId;
	private String username;
	private String email;
    private String password;
    private String phoneNumber;

    

	
	
	
	
	



	public int getUserId() {
		return userId;
	}




	public String getUsername() {
		return username;
	}




	public void setUsername(String username) {
		this.username = username;
	}




	public void setUserId(int userId) {
		this.userId = userId;
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
