package com.HotelRoom.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.HotelRoom.models.Hotel;

@Repository
public interface HotelRepo extends JpaRepository<Hotel, Integer> {

	boolean existsByEmailAndPassword(String email, String password);
}
