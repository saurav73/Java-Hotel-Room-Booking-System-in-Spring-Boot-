package com.HotelRoom.Repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.HotelRoom.models.Room;

public interface RoomRepo  extends JpaRepository<Room, Integer>{

	 

//	List<Room> findByHotelidsAndIs_Booked(long id, boolean b);
//
//	List<Room> findByHotelidsAndIs_Booked(long id, boolean b);

	Optional<Room> findById(int roomId);

	List<Room> findByHotelids(Long id);

	List<Room> findByRoomId(int id);

	


	

}
