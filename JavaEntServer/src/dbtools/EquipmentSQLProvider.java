/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dbtools;

//import domain
import domain.Equipment;

import domain.EquipmentType;
import domain.Booking;

//import sql exception
import java.sql.SQLException;

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
public class EquipmentSQLProvider extends SQLProvider implements IEquipmentSvc{
    //logger
    private static Logger logger = LogManager.getLogger(EquipmentSQLProvider.class);
    
    
    
    //select param
    public Equipment selectEquipmentParam(String eqID){
        Equipment eq = null;
        
        try {
            logger.warn("Attempting to retrieve data from database, errors may occur");
            String query = "SELECT * FROM Equipment WHERE eqID = ?";          //specify query to pull all records
            prepStat = con.prepareStatement(query);                     //create SQL statement
            prepStat.setString(1, eqID);
            result = prepStat.executeQuery();                           //execute the above query
            logger.info("Record Successfully Retrieved");
            
            while(result.next()) {
                eq = new Equipment();                //declare new Admin
                eq.setEqID(result.getString(1)); //get id from col 1
                
                // Create EquipmentType object and set its properties
                EquipmentType eqType = new EquipmentType();
                eqType.setEqTypeID(result.getString(2)); // Assuming col 2 holds eqTypeID
                eq.setEquipmentType(eqType); // Set the EquipmentType object
                
                eq.setName(result.getString(3)); 
            }
        }
        catch(SQLException ex) {
            logger.error(ex.getMessage());
            JOptionPane.showMessageDialog(null, "Could not retrieve record(s)", "SQL Exception", JOptionPane.ERROR_MESSAGE);
        }
        
        return eq;
    }
    
    //select all
    public ArrayList<Equipment> selectAllEquipment(){
        ArrayList<Equipment> list = new ArrayList<Equipment>();         //create array list to collect results
        
        try {
            logger.warn("Attempting to retrieve data from database, errors may occur");
            stat = con.createStatement(); 
            String query = "SELECT * FROM Equipment"; 
            logger.info(query);
            result = stat.executeQuery(query);
            logger.info("Record Successfully Retrieved");
            
            while(result.next()) {
                Equipment eq = new Equipment();
                eq.setEqID(result.getString(1)); //get id from col 1
                
                // Create EquipmentType object and set its properties
                EquipmentType eqType = new EquipmentType();
                eqType.setEqTypeID(result.getString(2)); // Assuming col 2 holds eqTypeID
                eq.setEquipmentType(eqType); // Set the EquipmentType object
                
                eq.setName(result.getString(3)); 
            
                list.add(eq);
            }
        }
        catch(SQLException ex){
            logger.error(ex.getMessage());
            JOptionPane.showMessageDialog(null, "Could not retrieve record(s)", "SQL Exception", JOptionPane.ERROR_MESSAGE);
        }
        
        return list;
    }
    
    //update
    public int updateEquipment(Equipment obj){
        int n;
        
        try {
            logger.warn("Attempting to update data in database, errors may occur");
            stat = con.createStatement();
            String query =
                    "UPDATE Equipment "
                    + "SET eqTypeID = '" + (obj.getEquipmentType() != null ? obj.getEquipmentType().getEqTypeID() : "NULL") + "', "
                    + "name = '" + obj.getName() + "' "
                    + "WHERE eqID = '" + obj.getEqID() + "' ";
            
            logger.info(query);
            n = stat.executeUpdate(query);
            logger.info("Record Successfully Updated");
        }
        catch(SQLException ex) {
            logger.error(ex.getMessage());
            JOptionPane.showMessageDialog(null, "Could not update record(s)", "SQL Exception", JOptionPane.ERROR_MESSAGE);	
            n = 0;
        }
        
        return n;
    }
    
    
    
    //insert param
    public int insertEquipmentParam(Equipment obj){
        int n;
        
        try{
            logger.warn("Attempting to insert data to database, errors may occur");
            String query = "INSERT INTO Equipment (eqID, eqTypeID, name) VALUES (?, ?, ?)";
            prepStat = con.prepareStatement(query);
            prepStat.setString(1, obj.getEqID());
            prepStat.setString(2, obj.getEquipmentType().getEqTypeID());
            prepStat.setString(3, obj.getName());
            
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
    public int deleteEquipment(String eqID){
        int n;
        
        try {
            logger.warn("Attempting to delete data from database, errors may occur");
            stat = con.createStatement();
            String query = 
                    "DELETE FROM Equipment "
                    + "WHERE eqID = '" + eqID + "' ";
            
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
    
    //Retrieve a list of all equipment rented for a given event -- needed for invoice generation
    public List<Equipment> getEquipmentByEvent(String eventID) {
        List<Equipment> equipmentList = new ArrayList<>();

        // Get all bookings for this event
        BookingSQLProvider bookingProvider = new BookingSQLProvider();
        List<Booking> eventBookings = bookingProvider.getBookingsByEventID(eventID);

        EquipmentSQLProvider equipmentProvider = new EquipmentSQLProvider();

        for (Booking booking : eventBookings) {
            Equipment eq = equipmentProvider.selectEquipmentParam(booking.getEquipmentID());
            if (eq != null) {
                equipmentList.add(eq);
            }
        }
        return equipmentList;
    }
}
