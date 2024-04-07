package com.HotelRoom.Controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import org.springframework.web.bind.annotation.GetMapping;

import com.HotelRoom.Repository.AdminRepo;


@Controller
public class AdminController {

	@Autowired
	AdminRepo adminRepo;




	
}
