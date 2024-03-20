package com.HotelRoom.Controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.Banner.Mode;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import com.HotelRoom.Repository.UserRepository;
import com.HotelRoom.models.User;

import jakarta.servlet.http.HttpSession;

@Controller
public class SignupController {
	
	@Autowired
	UserRepository uRepo;
	
	
	@GetMapping("/")
	public String frontpage() {
		return "index.html";
	}
	
	@GetMapping("/registerguide")
	public String registerguide() 
	{
		
		return "signup.html";
	}
	
	@PostMapping("/register")
	public String register(@ModelAttribute User u)
	{
		uRepo.save(u);
		return "login.html";
	}
	
	@GetMapping("/loginguide")
	public String loginguide()
	{
		return"login.html";
	}
	
	@PostMapping("/login")
	public String postlogin(@ModelAttribute User u, Model model, HttpSession session) {
	    if (uRepo.existsByFirstNameAndPassword(u.getFirstName(), u.getPassword())) 
	    {
	    	
	    	session.setAttribute("ActiveUser", u.getFirstName());
	    	session.setMaxInactiveInterval(60);
	        List<User> userList = uRepo.findAll();
	        model.addAttribute("uList", userList); // Adding the 'uList' attribute to the model
	        return "home.html"; // Redirecting to home page or any other appropriate page after successful login
	    }
	    return "login.html"; // Returning to the login page if login is unsuccessful
	}


	
	
	@GetMapping("/edit/{id}")
	public String editForm(@PathVariable int id, Model model ,HttpSession session) 
	{
		if (session.getAttribute("ActiveUser")==null) {
			
			session.setAttribute("error", "Please login first");
			return"login.html";
		}
		User u = uRepo.getById(id);
		model.addAttribute("userObject",u);
		return "editform.html";
		
		
		
	}
	
	@PostMapping("/edit")
	public String edit(@ModelAttribute User u, Model model)
	{
		uRepo.save(u);

		model.addAttribute("uList",uRepo.findAll());
		
		return "home.html";
	}
	
	@GetMapping("/logout")
	public String logout(HttpSession session) 
	{
		session.invalidate();
		return"login.html";
	}
	
	
	@GetMapping("/delete/{id}")
	public String deleteAdata(@PathVariable int id,Model model)
	{
		uRepo.deleteById(id);
		model.addAttribute("uList",uRepo.findAll());
		return "home.html";
	}

}
