/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package dbtools;
//import domain
import domain.Booking;

//import arraylist
import java.util.ArrayList;

//Interface defining CRUD operations for the imported domain.


/**
 *
 * @author iyana
 */
public interface IBookingSvc {
    
    
    // Select by any other parameter (e.g., email or phone number)
    public Booking selectBookingParam(String bookingID);
    
    // Select all
    public ArrayList<Booking> selectAllBooking();
    
    // Update an existing record
    public int updateBooking(Booking obj);
    
    
    // Insert a new using parameters (perhaps for a different method of insertion)
    public int insertBookingParam(Booking obj);
    
    // Delete by their unique identifier
    public int deleteBooking(String bookingID);
}
