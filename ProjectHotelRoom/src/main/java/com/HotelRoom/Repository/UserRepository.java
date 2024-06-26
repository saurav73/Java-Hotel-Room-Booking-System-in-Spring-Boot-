package com.HotelRoom.Repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


import com.HotelRoom.models.User;



@Repository
public interface UserRepository  extends JpaRepository<User, Integer>{
	
	boolean existsByUsernameAndPassword(String Username, String password);

    boolean  findByEmail(String email);
    
    boolean existsByEmail(String email);

	Optional<User> findById(Integer userId);

	User findByUsername(String string);

	

	

}
