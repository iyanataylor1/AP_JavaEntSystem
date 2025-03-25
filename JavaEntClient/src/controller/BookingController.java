/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controller;

//import domain & view
import domain.Booking;
import view.BookingView;

//import network tools
import networktools.Client;

//import lightweights & heavyweights
import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

//imports for exception handling
import java.io.IOException;

//import for generics & collections
import java.util.List;

// Import for formatting
import java.time.format.DateTimeFormatter; 
import java.time.LocalDate;
import java.time.format.DateTimeParseException;

/**
 *
 * @author iyana
 */
public class BookingController {
    private BookingView view;
    private Client client;
    
    //constructor
    public BookingController(BookingView view, Client client){
        this.view = view;
        this.client = client;
        
        // Load all on startup
        loadAllBookings();
        
        // Add event listeners
        view.getAddButton().addActionListener(e -> addBooking());
        view.getUpdateButton().addActionListener(e -> updateBooking());
        view.getDeleteButton().addActionListener(e -> deleteBooking());
        view.getSearchButton().addActionListener(e -> searchBooking());
    }
    
    //load all method - select all
    private void loadAllBookings(){
        try{
            client.sendAction("Select All Bookings");
            List<Booking> bookingList = (List<Booking>)client.receiveResponseObject();
            
            String[][] data = new String[bookingList.size()][5];
            
            // Define the date format
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd"); 
        
            for (int i = 0; i < bookingList.size(); i++){
                Booking booking = bookingList.get(i);
                data[i][0] = booking.getBookingID();
                data[i][1] = booking.getEventID();
                data[i][2] = booking.getEquipmentID();
                //data[i][3] = booking.getDeliveryDate();
                //data[i][4] = booking.getReturnDate();
                data[i][3] = booking.getDeliveryDate().format(formatter); // Convert LocalDate to String
                data[i][4] = booking.getReturnDate().format(formatter);
            }
            
            String[] columnNames = {"ID", "Event ID", "Equipment ID", "Delivery Date", "Return Date"};
            view.getBookingTable().setModel(new javax.swing.table.DefaultTableModel(data, columnNames));
        }
        catch(Exception ex){
            JOptionPane.showMessageDialog(view, "Error: " + ex.getMessage());
        }
    }
    
    //add method
    private void addBooking(){
        JTextField idField = new JTextField();
        JTextField eventIDField = new JTextField();
        JTextField eqIDField = new JTextField();
        JTextField deliveryDateField = new JTextField();
        JTextField returnDateField = new JTextField();
        
        Object[] message = {
            "Booking ID:", idField,
            "Event ID:", eventIDField,
            "Equipment ID:", eqIDField,
            "Delivery Date (yyyy-MM-dd):", deliveryDateField,
            "Return Date (yyyy-MM-dd):", returnDateField
        };
        
        int option = JOptionPane.showConfirmDialog(view, message, "Add Booking", JOptionPane.OK_CANCEL_OPTION);
        if (option == JOptionPane.OK_OPTION){
            try{
                // Define the date format
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

                String id = idField.getText().trim();
                String eventID = eventIDField.getText().trim();
                String eqID = eqIDField.getText().trim();
                //String deliveryDate = deliveryDateField.getText().trim();
                //String returnDate = returnDateField.getText().trim();
                // Convert String to LocalDate
                LocalDate deliveryDate = LocalDate.parse(deliveryDateField.getText().trim(), formatter);
                LocalDate returnDate = LocalDate.parse(returnDateField.getText().trim(), formatter);
                
                Booking newBooking = new Booking(id, eventID, eqID, deliveryDate, returnDate);
                client.sendAction("Insert Booking");
                client.sendObject(newBooking);
                
                // Correctly receive the boolean response first
                boolean isAdded = client.receiveResponseBoolean();
                
                if (isAdded) {
                    JOptionPane.showMessageDialog(view, "Booking added successfully!");
                    loadAllBookings(); // Now we send "Select All" AFTER we confirmed the response
                } else {
                    JOptionPane.showMessageDialog(view, "Addition failed. Please try again.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
            catch(DateTimeParseException e){
                JOptionPane.showMessageDialog(view, "Invalid date format. Please enter in yyyy-MM-dd format.", "Error", JOptionPane.ERROR_MESSAGE);
            }
            catch(Exception ex){
                JOptionPane.showMessageDialog(view, "Error: " + ex.getMessage());
            }
        }
    }
    
    //update method
    private void updateBooking(){
        int selectedRow = view.getBookingTable().getSelectedRow();
        if (selectedRow == -1){
            JOptionPane.showMessageDialog(view, "Please select a booking to update.");
            return;
        }
        
        String id = (String)view.getBookingTable().getValueAt(selectedRow, 0);
        String currentEventID = (String)view.getBookingTable().getValueAt(selectedRow, 1);
        String currentEqID = (String)view.getBookingTable().getValueAt(selectedRow, 2);
        String currentDeliveryDate = (String)view.getBookingTable().getValueAt(selectedRow, 3);
        String currentReturnDate = (String)view.getBookingTable().getValueAt(selectedRow, 4);
        
        JTextField eventIDField = new JTextField(currentEventID);
        JTextField eqIDField = new JTextField(currentEqID);
        JTextField deliveryDateField = new JTextField(currentDeliveryDate);
        JTextField returnDateField = new JTextField(currentReturnDate);
        
        Object[] message = {
            "Event ID:", eventIDField,
            "Equipment ID:", eqIDField,
            "Delivery Date (yyyy-MM-dd):", deliveryDateField,
            "Return Date (yyyy-MM-dd):", returnDateField
        };
        
        int option = JOptionPane.showConfirmDialog(view, message, "Update Booking", JOptionPane.OK_CANCEL_OPTION);
        if (option == JOptionPane.OK_OPTION){
            try{
                // Define the date format
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

                String newEventID = eventIDField.getText().trim();
                String newEqID = eqIDField.getText().trim();
                //String newDeliveryDate = deliveryDateField.getText().trim();
                //String newReturnDate = returnDateField.getText().trim();
                // Convert String to LocalDate
                LocalDate newDeliveryDate = LocalDate.parse(deliveryDateField.getText().trim(), formatter);
                LocalDate newReturnDate = LocalDate.parse(returnDateField.getText().trim(), formatter);
                
                Booking updatedBooking = new Booking(id, newEventID, newEqID, newDeliveryDate, newReturnDate);
                client.sendAction("Update Booking");
                client.sendObject(updatedBooking);
                
                // Correctly receive the boolean response first
                boolean isUpdated = client.receiveResponseBoolean();
                
                if (isUpdated) {
                    JOptionPane.showMessageDialog(view, "Booking updated successfully!");
                    loadAllBookings(); // Now we send "Select All" AFTER we confirmed the response
                } else {
                    JOptionPane.showMessageDialog(view, "Update failed. Please try again.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
            catch(DateTimeParseException e){
                JOptionPane.showMessageDialog(view, "Invalid date format. Please enter in yyyy-MM-dd format.", "Error", JOptionPane.ERROR_MESSAGE);
            }
            catch(Exception ex){
                JOptionPane.showMessageDialog(view, "Error: " + ex.getMessage());
            }
        }
        view.getBookingTable().clearSelection(); // Deselect the row whether the user clicks ok or cancel
    }
    
    //delete method
    private void deleteBooking(){
        int selectedRow = view.getBookingTable().getSelectedRow();
        if (selectedRow == -1){
            JOptionPane.showMessageDialog(view, "Please select a booking to delete.");
            return;
        }
        
        String id = (String)view.getBookingTable().getValueAt(selectedRow, 0);
        int confirm = JOptionPane.showConfirmDialog(view, "Are you sure you want to delete this booking?", "Delete Booking", JOptionPane.YES_NO_OPTION);
        
        if (confirm == JOptionPane.YES_OPTION){
            try{
                client.sendAction("Delete Booking");
                client.sendObject(id);
                
                // Correctly receive the boolean response first
                boolean isDeleted = client.receiveResponseBoolean();
                
                if (isDeleted) {
                    JOptionPane.showMessageDialog(view, "Booking deleted successfully!");
                    loadAllBookings(); // Now we send "Select All" AFTER we confirmed the response
                } else {
                    JOptionPane.showMessageDialog(view, "Deletion failed. Please try again.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
            catch(Exception ex){
                JOptionPane.showMessageDialog(view, "Error: " + ex.getMessage());
            }
        }
        view.getBookingTable().clearSelection(); // Deselect the row whether the user clicks yes or no
    }
    
    //search method
    private void searchBooking(){
        String searchId = view.getSearchField().getText();
        if (searchId.isEmpty()){
            JOptionPane.showMessageDialog(view, "Please enter a Booking ID to search.");
            return;
        }
        
        try{
            client.sendAction("Select Booking");
            client.sendObject(searchId);
            Booking booking = (Booking)client.receiveResponseObject();
            
            if(booking != null){
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                
                JOptionPane.showMessageDialog(view, "Booking Found:\nID: " + booking.getBookingID()+ 
                        "\nEvent ID: " + booking.getEventID()+ 
                        "\nEquipment ID: " + booking.getEquipmentID()+ 
                        "\nDelivery Date: " + booking.getDeliveryDate().format(formatter)+ 
                        "\nReturn Date: " + booking.getReturnDate().format(formatter));
            } /*else {
                JOptionPane.showMessageDialog(view, "Booking not found.");
            }*/
        }
        catch(Exception ex){
            JOptionPane.showMessageDialog(view, "Booking not found.");
            //JOptionPane.showMessageDialog(view, "Error: " + ex.getMessage());
            System.err.println("Error while searching for price: " + ex.getMessage());
            ex.printStackTrace(); // This prints the full error details to the console
        }
    }
}
