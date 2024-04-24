package com.HotelRoom.models;


import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name="Room")
public class Room {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
    private int roomId;
    private String name;
    private String description;
    private String image;
    private String roomType;
    private float price;
    private boolean is_Booked;


    @ManyToOne
    @JoinColumn(name = "hotelid")
    private Hotel hotel;
    
    
    private long hotelids;
    
    

  



	
	public long getHotelids() {
		return hotelids;
	}

	public void setHotelids(long hotelids) {
		this.hotelids = hotelids;
	}

	public Hotel getHotel() {
		return hotel;
	}

	public void setHotel(Hotel hotel) {
		this.hotel = hotel;
	}



	@Override
	public String toString() {
		return "Room [roomId=" + roomId + ", name=" + name + ", description=" + description + ", image=" + image
				+ ", roomType=" + roomType + ", price=" + price + ", isBooked=" + is_Booked + ", hotel=" + hotel + "]";
	}



	public int getRoomId() {
        return roomId;
    }

    public void setRoomId(int roomId) {
        this.roomId = roomId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getRoomType() {
        return roomType;
    }

    public void setRoomType(String roomType) {
        this.roomType = roomType;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }

	public boolean isIs_Booked() {
		return is_Booked;
	}

	public void setIs_Booked(boolean is_Booked) {
		this.is_Booked = is_Booked;
	}


}

