package com.HotelRoom.Repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.HotelRoom.models.Hotel;

@Repository
public interface HotelRepo extends JpaRepository<Hotel, Long> {

	boolean existsByEmailAndPassword(String email, String password);
	long countByVerify(boolean verify);
	List<Hotel> getHotelsByCity(String cityName);
	List<Hotel> findByVerifyIsTrue();
	Optional<Hotel> findById(long id);
	 Optional<Hotel> findById(Long id);
	Hotel findByEmail(String hotelEmail);
	Hotel findByEmailAndPassword(String email, String password);
	
}
