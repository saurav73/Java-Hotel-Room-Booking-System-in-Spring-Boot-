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
import java.util.List;
import java.util.Optional;

import org.springframework.core.io.ClassPathResource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.HotelRoom.Repository.HotelRepo;
import com.HotelRoom.models.Hotel;




@Controller
public class HotelController {
	
	@Autowired
	HotelRepo hotelRepo;
	
	
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
