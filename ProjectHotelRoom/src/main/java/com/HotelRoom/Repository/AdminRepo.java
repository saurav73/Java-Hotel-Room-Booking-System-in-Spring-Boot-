package com.HotelRoom.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.HotelRoom.models.Admin;
@Repository
public interface AdminRepo extends JpaRepository<Admin, Integer>{

}
