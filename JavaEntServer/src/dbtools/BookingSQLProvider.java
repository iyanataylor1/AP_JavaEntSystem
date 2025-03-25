/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dbtools;

//import domain
import domain.Booking;

import java.time.LocalDate;

//import sql exception
import java.sql.SQLException;

import java.sql.PreparedStatement;

//import arraylist
import java.util.ArrayList;
import java.util.List;

//import swing for joptionpane
import javax.swing.JOptionPane;

//import for logging
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


/**
 *
 * @author iyana
 */
public class BookingSQLProvider extends SQLProvider implements IBookingSvc{
    //logger
    private static Logger logger = LogManager.getLogger(BookingSQLProvider.class);
    
    
            
    //select param
    public Booking selectBookingParam(String bookingID){
        Booking booking = null;
        
        try{
            logger.warn("Attempting to retrieve data from database, errors may occur");
            String query = "SELECT * FROM Booking WHERE bookingID = ?";          //specify query to pull all records
            prepStat = con.prepareStatement(query);                     //create SQL statement
            prepStat.setString(1, bookingID);
            result = prepStat.executeQuery();                           //execute the above query
            logger.info("Record Successfully Retrieved");
            
            while(result.next()) {
                booking = new Booking();         
                booking.setBookingID(result.getString(1));
                booking.setEventID(result.getString(2));
                booking.setEquipmentID(result.getString(3));
                booking.setDeliveryDate(result.getDate(4).toLocalDate());  // Convert Date to LocalDate
                booking.setReturnDate(result.getDate(5).toLocalDate());    // Convert Date to LocalDate
            }
        }
        catch(SQLException ex){
            logger.error(ex.getMessage());
            JOptionPane.showMessageDialog(null, "Could not retrieve record(s)", "SQL Exception", JOptionPane.ERROR_MESSAGE);
        
        }
        return booking;
    }
    
    //select all
    public ArrayList<Booking> selectAllBooking(){
        ArrayList<Booking> list = new ArrayList<Booking>();
        
        try{
            logger.warn("Attempting to retrieve data from database, errors may occur");
            stat = con.createStatement(); 
            String query = "SELECT * FROM Booking";
            logger.info(query);
            result = stat.executeQuery(query);
            logger.info("Record Successfully Retrieved");
            
            while(result.next()){
                Booking booking = new Booking();         
                booking.setBookingID(result.getString(1));
                booking.setEventID(result.getString(2));
                booking.setEquipmentID(result.getString(3));
                booking.setDeliveryDate(result.getDate(4).toLocalDate());  // Convert Date to LocalDate
                booking.setReturnDate(result.getDate(5).toLocalDate());    // Convert Date to LocalDate
                list.add(booking);
            }
        }
        catch(SQLException ex){
            logger.error(ex.getMessage());
            JOptionPane.showMessageDialog(null, "Could not retrieve record(s)", "SQL Exception", JOptionPane.ERROR_MESSAGE);
        }
        return list;
    }
    
    //update
    public int updateBooking(Booking obj){
        int n;
        
        try{
            logger.warn("Attempting to update data in database, errors may occur");
            stat = con.createStatement();
            String query =
                    "UPDATE Booking "
                    + "SET eventID = '" + obj.getEventID() + "', "
                    + "equipmentID = '" + obj.getEquipmentID() + "', "
                    + "deliveryDate = '" + obj.getDeliveryDate() + "', "
                    + "returnDate = '" + obj.getReturnDate() + "' "
                    + "WHERE bookingID = '" + obj.getBookingID() + "' ";
            
            logger.info(query);
            n = stat.executeUpdate(query);
            logger.info("Record Successfully Updated");
        }
        catch(SQLException ex){
            logger.error(ex.getMessage());
            JOptionPane.showMessageDialog(null, "Could not update record(s)", "SQL Exception", JOptionPane.ERROR_MESSAGE);	
            n = 0;
        }
        return n;
    }
    
    //Ensure no double bookings (already added a DB constraint, this is for user experience)
    public boolean isEquipmentAvailable(String equipmentID, LocalDate deliveryDate, LocalDate returnDate) {
        String query = "SELECT 1 FROM Booking WHERE equipmentID = ? " +
                       "AND NOT (returnDate <= ? OR deliveryDate >= ?) LIMIT 1";

        try (PreparedStatement prepStat = con.prepareStatement(query)) {  // Use a local variable here
            prepStat.setString(1, equipmentID);
            prepStat.setDate(2, java.sql.Date.valueOf(deliveryDate));  // Convert LocalDate to SQL Date
            prepStat.setDate(3, java.sql.Date.valueOf(returnDate));

            result = prepStat.executeQuery();
            return !result.next(); // True if no conflicts found
        } catch (SQLException ex) {
            logger.error("Error checking availability: " + ex.getMessage());
        }
        return false;
    }


 
    //insert param
    public int insertBookingParam(Booking obj){
        int n;
        
        try{
            logger.warn("Attempting to insert data to database, errors may occur");
            
            // Check if equipment is available before inserting
                if (!isEquipmentAvailable(obj.getEquipmentID(), obj.getDeliveryDate(), obj.getReturnDate())) {
                logger.warn("Booking conflict detected! Equipment is already booked for these dates.");
                JOptionPane.showMessageDialog(null, "Equipment is already booked for these dates!", 
                                              "Booking Conflict", JOptionPane.WARNING_MESSAGE);
                return 0; // Reject the insert
            }
            
            String query = "INSERT INTO Booking (bookingID, eventID, equipmentID, deliveryDate, returnDate) VALUES (?, ?, ?, ?, ?)";
            prepStat = con.prepareStatement(query);
            prepStat.setString(1, obj.getBookingID());
            prepStat.setString(2, obj.getEventID());
            prepStat.setString(3, obj.getEquipmentID());
            prepStat.setDate(4, java.sql.Date.valueOf(obj.getDeliveryDate()));
            prepStat.setDate(5, java.sql.Date.valueOf(obj.getReturnDate()));
            
            n = prepStat.executeUpdate();
            logger.info("Record Successfully Inserted");
        }
        catch(SQLException ex){
            n = 0;
            logger.error(ex.getMessage());
            JOptionPane.showMessageDialog(null, "Could not insert record(s)", "SQL Exception", JOptionPane.ERROR_MESSAGE);
        }
        
        return n;
    }
    
    //delete
    public int deleteBooking(String bookingID){
        int n;
        
        try{
            logger.warn("Attempting to delete data from database, errors may occur");
            stat = con.createStatement();
            String query = 
                    "DELETE FROM Booking "
                    + "WHERE bookingID = '" + bookingID + "' ";
            
            n = stat.executeUpdate(query);	   
            logger.info("Record Successfully Deleted");
        }
        catch(SQLException ex){
            n = 0;
            logger.error(ex.getMessage());
            JOptionPane.showMessageDialog(null, "Could not delete record(s)", "SQL Exception", JOptionPane.ERROR_MESSAGE);
        }
        return n;
    }
    
    //select all bookings with a particular event ID -- needed for invoice generation
    public List<Booking> getBookingsByEventID(String eventID) {
        List<Booking> bookings = new ArrayList<>();
        String query = "SELECT * FROM Booking WHERE eventID = ?";

        try {
            prepStat = con.prepareStatement(query);
            prepStat.setString(1, eventID);
            result = prepStat.executeQuery();

            while (result.next()) {
                Booking booking = new Booking();
                booking.setBookingID(result.getString("bookingID"));
                booking.setEventID(result.getString("eventID"));
                booking.setEquipmentID(result.getString("equipmentID"));
                booking.setDeliveryDate(result.getDate("deliveryDate").toLocalDate());
                booking.setReturnDate(result.getDate("returnDate").toLocalDate());
                bookings.add(booking);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return bookings;
    }
}
