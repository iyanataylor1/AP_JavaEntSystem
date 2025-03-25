/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dbtools;

//import domain
import domain.Invoice;

import domain.Booking;
import domain.Event;

import utility.InvoiceGenerator;

import java.time.LocalDate;

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
public class InvoiceSQLProvider extends SQLProvider implements IInvoiceSvc{
    //logger
    private static Logger logger = LogManager.getLogger(EventSQLProvider.class);
   
    
    
    //select param
    public Invoice selectInvoiceParam(String invoiceID){
        Invoice invoice = null;
        
        try{
            logger.warn("Attempting to retrieve data from database, errors may occur");
            String query = "SELECT * FROM Invoice WHERE invoiceID = ?";          //specify query to pull all records
            prepStat = con.prepareStatement(query);                     //create SQL statement
            prepStat.setString(1, invoiceID);
            result = prepStat.executeQuery();                           //execute the above query
            logger.info("Record Successfully Retrieved");
            
            while(result.next()) {
               invoice = new Invoice();         
               invoice.setInvoiceID(result.getString(1));
               invoice.setEventID(result.getString(2));
               invoice.setCustomerID(result.getString(3));
               invoice.setTotalAmount(result.getDouble(4));
               invoice.setIssueDate(result.getDate(5).toLocalDate());    // Convert Date to LocalDate
               invoice.setInvoiceStatus(result.getString(6));
            }
        }
        catch(SQLException ex){
            logger.error(ex.getMessage());
            JOptionPane.showMessageDialog(null, "Could not retrieve record(s)", "SQL Exception", JOptionPane.ERROR_MESSAGE);
        
        }
        return invoice;
    }
    
    //select all
    public ArrayList<Invoice> selectAllInvoice(){
        ArrayList<Invoice> list = new ArrayList<Invoice>();
        
        try{
            logger.warn("Attempting to retrieve data from database, errors may occur");
            stat = con.createStatement(); 
            String query = "SELECT * FROM Invoice";
            logger.info(query);
            result = stat.executeQuery(query);
            logger.info("Record Successfully Retrieved");
            
            while(result.next()){
                Invoice invoice = new Invoice();         
                invoice.setInvoiceID(result.getString(1));
                invoice.setEventID(result.getString(2));
                invoice.setCustomerID(result.getString(3));
                invoice.setTotalAmount(result.getDouble(4));
                invoice.setIssueDate(result.getDate(5).toLocalDate());    // Convert Date to LocalDate
                invoice.setInvoiceStatus(result.getString(6));
                list.add(invoice);
            }
        }
        catch(SQLException ex){
            logger.error(ex.getMessage());
            JOptionPane.showMessageDialog(null, "Could not retrieve record(s)", "SQL Exception", JOptionPane.ERROR_MESSAGE);
        }
        return list;
    }
    
    //update
    public int updateInvoice(Invoice obj){
        int n;
        
        try{
            logger.warn("Attempting to update data in database, errors may occur");
            stat = con.createStatement();
            String query =
                    "UPDATE Invoice "
                    + "SET eventID = '" + obj.getEventID() + "', "
                    + "customerID = '" + obj.getCustomerID() + "', "
                    + "totalAmount = '" + obj.getTotalAmount() + "', "
                    + "issueDate = '" + obj.getIssueDate() + "', "
                    + "invoiceStatus = '" + obj.getInvoiceStatus() + "' "
                    + "WHERE invoiceID = '" + obj.getInvoiceID() + "' ";
            
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
    public int insertInvoiceParam(Invoice obj){
        int n;
        
        try{
            logger.warn("Attempting to insert data to database, errors may occur");
            String query = "INSERT INTO Invoice (invoiceID, eventID, customerID, totalAmount, issueDate, invoiceStatus) VALUES (?, ?, ?, ?, ?, ?)";
            prepStat = con.prepareStatement(query);
            prepStat.setString(1, obj.getInvoiceID());
            prepStat.setString(2, obj.getEventID());
            prepStat.setString(3, obj.getCustomerID());
            prepStat.setDouble(4, obj.getTotalAmount());
            prepStat.setDate(5, java.sql.Date.valueOf(obj.getIssueDate()));
            prepStat.setString(6, obj.getInvoiceStatus());
            
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
    public int deleteInvoice(String invoiceID){
        int n;
        
        try{
            logger.warn("Attempting to delete data from database, errors may occur");
            stat = con.createStatement();
            String query = 
                    "DELETE FROM Invoice "
                    + "WHERE invoiceID = '" + invoiceID + "' ";
            
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
    
    //check if an invoice exists for a particular event -- needed for invoice generation
    public boolean checkInvoiceExists(String eventID) {
        String query = "SELECT COUNT(*) FROM Invoice WHERE eventID = ?";

        try {
            prepStat = con.prepareStatement(query);
            prepStat.setString(1, eventID);
            result = prepStat.executeQuery();

            if (result.next()) {
                return result.getInt(1) > 0; // If count > 0, invoice exists
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false; // Default return if something goes wrong
    }
    
    //automatically generate an invoiceID -- needed for invoice generation
    private String generateInvoiceID() {
        String lastID = null;
        String query = "SELECT invoiceID FROM Invoice ORDER BY invoiceID DESC LIMIT 1";

        try {
            prepStat = con.prepareStatement(query);
            result = prepStat.executeQuery();

            if (result.next()) {
                lastID = result.getString("invoiceID");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        // Generate new ID
        if (lastID == null) {
            return "I001"; // First invoice
        } else {
            int num = Integer.parseInt(lastID.substring(1)) + 1;
            return String.format("I%03d", num);
        }
    }

    //the method actually generate an invoice per event 
    public boolean generateInvoiceForEvent(String eventID) {
        EventSQLProvider eventProvider = new EventSQLProvider();
        BookingSQLProvider bookingProvider = new BookingSQLProvider();
        InvoiceSQLProvider invoiceProvider = new InvoiceSQLProvider();

        // Step 1: Check if an invoice already exists for this event
        if (invoiceProvider.checkInvoiceExists(eventID)) {
            System.out.println("Invoice already exists for event " + eventID);
            return false;
        }

        // Step 2: Get all bookings for this event
        List<Booking> bookings = bookingProvider.getBookingsByEventID(eventID);
        if (bookings.isEmpty()) {
            System.out.println("No bookings found for event " + eventID);
            return false;
        }

        // Step 3: Calculate total amount
        double totalAmount = InvoiceGenerator.calculateTotalAmount(bookings);

        // Step 4: Fetch event details (to get customerID)
        Event event = eventProvider.selectEventParam(eventID);
        if (event == null) {
            System.out.println("Event not found for ID: " + eventID);
            return false;
        }

        // Step 5: Generate a new invoice ID
        String newInvoiceID = generateInvoiceID(); // We need a method for this

        // Step 6: Create Invoice object
        Invoice invoice = new Invoice(
            newInvoiceID, 
            eventID,
            event.getCustomerID(), 
            totalAmount, 
            LocalDate.now(),  // Issue date is today
            "Pending"
        );

        // Step 7: Insert into database
        int rowsInserted = insertInvoiceParam(invoice);
        return rowsInserted > 0;
    }
}
