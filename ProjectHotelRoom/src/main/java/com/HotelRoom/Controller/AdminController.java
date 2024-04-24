package com.HotelRoom.Controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.HotelRoom.Repository.AdminRepo;
import com.HotelRoom.Repository.HotelRepo;
import com.HotelRoom.Repository.UserRepository;
import com.HotelRoom.models.Hotel;
import com.HotelRoom.models.User;

import jakarta.servlet.http.HttpSession;


@Controller
public class AdminController {

	@Autowired
	AdminRepo adminRepo;


	@Autowired
	HotelRepo hotelRepo;
	
	@Autowired
	UserRepository userRepo;

@GetMapping("/adminguidedash")
public String adminguidedash()
{
	return "admin-dashboard-booking";
}

@GetMapping("/adminshowhotels")
public String showHotels(Model model) {
    List<Hotel> hotels = hotelRepo.findAll();
    model.addAttribute("hotels", hotels);
    return "admin-dashboard-booking";
}


@PostMapping("/verifyHotel")
public String verifyHotel(@RequestParam int id, RedirectAttributes redirectAttributes) {
    Optional<Hotel> optionalHotel = hotelRepo.findById(id);
    if (optionalHotel.isPresent()) {
        Hotel hotel = optionalHotel.get();
        // Set hotel verification status to true
        hotel.setVerify(true);
        hotelRepo.save(hotel);
        redirectAttributes.addFlashAttribute("successMessage", "Hotel verified successfully.");
    } else {
        redirectAttributes.addFlashAttribute("errorMessage", "Hotel verification failed. Hotel not found.");
    }
    // Redirect back to the hotel list page
    return "redirect:/adminshowhotels";
}


//@PostMapping("/cancelHotel")
//public String cancelHotel(@RequestParam int id, RedirectAttributes redirectAttributes) {
//    Optional<Hotel> optionalHotel = hotelRepo.findById(id);
//    if (optionalHotel.isPresent()) {
//        Hotel hotel = optionalHotel.get();
//        // Set hotel verification status to false (assuming 'verify' is a boolean field)
//        hotel.setVerify(false);
//        hotelRepo.save(hotel);
//        redirectAttributes.addFlashAttribute("successMessage", "Hotel verification canceled successfully.");
//    } else {
//        redirectAttributes.addFlashAttribute("errorMessage", "Hotel cancellation failed. Hotel not found.");
//    }
//    // Redirect back to the hotel list page
//    return "redirect:/adminshowhotels";
//}



@PostMapping("/cancelHotel")
public String cancelHotel(@RequestParam int id, HttpSession session, RedirectAttributes redirectAttributes) {
    // Check if user is logged in and is an admin
    String activeUser = (String) session.getAttribute("Activeadmin");
    if (activeUser == null || !activeUser.equals("Hoteladmin")) {
        // Redirect to login page or handle unauthorized access
        return "index2";
    }

    Optional<Hotel> optionalHotel = hotelRepo.findById(id);
    if (optionalHotel.isPresent()) {
        Hotel hotel = optionalHotel.get();
        // Set hotel verification status to false (assuming 'verify' is a boolean field)
        hotel.setVerify(false);
        hotelRepo.save(hotel);
        redirectAttributes.addFlashAttribute("successMessage", "Hotel verification canceled successfully.");
    } else {
        redirectAttributes.addFlashAttribute("errorMessage", "Hotel cancellation failed. Hotel not found.");
    }
    // Redirect back to the hotel list page
    return "redirect:/adminshowhotels";
}

	
@GetMapping("/userCounts")
public String getUserCounts(Model model ,HttpSession session) {
	   // Check if user is logged in and is an admin
    String activeUser = (String) session.getAttribute("Activeadmin");
    if (activeUser == null || !activeUser.equals("Hoteladmin")) {
        // Redirect to login page or handle unauthorized access
        return "index2";
    }
    
    long verifiedCount = hotelRepo.countByVerify(true);
    long unverifiedCount = hotelRepo.countByVerify(false);
    model.addAttribute("verifiedCount", verifiedCount);
    model.addAttribute("unverifiedCount", unverifiedCount);
    return "admin-dashboard";
}



@GetMapping("/Userlist")
public String userlist( Model model, HttpSession session)
{
	   // Check if user is logged in and is an admin
    String activeUser = (String) session.getAttribute("Activeadmin");
    if (activeUser == null || !activeUser.equals("Hoteladmin")) {
        // Redirect to login page or handle unauthorized access
        return "index2";
    }
	 List<User> users = userRepo.findAll();
	 
     model.addAttribute("users", users);
	return "admin-dashboard-travellers";
}

@GetMapping("/deleteuser/{id}")
public String deleteAdata(@PathVariable int id,Model model,HttpSession session)
{
	   // Check if user is logged in and is an admin
    String activeUser = (String) session.getAttribute("Activeadmin");
    if (activeUser == null || !activeUser.equals("Hoteladmin")) {
        // Redirect to login page or handle unauthorized access
        return "index2";
    }
	userRepo.deleteById(id);
	model.addAttribute("users",userRepo.findAll());
	return "redirect:/Userlist";
}


@GetMapping("/edit-user/{userId}")
public String showEditForm(@PathVariable int userId, Model model,HttpSession session) {
	   // Check if user is logged in and is an admin
    String activeUser = (String) session.getAttribute("Activeadmin");
    if (activeUser == null || !activeUser.equals("Hoteladmin")) {
        // Redirect to login page or handle unauthorized access
        return "index2";
    }
    // Retrieve the user by ID from the repository
    Optional<User> user = userRepo.findById(userId);

model.addAttribute("updated","Data Updated succesfully");       
model.addAttribute("user", user);
        
        
        // Return the name of the Thymeleaf template for the edit form
        return "admin-dashboard-traveler-detail";

}





// This method handles the form submission to save updated user details
@PostMapping("/admin-save-updated-user")
public String saveUpdatedUser(User user,Model model) {
    // Here, you would save the updated user details to the database
    // Replace the following line with your logic to save the updated user

    userRepo.save(user);
	model.addAttribute("updated","Data Updated succesfully");
	   // Debug print statement
    System.out.println("Updated message: Data Updated succesfully");

    // Redirect to a success page or the user list page
    return "redirect:/usersuccess";
}

@GetMapping("/usersuccess")
public String usersuccess( Model model)
{
	 List<User> users = userRepo.findAll();
	 model.addAttribute("updated","Data Updated succesfully");
     model.addAttribute("users", users);
	return "admin-dashboard-travellers";
}

@GetMapping("/Adminlogout")
public String logout(HttpSession session,Model model) 
{
	session.invalidate();
	model.addAttribute("success","Logout Succesfully");
	return"index2.html";
}

}
