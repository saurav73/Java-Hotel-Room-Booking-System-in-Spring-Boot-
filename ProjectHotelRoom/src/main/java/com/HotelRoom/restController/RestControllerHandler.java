package com.HotelRoom.restController;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.HotelRoom.Repository.UserRepository;
import com.HotelRoom.models.User;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.PathVariable;


@RestController
public class RestControllerHandler {
	
	@Autowired
	private UserRepository uRepo;
	
	@GetMapping("/api/getData")
	public List<User> getData()
	{
		
		List<User> uList = uRepo.findAll();
		return uList;
	}
	
	@PostMapping("/api/postData")
	public String postData(@RequestBody User u) 
	{
		uRepo.save(u);
		return "Succesfully saved";
		
	}
	@PutMapping("/api/updateData")
	public String updateData( @RequestBody User u) {
		//TODO: process PUT request
		
		
		uRepo.save(u);
		return "update successfully";
	}
	
	@DeleteMapping("/api/deleteData/{id}")
	public String deleteData(@PathVariable int id)
	{
		
		uRepo.deleteById(id);
		return"Data Deleted successfully";
	}

	
	
}
