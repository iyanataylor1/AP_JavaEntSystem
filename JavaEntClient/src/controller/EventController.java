/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controller;

//import domain & view
import domain.Event;
import view.EventView;

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
public class EventController {
    private EventView view;
    private Client client;
    
    //constructor
    public EventController(EventView view, Client client){
        this.view = view;
        this.client = client;
        
        // Load all on startup
        loadAllEvents();
        
        // Add event listeners
        view.getAddButton().addActionListener(e -> addEvent());
        view.getUpdateButton().addActionListener(e -> updateEvent());
        view.getDeleteButton().addActionListener(e -> deleteEvent());
        view.getSearchButton().addActionListener(e -> searchEvent());
        view.getGenInvoiceButton().addActionListener(e -> {
            int selectedRow = view.getEventTable().getSelectedRow();

            if (selectedRow != -1) {
                String eventID = view.getEventTable().getValueAt(selectedRow, 0).toString(); // Assuming eventID is in column 0
                genInvoice(eventID);
            } else {
                JOptionPane.showMessageDialog(view, "Please select an event to generate an invoice for.");
            }
        });
    }
    
    //load all method - select all
    private void loadAllEvents(){
        try{
            client.sendAction("Select All Events");
            List<Event> eventList = (List<Event>)client.receiveResponseObject();
            
            String[][] data = new String[eventList.size()][8];
            
            // Define the date format
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd"); 
        
            for (int i = 0; i < eventList.size(); i++){
                Event event = eventList.get(i);
                data[i][0] = event.getEventID();
                data[i][1] = event.getEventName();
                data[i][2] = event.getStreet();
                data[i][3] = event.getTown();
                data[i][4] = event.getParish();
                //data[i][5] = event.getStartDate();
                //data[i][6] = event.getEndDate(); 
                data[i][5] = event.getStartDate().format(formatter); // Convert LocalDate to String
                data[i][6] = event.getEndDate().format(formatter);
                data[i][7] = event.getCustomerID();
            }
            
            String[] columnNames = {"ID", "Event Name", "Street", "Town", "Parish", "Start Date", "End Date", "Customer ID"};
            view.getEventTable().setModel(new javax.swing.table.DefaultTableModel(data, columnNames));
        }
        catch(Exception ex){
            JOptionPane.showMessageDialog(view, "Error: " + ex.getMessage());
        }
    }
    
    //add method
    private void addEvent(){
        JTextField idField = new JTextField();
        JTextField eventNameField = new JTextField();
        JTextField streetField = new JTextField();
        JTextField townField = new JTextField();
        JTextField parishField = new JTextField();
        JTextField startDateField = new JTextField();
        JTextField endDateField = new JTextField();
        JTextField customerIDField = new JTextField();
        
        Object[] message = {
            "Event ID:", idField,
            "Event Name:", eventNameField,
            "Street:", streetField,
            "Town:", townField,
            "Parish:", parishField,
            "Start Date (yyyy-MM-dd):", startDateField,
            "End Date (yyyy-MM-dd):", endDateField,
            "Customer ID:", customerIDField
        };
        
        int option = JOptionPane.showConfirmDialog(view, message, "Add Event", JOptionPane.OK_CANCEL_OPTION);
        if (option == JOptionPane.OK_OPTION){
            try{
                // Define the date format
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

                String id = idField.getText().trim();
                String eventName = eventNameField.getText().trim();
                String street = streetField.getText().trim();
                String town = townField.getText().trim();
                String parish = parishField.getText().trim();
                //String startDate = startDateField.getText().trim();
                //String endDate = endDateField.getText().trim();
                // Convert String to LocalDate
                LocalDate startDate = LocalDate.parse(startDateField.getText().trim(), formatter);
                LocalDate endDate = LocalDate.parse(endDateField.getText().trim(), formatter);
                String customerID = customerIDField.getText().trim();
                
                Event newEvent = new Event(id, eventName, street, town, parish, startDate, endDate, customerID);
                client.sendAction("Insert Event");
                client.sendObject(newEvent);
                
                // Correctly receive the boolean response first
                boolean isAdded = client.receiveResponseBoolean();
                
                if (isAdded) {
                    JOptionPane.showMessageDialog(view, "Event added successfully!");
                    loadAllEvents(); // Now we send "Select All" AFTER we confirmed the response
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
    private void updateEvent(){
        int selectedRow = view.getEventTable().getSelectedRow();
        if (selectedRow == -1){
            JOptionPane.showMessageDialog(view, "Please select an event to update.");
            return;
        }
        
        String id = (String)view.getEventTable().getValueAt(selectedRow, 0);
        String currentEventName = (String)view.getEventTable().getValueAt(selectedRow, 1);
        String currentStreet = (String)view.getEventTable().getValueAt(selectedRow, 2);
        String currentTown = (String)view.getEventTable().getValueAt(selectedRow, 3);
        String currentParish = (String)view.getEventTable().getValueAt(selectedRow, 4);
        String currentStartDate = (String)view.getEventTable().getValueAt(selectedRow, 5);
        String currentEndDate = (String)view.getEventTable().getValueAt(selectedRow, 6);
        String currentCustomerID = (String)view.getEventTable().getValueAt(selectedRow, 7);
        
        JTextField eventNameField = new JTextField(currentEventName);
        JTextField streetField = new JTextField(currentStreet);
        JTextField townField = new JTextField(currentTown);
        JTextField parishField = new JTextField(currentParish);
        JTextField startDateField = new JTextField(currentStartDate);
        JTextField endDateField = new JTextField(currentEndDate);
        JTextField customerIDField = new JTextField(currentCustomerID);
        
        Object[] message = {
            "Event Name:", eventNameField,
            "Street:", streetField,
            "Town:", townField,
            "Parish:", parishField,
            "Start Date (yyyy-MM-dd):", startDateField,
            "End Date (yyyy-MM-dd):", endDateField,
            "Customer ID:", customerIDField
        };
        
        int option = JOptionPane.showConfirmDialog(view, message, "Update Event", JOptionPane.OK_CANCEL_OPTION);
        if (option == JOptionPane.OK_OPTION){
            try{
                // Define the date format
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

                String newEventName = eventNameField.getText().trim();
                String newStreet = streetField.getText().trim();
                String newTown = townField.getText().trim();
                String newParish = parishField.getText().trim();
                //String newStartDate = startDateField.getText().trim();
                //String newEndDate = endDateField.getText().trim();
                // Convert String to LocalDate
                LocalDate newStartDate = LocalDate.parse(startDateField.getText().trim(), formatter);
                LocalDate newEndDate = LocalDate.parse(endDateField.getText().trim(), formatter);
                String newCustomerID = customerIDField.getText().trim();
                
                Event updatedEvent = new Event(id, newEventName, newStreet, newTown, newParish, newStartDate, newEndDate, newCustomerID);
                client.sendAction("Update Event");
                client.sendObject(updatedEvent);
                
                // Correctly receive the boolean response first
                boolean isUpdated = client.receiveResponseBoolean();
                
                if (isUpdated) {
                    JOptionPane.showMessageDialog(view, "Event updated successfully!");
                    loadAllEvents(); // Now we send "Select All" AFTER we confirmed the response
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
        view.getEventTable().clearSelection(); // Deselect the row whether the user clicks ok or cancel
    }
    
    //delete method
    private void deleteEvent(){
        int selectedRow = view.getEventTable().getSelectedRow();
        if (selectedRow == -1){
            JOptionPane.showMessageDialog(view, "Please select an event to delete.");
            return;
        }
        
        String id = (String)view.getEventTable().getValueAt(selectedRow, 0);
        int confirm = JOptionPane.showConfirmDialog(view, "Are you sure you want to delete this event?", "Delete Event", JOptionPane.YES_NO_OPTION);
        
        if (confirm == JOptionPane.YES_OPTION){
            try{
                client.sendAction("Delete Event");
                client.sendObject(id);
                
                // Correctly receive the boolean response first
                boolean isDeleted = client.receiveResponseBoolean();
                
                if (isDeleted) {
                    JOptionPane.showMessageDialog(view, "Event deleted successfully!");
                    loadAllEvents(); // Now we send "Select All" AFTER we confirmed the response
                } else {
                    JOptionPane.showMessageDialog(view, "Deletion failed. Please try again.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
            catch(Exception ex){
                JOptionPane.showMessageDialog(view, "Error: " + ex.getMessage());
            }
        }
        view.getEventTable().clearSelection(); // Deselect the row whether the user clicks yes or no
    }
    
    //search method
    private void searchEvent(){
        String searchId = view.getSearchField().getText();
        if (searchId.isEmpty()){
            JOptionPane.showMessageDialog(view, "Please enter a Event ID to search.");
            return;
        }
        
        try {
            client.sendAction("Select Event");
            client.sendObject(searchId);
            Event event = (Event)client.receiveResponseObject();
            
            if(event != null){
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                
                JOptionPane.showMessageDialog(view, "Event Found:\nID: " + event.getEventID()+ 
                        "\nEvent Name: " + event.getEventName()+ 
                        "\nStreet: " + event.getStreet()+ 
                        "\nTown: " + event.getTown()+ 
                        "\nParish: " + event.getParish()+ 
                        "\nStart Date: " + event.getStartDate().format(formatter)+ 
                        "\nEnd Date: " + event.getEndDate().format(formatter)+ 
                        "\nCustomer: " + event.getCustomerID());
            } /*else {
                JOptionPane.showMessageDialog(view, "Event not found.");
            }*/
        }
        catch(Exception ex){
            JOptionPane.showMessageDialog(view, "Event not found.");
            //JOptionPane.showMessageDialog(view, "Error: " + ex.getMessage());
            System.err.println("Error while searching for price: " + ex.getMessage());
            ex.printStackTrace(); // This prints the full error details to the console
        }
    }
    
    //generate invoice method
    private void genInvoice(String eventID){
        try {
        client.sendAction("Generate Invoice"); // Send action to server
        client.sendObject(eventID); // Send event ID
        
        boolean isGenerated = client.receiveResponseBoolean(); // Get response
        
        if (isGenerated) {
            JOptionPane.showMessageDialog(view, "Invoice generated successfully!");
        } else {
            JOptionPane.showMessageDialog(view, "Failed to generate invoice. It may already exist or there may be no bookings associated with this event.", "Error", JOptionPane.ERROR_MESSAGE);
        }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(view, "Error: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }

        view.getEventTable().clearSelection(); // Deselect the row whether the user clicks ok or cancel
    }
}
