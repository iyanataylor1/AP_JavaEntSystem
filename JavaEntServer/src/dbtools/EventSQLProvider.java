/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dbtools;

//import domain
import domain.Event;

import domain.Equipment;
import domain.Booking;

import java.time.LocalDate;
import java.util.List;

//import sql exception
import java.sql.SQLException;

//import arraylist
import java.util.ArrayList;

//import swing for joptionpane
import javax.swing.JOptionPane;

//import for logging
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 *
 * @author iyana
 */
public class EventSQLProvider extends SQLProvider implements IEventSvc{
    //logger
    private static Logger logger = LogManager.getLogger(EventSQLProvider.class);
     
    
    
    //select param
    public Event selectEventParam(String eventID){
        Event event = null;
        
        try{
            logger.warn("Attempting to retrieve data from database, errors may occur");
            String query = "SELECT * FROM Event WHERE eventID = ?";          //specify query to pull all records
            prepStat = con.prepareStatement(query);                     //create SQL statement
            prepStat.setString(1, eventID);
            result = prepStat.executeQuery();                           //execute the above query
            logger.info("Record Successfully Retrieved");
            
            while(result.next()) {
                event = new Event();         
                event.setEventID(result.getString(1));
                event.setEventName(result.getString(2));
                event.setStreet(result.getString(3));
                event.setTown(result.getString(4));
                event.setParish(result.getString(5));
                //event.setStartDate(result.getDate(6));
                //event.setEndDate(result.getDate(7));
                event.setStartDate(result.getDate(6).toLocalDate());  // Convert Date to LocalDate
                event.setEndDate(result.getDate(7).toLocalDate());    // Convert Date to LocalDate
                event.setCustomerID(result.getString(8));
            }
        }
        catch(SQLException ex){
            logger.error(ex.getMessage());
            JOptionPane.showMessageDialog(null, "Could not retrieve record(s)", "SQL Exception", JOptionPane.ERROR_MESSAGE);
        
        }
        return event;
    }
    
    //select all
    public ArrayList<Event> selectAllEvent(){
        ArrayList<Event> list = new ArrayList<Event>();
        
        try{
            logger.warn("Attempting to retrieve data from database, errors may occur");
            stat = con.createStatement(); 
            String query = "SELECT * FROM Event";
            logger.info(query);
            result = stat.executeQuery(query);
            logger.info("Record Successfully Retrieved");
            
            while(result.next()){
                Event event = new Event();         
                event.setEventID(result.getString(1));
                event.setEventName(result.getString(2));
                event.setStreet(result.getString(3));
                event.setTown(result.getString(4));
                event.setParish(result.getString(5));
                //event.setStartDate(result.getDate(6));
                //event.setEndDate(result.getDate(7));
                event.setStartDate(result.getDate(6).toLocalDate());  // Convert Date to LocalDate
                event.setEndDate(result.getDate(7).toLocalDate());    // Convert Date to LocalDate
                event.setCustomerID(result.getString(8));
                list.add(event);
            }
        }
        catch(SQLException ex){
            logger.error(ex.getMessage());
            JOptionPane.showMessageDialog(null, "Could not retrieve record(s)", "SQL Exception", JOptionPane.ERROR_MESSAGE);
        }
        return list;
    }
    
    //update
    public int updateEvent(Event obj){
        int n;
        
        try{
            logger.warn("Attempting to update data in database, errors may occur");
            stat = con.createStatement();
            String query =
                    "UPDATE Event "
                    + "SET eventName = '" + obj.getEventName() + "', "
                    + "street = '" + obj.getStreet() + "', "
                    + "town = '" + obj.getTown() + "', "
                    + "parish = '" + obj.getParish() + "', "
                    + "startDate = '" + obj.getStartDate() + "', "
                    + "endDate = '" + obj.getEndDate() + "', "
                    + "customerID = '" + obj.getCustomerID() + "' "
                    + "WHERE eventID = '" + obj.getEventID() + "' ";
            
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
    
    
    
    //insert param
    public int insertEventParam(Event obj){
        int n;
        
        try{
            logger.warn("Attempting to insert data to database, errors may occur");
            String query = "INSERT INTO Event (eventID, eventName, street, town, parish, startDate, endDate, customerID) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
            prepStat = con.prepareStatement(query);
            prepStat.setString(1, obj.getEventID());
            prepStat.setString(2, obj.getEventName());
            prepStat.setString(3, obj.getStreet());
            prepStat.setString(4, obj.getTown());
            prepStat.setString(5, obj.getParish());
            prepStat.setDate(6, java.sql.Date.valueOf(obj.getStartDate()));
            prepStat.setDate(7, java.sql.Date.valueOf(obj.getEndDate()));
            prepStat.setString(8, obj.getCustomerID());
            
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
    public int deleteEvent(String eventID){
        int n;
        
        try{
            logger.warn("Attempting to delete data from database, errors may occur");
            stat = con.createStatement();
            String query = 
                    "DELETE FROM Event "
                    + "WHERE eventID = '" + eventID + "' ";
            
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
    public List<Booking> getBookingsByEvent(String eventID) {
        return new BookingSQLProvider().getBookingsByEventID(eventID);
    }
    
    // Display all equipment for this event (by name)  
    public void viewEventEquipment() {
        Event event = new Event();
        Booking bookingDom = new Booking();
        
        System.out.println("Event: " + event.getEventName() + " has the following equipment:");

        BookingSQLProvider bookingProvider = new BookingSQLProvider();
        EquipmentSQLProvider equipmentProvider = new EquipmentSQLProvider();

        List<Booking> eventBookings = bookingProvider.getBookingsByEventID(bookingDom.getEventID());

        for (Booking booking : eventBookings) {
            Equipment eq = equipmentProvider.selectEquipmentParam(booking.getEquipmentID());
            if (eq != null) {
                System.out.println("- " + eq.getName());
            }
        }
    }
}
