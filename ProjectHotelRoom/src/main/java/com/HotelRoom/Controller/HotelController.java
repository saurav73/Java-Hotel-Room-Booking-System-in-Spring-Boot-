package com.HotelRoom.Controller;


import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.servlet.http.HttpSession;

import org.springframework.core.io.ClassPathResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.HotelRoom.Repository.HotelRepo;
import com.HotelRoom.Repository.RoomRepo;
import com.HotelRoom.models.Hotel;
import com.HotelRoom.models.Room;




@Controller
public class HotelController {
	
	@Autowired
	HotelRepo hotelRepo;
	
	@Autowired
	RoomRepo roomRepo;
	
	
	
	
	
	 private static final int MAX_FILE_NAME_LENGTH = 255;
	
	@GetMapping("/addhotelhelper")	
	public String addhotelhelper() {
		return "add-hotel.html";
	}
	
    @PostMapping("/addhotel")
    public String addHotel(@ModelAttribute Hotel hotel,
                           @RequestParam("photoUrls") MultipartFile photoUrls,
                           @RequestParam("hotelpic") MultipartFile hotelpic) {
        try {
            if (photoUrls.isEmpty() || hotelpic.isEmpty()) {
                return "error"; // Both photoUrls and hotelpic are required, handle this accordingly
            }

            Hotel newHotel = new Hotel();
            newHotel.setName(hotel.getName());
            newHotel.setEmail(hotel.getEmail());
            newHotel.setRole(hotel.getRole());
            newHotel.setBusinessname(hotel.getBusinessname());
            newHotel.setDescription(hotel.getDescription());
            newHotel.setCity(hotel.getCity());
            newHotel.setLongitude(hotel.getLongitude());
            newHotel.setLatitude(hotel.getLatitude());
            newHotel.setPhonenumber(hotel.getPhonenumber());
         // Hash the password
            String hashedPassword = hashPassword(hotel.getPassword());
            newHotel.setPassword(hashedPassword);

            // Save photoUrls file
            String photo_url = StringUtils.cleanPath(photoUrls.getOriginalFilename());
            saveFile(photoUrls, photo_url);
            newHotel.setPhoto_url("/pictures/" + photo_url);

            // Save hotelpic file
            String hotel_pic = StringUtils.cleanPath(hotelpic.getOriginalFilename());
            saveFile(hotelpic, hotel_pic);
            newHotel.setHotel_pic("/pictures/" + hotel_pic);

            // Save the hotel to the database
            hotelRepo.save(newHotel);

            return "index2"; // Return success view
        } catch (Exception e) {
            e.printStackTrace();
            // Handle exception properly (e.g., return an error page)
            return "error"; // Return error view
        }
    }
    
    @PostMapping("/addRoom")
    public String addRoom(@RequestParam("hotelids") int hotelids,
                          @RequestParam("name") String name,
                          @RequestParam("roomType") String roomType,
                          @RequestParam("description") String description,
                          @RequestParam("price") float price,
                          @RequestParam("photoUrls") MultipartFile[] photoUrls,
                          Model model) {
        try {
            
            
            
            // Create a new room object
            Room newRoom = new Room();
            newRoom.setName(name);
            newRoom.setRoomType(roomType);
            newRoom.setDescription(description);
            newRoom.setPrice(price);
            newRoom.setHotelids(hotelids); // Set the hotel object
            
            // Save photoUrls files
            StringBuilder photoUrlsString = new StringBuilder();
            for (MultipartFile photoUrl : photoUrls) {
                String filename = StringUtils.cleanPath(photoUrl.getOriginalFilename());
                saveFile(photoUrl, filename);
                photoUrlsString.append("/pictures/").append(filename);
            }
            newRoom.setImage(photoUrlsString.toString());
            
            // Save the room to the database
            roomRepo.save(newRoom);
            
            // Add success message to the model
            model.addAttribute("success", "Successfully added");
            
            return "redirect:/userroomguide"; // Redirect to success view
        } catch (Exception e) {
            e.printStackTrace();
            // Handle exception properly (e.g., return an error page)
            return "error"; // Return error view
        }
    }

    
 
//    @GetMapping("/roomDetails")
//    public String showRoomDetails(@RequestParam("email") String hotelEmail, Model model) {
//        // Retrieve the hotel based on the provided email
//        Hotel hotel = hotelRepo.findByEmail(hotelEmail);
//        
//        if (hotel != null) {
//            // Get the list of rooms associated with the hotel
//            List<Room> rooms = hotel.getRoomList();
//            
//            // Add the list of rooms to the model
//            model.addAttribute("rooms", rooms);
//        } else {
//            // Handle case where hotel with the provided email is not found
//            // You can add appropriate error handling here
//            // For example, display an error message to the user
//            model.addAttribute("error", "Hotel not found");
//        }
//        
//        // Return the Thymeleaf template to display the room details
//        return "room-details";
//    }


    
    
    
    //file

    private void saveFile(MultipartFile file, String fileName) throws IOException {
        Path uploadDir = Paths.get("src/main/resources/static/pictures/");

        if (!Files.exists(uploadDir)) {
            Files.createDirectories(uploadDir);
        }

        try (InputStream inputStream = file.getInputStream()) {
            Path filePath = uploadDir.resolve(fileName);
            Files.copy(inputStream, filePath, StandardCopyOption.REPLACE_EXISTING);
            System.out.println(filePath);
        } catch (IOException e) {
            throw new IOException("Could not save uploaded file: " + fileName, e);
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


    @GetMapping("/userdashboard")
    public String showHotels(Model model) {
        List<Hotel> hotels = hotelRepo.findAll();
        model.addAttribute("hotels", hotels);
        return "userdashboard";
    }
    
    
 // Handle search request
    @PostMapping("/search")
    public String searchHotelsByCity(@RequestParam("city") String cityName, Model model) {
        List<Hotel> hotels = hotelRepo.getHotelsByCity(cityName);
        
        // Filter hotels where verify is true
        List<Hotel> verifiedHotels = hotels.stream()
                                          .filter(Hotel::isVerify)
                                          .collect(Collectors.toList());
        
        
        model.addAttribute("hotels", verifiedHotels);
        model.addAttribute("cityName", cityName); 
        long numberOfHotels = verifiedHotels.size(); 
// Count the number of verified hotels for the city
        model.addAttribute("numberOfHotels", numberOfHotels); // Add the count to the model
        
        return "hotel-list";
    }

	
    
    @GetMapping("/hotel-details/{id}")
    public String getHotelDetails(@PathVariable("id") int id, Model model) {
        Hotel hotel = hotelRepo.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid hotel id: " + id));
        List<Room> rooms = roomRepo.findByHotelids(id);
        model.addAttribute("rooms",rooms);
        model.addAttribute("hotel", hotel);
        model.addAttribute("latitude", hotel.getLatitude());
        model.addAttribute("longitude", hotel.getLongitude());
        return "HotelDetail";
        // Thymeleaf template name
    }
    
    
//    @GetMapping("/hotel-details/{id}")
//    public ResponseEntity<Map<String, Object>> getHotelDetails(@PathVariable("id") int id) {
//        Map<String, Object> response = new HashMap();
//        try {
//            Hotel hotel = hotelRepo.findById(id)
//                    .orElseThrow(() -> new IllegalArgumentException("Invalid hotel id: " + id));
//            List<Room> rooms = roomRepo.findByHotelids(id);
//            response.put("hotel", hotel);
//            response.put("rooms", rooms);
//            return ResponseEntity.ok(response);
//        } catch (IllegalArgumentException e) {
//            response.put("error", e.getMessage());
//            return ResponseEntity.badRequest().body(response);
//        } catch (Exception e) {
//            response.put("error", "Internal server error");
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
//        }
//    }
    
    
    @GetMapping("/userbookingguide")
    public String userBookingguide() 
    {
    	return"user-dashboard-booking";
    }
    
    @GetMapping("/userdashboardguide")
    public String userdashboardguide()
    {
    	return"user-dashboard";
    }
    
    
    @GetMapping("/userroomguide")
    public String userroomguide()
    {
    	return "user-dashboard-room";
    }
    
    @GetMapping("/userhotelinfo")
    public String userhotelinfo()
    {
    	return"hotel-info";
    }
  
    
	
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

}
