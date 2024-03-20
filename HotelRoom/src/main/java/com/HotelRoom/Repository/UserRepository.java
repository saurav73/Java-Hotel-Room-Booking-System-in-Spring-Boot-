package com.HotelRoom.Repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.HotelRoom.models.User;

public interface UserRepository  extends JpaRepository<User, Integer>{
	
	boolean existsByFirstNameAndPassword(String FirstName, String password);

}
