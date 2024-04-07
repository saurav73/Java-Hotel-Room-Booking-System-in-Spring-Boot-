package com.HotelRoom.models;





import jakarta.persistence.Entity;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;


@Entity
@Table(name="Booking")
public class Booking {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
    private int bookingId;
	
//	@ManyToMany
//    private int userId;
//	
//	@ManyToMany
//    private int roomId;
    private String startDate;
    private String endDate;
    private int numberONights;
    private float price;

  

    public int getBookingId() {
        return bookingId;
    }

    public void setBookingId(int bookingId) {
        this.bookingId = bookingId;
    }

   

//    public int getUserId() {
//		return userId;
//	}
//
//	public void setUserId(int userId) {
//		this.userId = userId;
//	}
//
//	public int getRoomId() {
//        return roomId;
//    }
//
//    public void setRoomId(int roomId) {
//        this.roomId = roomId;
//    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public int getNumberONights() {
        return numberONights;
    }

    public void setNumberONights(int numberONights) {
        this.numberONights = numberONights;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }
}

