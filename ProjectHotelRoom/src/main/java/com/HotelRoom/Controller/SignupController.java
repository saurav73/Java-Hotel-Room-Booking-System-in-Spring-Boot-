package com.HotelRoom.Controller;

import java.io.File;


import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.sql.SQLException;
import java.util.List;

import java.util.Properties;
import java.util.Random;

import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.HotelRoom.Repository.HotelRepo;
import com.HotelRoom.Repository.UserRepository;
import com.HotelRoom.models.Hotel;
import com.HotelRoom.models.User;

import jakarta.servlet.http.HttpSession;



@Controller
@Service
public class SignupController {
	
	@Autowired
	UserRepository uRepo;
	
	@Autowired
	HotelRepo hRepo;
	
	
	@GetMapping("/")
	public String frontpage() {
		return "index2.html";
	}
	
	@GetMapping("/registerguide")
	public String registerguide() 
	{
		
		return "signup.html";
	}
	
	@PostMapping("/register")
	public String register(@ModelAttribute User u )
	{
		uRepo.save(u);
		return "index2.html";
	}
	
	
	@GetMapping("/loginguide")
	public String loginguide()
	{
		return"login.html";
	}
	
	@PostMapping("/login")
	public String postlogin(@ModelAttribute User u,
			@ModelAttribute Hotel hotel, Model model, HttpSession session) {
	     
	   
	    	if (u.getUsername().equals("Hoteladmin") && u.getPassword().equals("admin")) {
	            // Admin login
	            session.setAttribute("ActiveUser", u.getUsername());
	            long verifiedCount = hRepo.countByVerify(true);
	            long unverifiedCount = hRepo.countByVerify(false);
	            model.addAttribute("verifiedCount", verifiedCount);
	            model.addAttribute("unverifiedCount", unverifiedCount);
	            session.setMaxInactiveInterval(600);
	            return "admin-dashboard"; // Redirecting to the admin dashboard
	        } else if (uRepo.existsByUsernameAndPassword(u.getUsername(), u.getPassword())){
	            // Normal user login
	            session.setAttribute("ActiveUser", u.getUsername());
	            session.setMaxInactiveInterval(60);
	            List<User> userList = uRepo.findAll();
	            long verifiedCount = hRepo.countByVerify(true);
	            model.addAttribute("verifiedCount", verifiedCount);
	            List<Hotel> verifiedHotels = hRepo.findByVerifyIsTrue();
	            model.addAttribute("hotels", verifiedHotels);
	            model.addAttribute("uList", userList); // Adding the 'uList' attribute to the model
	            return "hotel-list.html"; // Redirecting to home page or any other appropriate page after successful login
	        }
	    
	    
	    else {
	    	
	    model.addAttribute("error", "Please enter the valid crediantial");
	    	return "index2.html"; // Returning to the login page if login is unsuccessful
	    }
	    
	}
	
	@PostMapping("/hotellogin")
	public String posthotellogin(
	        @ModelAttribute Hotel hotel, Model model, HttpSession session) throws NoSuchAlgorithmException {
	    Hotel foundHotel = hRepo.findByEmailAndPassword(hotel.getEmail(), hashPassword(hotel.getPassword()));
	    if (foundHotel != null) {
	        // Set the ActiveUser session attribute to the id of the found hotel
	        session.setAttribute("ActiveUser", foundHotel.getId());
	        return "user-dashboard";
	    } else {
	        model.addAttribute("error", "Please enter valid credentials");
	        return "index2.html";
	    }
	}

	
	
	

	
	
	
    
 // Method to hash the password using MD5
    private String hashPassword(String password) throws NoSuchAlgorithmException {
        MessageDigest md = MessageDigest.getInstance("MD5");
        md.update(password.getBytes());
        byte[] digest = md.digest();
        StringBuilder sb = new StringBuilder();
        for (byte b : digest) {
            sb.append(String.format("%02x", b & 0xff));
        }
        return sb.toString();
    }

	
	//Image uploading in file system
	
//	@PostMapping("/upload")
//	public String addandupload(@ModelAttribute User usr, @RequestParam MultipartFile image )
//	{
//		User user = new User();
//		user.setBackImage(image.getOriginalFilename());
//		user.setUsername(usr.getUsername());
//		user.setPassword(usr.getPassword());
//		user.setEmail(usr.getEmail());
//		User u= uRepo.save(user);
//		
//		if (u!=null) {
//			
//			try {
//				
//			File saveFile =	new ClassPathResource("static/pictures").getFile();
//			Path path=Paths.get(saveFile.getAbsolutePath()+File.separator+image.getOriginalFilename());
//			
//			Files.copy(image.getInputStream(), path,StandardCopyOption.REPLACE_EXISTING);
//			System.out.println(path);
//			} catch (Exception e) {
//				// TODO: handle exception
//				e.printStackTrace();
//			}
//		}
//		
//		return "index2.html";
//	}
	
	
	@GetMapping("/recover")
	public String recover()
	{
		return"recover";
	}
	
	
//	@PostMapping("/login")
//	@ResponseBody // Ensure the response is sent as JSON
//	public Map<String, Object> postlogin(@ModelAttribute User u, HttpSession session) {
//	    Map<String, Object> response = new HashMap<String, Object>();
//	    if (uRepo.existsByUsernameAndPassword(u.getUsername(), u.getPassword())) {
//	        session.setAttribute("ActiveUser", u.getUsername());
//	        session.setMaxInactiveInterval(60);
//	        response.put("success", true);
//	        return response;
//	    } else {
//	        response.put("success", false);
//	        response.put("error", "Please enter the valid credentials");
//	        return response;
//	    }
//	}
	
	@GetMapping("/dashboard")
	public String getdashboard() {
		return "hotel-search-result.html";
	}

	
	@GetMapping("/user-board")
	public String userGuide()
	{
		return "user-dashboard-booking";
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
	
	@GetMapping("/adminUserlist")
	public String adminUserlist(Model model)
	{
		List<User> userList = uRepo.findAll();
	    model.addAttribute("uList", userList);
		return "index";
	}
	
	
	 @PostMapping("/forgot")
	    public String forgot(@ModelAttribute User u, HttpSession session1) throws SQLException {
	        String email = u.getEmail();
	        int otpvalue = 0;

	        session1.setAttribute(email, "email");

	        if (email != null && uRepo.existsByEmail(email)==true) {
			    // sending otp
			    Random rand = new Random();
			    otpvalue = rand.nextInt(999999); // Generate a 6-digit OTP

			    String to = email; // Change accordingly
			    // Get the session object
			    Properties props = new Properties();
			    props.put("mail.smtp.host", "smtp.gmail.com");
			    props.put("mail.smtp.socketFactory.port", "465");
			    props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
			    props.put("mail.smtp.auth", "true");
			    props.put("mail.smtp.port", "465");
			    Session session = Session.getDefaultInstance(props, new Authenticator() {
			        protected PasswordAuthentication getPasswordAuthentication() {
			            return new PasswordAuthentication("rentmatic18@gmail.com", "ipotqmktdoltoxtw"); // Put your
			                                                                                            // email
			            // id and
			            // password here
			        }
			    });

			    // compose message
			    try {
			        MimeMessage message = new MimeMessage(session);
			        message.setFrom(new InternetAddress("rentmatic18@gmail.com")); // Change accordingly
			        message.addRecipient(Message.RecipientType.TO, new InternetAddress(to));
			        message.setSubject("OTP for Password Reset");
			        message.setText("Your OTP for password reset is: " + otpvalue);
			        // send message
			        Transport.send(message);
			        System.out.println("Message sent successfully");
			    } catch (MessagingException e) {
			        throw new RuntimeException(e);
			    }
			    session1.setAttribute("message1", "OTP is sent to your email id");
			    session1.setAttribute("otp", otpvalue);
			    session1.setAttribute("email", email);
			    return "otp.jsp";
			} else {
			    session1.setAttribute("errorMessage1", "Email address not found!");
			    return "otppage";
			}
	        
	       
	    }

	 
	 @GetMapping("adminAddhotel")
     public String adminAddhotel()
     {
     	return "add-hotel";
     }
}
