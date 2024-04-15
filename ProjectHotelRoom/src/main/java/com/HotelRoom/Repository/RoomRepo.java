package com.HotelRoom.Repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.HotelRoom.models.Room;

public interface RoomRepo  extends JpaRepository<Room, Integer>{

	 List<Room> findByHotelids(int hotelids);

}
