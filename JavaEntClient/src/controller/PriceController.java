/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controller;

//import domain & view
import domain.Price;
import view.PriceView;

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
public class PriceController {
    private PriceView view;
    private Client client;
    
    //constructor
    public PriceController(PriceView view, Client client){
        this.view = view;
        this.client = client;
        
        // Load all on startup
        loadAllPrices();
        
        // Add event listeners
        view.getAddButton().addActionListener(e -> addPrice());
        view.getUpdateButton().addActionListener(e -> updatePrice());
        view.getDeleteButton().addActionListener(e -> deletePrice());
        view.getSearchButton().addActionListener(e -> searchPrice());
    }
    
    //load all method - select all
    private void loadAllPrices(){
        try{
            client.sendAction("Select All Prices");
            List<Price> priceList = (List<Price>)client.receiveResponseObject();
            
            String[][] data = new String[priceList.size()][4];
            
            // Define the date format
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd"); 
        
            for (int i = 0; i < priceList.size(); i++){
                Price price = priceList.get(i);
                data[i][0] = price.getPriceID();
                data[i][1] = price.getEqID();
                //data[i][2] = price.getRatePerHour();
                data[i][2] = String.valueOf(price.getRatePerHour()); // Convert float to String
                //data[i][3] = price.getRateAtDate();
                data[i][3] = price.getRateAtDate().format(formatter); // Convert LocalDate to String
            }
            
            String[] columnNames = {"ID", "Equipment ID", "Rate Per Hour ($)", "Rate At Date"};
            view.getPriceTable().setModel(new javax.swing.table.DefaultTableModel(data, columnNames));
        }
        catch(Exception ex){
            JOptionPane.showMessageDialog(view, "Error: " + ex.getMessage());
        }
    }
    
    //add method
    private void addPrice(){
        JTextField idField = new JTextField();
        JTextField eqIDField = new JTextField();
        JTextField ratePerHourField = new JTextField();
        JTextField rateAtDateField = new JTextField();
        
        Object[] message = {
            "Price ID:", idField,
            "Equipment ID:", eqIDField,
            "Rate Per Hour ($):", ratePerHourField,
            "Rate At Date (yyyy-MM-dd):", rateAtDateField
        };
        
        int option = JOptionPane.showConfirmDialog(view, message, "Add Price", JOptionPane.OK_CANCEL_OPTION);
        if (option == JOptionPane.OK_OPTION){
            try{
                // Define the date format
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

                String id = idField.getText().trim();
                String eqID = eqIDField.getText().trim();
                //String ratePerHour = ratePerHourField.getText().trim();
                float ratePerHour = Float.parseFloat(ratePerHourField.getText().trim()); // Convert String to float
                //String rateAtDate = rateAtDateField.getText().trim();
                LocalDate rateAtDate = LocalDate.parse(rateAtDateField.getText().trim(), formatter); //convert string to local date
            
                Price newPrice = new Price(id, eqID, ratePerHour, rateAtDate);
                client.sendAction("Insert Price");
                client.sendObject(newPrice);
            
                // Correctly receive the boolean response first
                boolean isAdded = client.receiveResponseBoolean();
                
                if (isAdded) {
                    JOptionPane.showMessageDialog(view, "Price added successfully!");
                    loadAllPrices(); // Now we send "Select All" AFTER we confirmed the response
                } else {
                    JOptionPane.showMessageDialog(view, "Addition failed. Please try again.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
            catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(view, "Invalid amount. Please enter a numeric value.", "Error", JOptionPane.ERROR_MESSAGE);
                return; // Exit the method if parsing fails
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
    private void updatePrice(){
        int selectedRow = view.getPriceTable().getSelectedRow();
        if (selectedRow == -1){
            JOptionPane.showMessageDialog(view, "Please select a price to update.");
            return;
        }
        
        String id = (String)view.getPriceTable().getValueAt(selectedRow, 0);
        String currentEqID = (String)view.getPriceTable().getValueAt(selectedRow, 1);
        String currentRatePerHour = (String)view.getPriceTable().getValueAt(selectedRow, 2);
        String currentRateAtDate = (String)view.getPriceTable().getValueAt(selectedRow, 3);
        
        JTextField eqIDField = new JTextField(currentEqID);
        JTextField ratePerHourField = new JTextField(currentRatePerHour);
        JTextField rateAtDateField = new JTextField(currentRateAtDate);
        
        Object[] message = {
            "Equipment ID:", eqIDField,
            "Rate Per Hour ($):", ratePerHourField,
            "Rate At Date (yyyy-MM-dd):", rateAtDateField
        };
        
        int option = JOptionPane.showConfirmDialog(view, message, "Update Price", JOptionPane.OK_CANCEL_OPTION);
        if (option == JOptionPane.OK_OPTION){
            try{
                // Define the date format
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

                String newEqID = eqIDField.getText().trim();
                //String newRatePerHour = ratePerHourField.getText().trim();
                float newRatePerHour = Float.parseFloat(ratePerHourField.getText().trim()); // Convert String to float
                //String newRateAtDate = rateAtDateField.getText().trim();
                LocalDate newRateAtDate = LocalDate.parse(rateAtDateField.getText().trim(), formatter); //convert string to local date
            
                Price updatedPrice = new Price(id, newEqID, newRatePerHour, newRateAtDate);
                client.sendAction("Update Price");
                client.sendObject(updatedPrice);
                
                // Correctly receive the boolean response first
                boolean isUpdated = client.receiveResponseBoolean();
                
                if (isUpdated) {
                    JOptionPane.showMessageDialog(view, "Price updated successfully!");
                    loadAllPrices(); // Now we send "Select All" AFTER we confirmed the response
                } else {
                    JOptionPane.showMessageDialog(view, "Update failed. Please try again.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
            catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(view, "Invalid amount. Please enter a numeric value.", "Error", JOptionPane.ERROR_MESSAGE);
                return; // Exit the method if parsing fails
            }
            catch(DateTimeParseException e){
                JOptionPane.showMessageDialog(view, "Invalid date format. Please enter in yyyy-MM-dd format.", "Error", JOptionPane.ERROR_MESSAGE);
            }
            catch(Exception ex){
                JOptionPane.showMessageDialog(view, "Error: " + ex.getMessage());
            }
        }
        view.getPriceTable().clearSelection(); // Deselect the row whether the user clicks ok or cancel
    }
    
    //delete method
    private void deletePrice(){
        int selectedRow = view.getPriceTable().getSelectedRow();
        if (selectedRow == -1){
            JOptionPane.showMessageDialog(view, "Please select a price to delete.");
            return;
        }
        
        String id = (String)view.getPriceTable().getValueAt(selectedRow, 0);
        int confirm = JOptionPane.showConfirmDialog(view, "Are you sure you want to delete this price?", "Delete Price", JOptionPane.YES_NO_OPTION);
        
        if (confirm == JOptionPane.YES_OPTION){
            try{
                client.sendAction("Delete Price");
                client.sendObject(id);
                
                // Correctly receive the boolean response first
                boolean isDeleted = client.receiveResponseBoolean();
                
                if (isDeleted) {
                    JOptionPane.showMessageDialog(view, "Price deleted successfully!");
                    loadAllPrices(); // Now we send "Select All" AFTER we confirmed the response
                } else {
                    JOptionPane.showMessageDialog(view, "Deletion failed. Please try again.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
            catch(Exception ex){
                JOptionPane.showMessageDialog(view, "Error: " + ex.getMessage());
            }
        }
        view.getPriceTable().clearSelection(); // Deselect the row whether the user clicks yes or no
    }
    
    //search method
    private void searchPrice(){
        String searchId = view.getSearchField().getText();
        if (searchId.isEmpty()){
            JOptionPane.showMessageDialog(view, "Please enter a Price ID to search.");
            return;
        }
        
        try{
            client.sendAction("Select Price");
            client.sendObject(searchId);
            Price price = (Price)client.receiveResponseObject();
            
            if(price != null){
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                
                JOptionPane.showMessageDialog(view, "Price Found:\nID: " + price.getPriceID()+ 
                        "\nEquipment ID: " + price.getEqID()+ 
                        "\nRate Per Hour ($): " + price.getRatePerHour()+ 
                        "\nRate At Date: " + price.getRateAtDate().format(formatter));
            } /*else {
                JOptionPane.showMessageDialog(view, "Price not found.");
            }*/
        }
        catch(Exception ex){
            JOptionPane.showMessageDialog(view, "Price not found.");
            //JOptionPane.showMessageDialog(view, "Error: " + ex.getMessage());
            System.err.println("Error while searching for price: " + ex.getMessage());
            ex.printStackTrace(); // This prints the full error details to the console
        }
    }
}
