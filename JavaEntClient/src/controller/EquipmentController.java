/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controller;

//import domain & view
import domain.EquipmentType;
import view.EquipmentTypeView;

import domain.Equipment;
import view.EquipmentView;

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

/**
 *
 * @author iyana
 */
public class EquipmentController {
    private EquipmentView view;
    private Client client;
    
    //constructor
    public EquipmentController(EquipmentView view, Client client){
        this.view = view;
        this.client = client;
        
        // Load all on startup
        loadAllEquipment();
        
        // Add event listeners
        view.getAddButton().addActionListener(e -> addEquipment());
        view.getUpdateButton().addActionListener(e -> updateEquipment());
        view.getDeleteButton().addActionListener(e -> deleteEquipment());
        view.getSearchButton().addActionListener(e -> searchEquipment());
    }
    
    //load all method - select all
    private void loadAllEquipment(){
        try{
            client.sendAction("Select All Equipment");
            List<Equipment> eqList = (List<Equipment>)client.receiveResponseObject();
            
            String[][] data = new String[eqList.size()][3];
            for (int i = 0; i < eqList.size(); i++){
                Equipment eq = eqList.get(i);
                data[i][0] = eq.getEqID();
                data[i][1] = eq.getEquipmentType().getEqTypeID();
                data[i][2] = eq.getName();
            }
            String[] columnNames = {"ID", "Equipment Type", "Name"};
            view.getEqTable().setModel(new javax.swing.table.DefaultTableModel(data, columnNames)); 
        }
        catch(Exception ex){
            JOptionPane.showMessageDialog(view, "Error: " + ex.getMessage());
        }
    }
    
    //add method
    private void addEquipment (){
        JTextField idField = new JTextField();
        JTextField eqTypeIDField = new JTextField();
        JTextField nameField = new JTextField();
        
        Object[] message = {
            "Equipment ID:", idField,
            "Equipment Type ID:", eqTypeIDField,
            "Name:", nameField
        };
        
        int option = JOptionPane.showConfirmDialog(view, message, "Add Equipment", JOptionPane.OK_CANCEL_OPTION);
        if (option == JOptionPane.OK_OPTION){
            try{
                String id = idField.getText().trim();
                String eqTypeID = eqTypeIDField.getText().trim();
                String name = nameField.getText().trim();
                
                // Create EquipmentType object
                EquipmentType eqType = new EquipmentType();
                eqType.setEqTypeID(eqTypeID);
                
                // Pass EquipmentType object instead of string
                Equipment newEq = new Equipment(id, eqType, name);
                client.sendAction("Insert Equipment");
                client.sendObject(newEq);
                
                // Correctly receive the boolean response first
                boolean isAdded = client.receiveResponseBoolean();
                
                if (isAdded) {
                    JOptionPane.showMessageDialog(view, "Equipment added successfully!");
                    loadAllEquipment(); // Now we send "Select All" AFTER we confirmed the response
                } else {
                    JOptionPane.showMessageDialog(view, "Addition failed. Please try again.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
            catch(Exception ex){
                JOptionPane.showMessageDialog(view, "Error: " + ex.getMessage());
            }
        }
    }
    
    //update method
    private void updateEquipment(){
        int selectedRow = view.getEqTable().getSelectedRow();
        if (selectedRow == -1){
            JOptionPane.showMessageDialog(view, "Please select an equipment to update.");
            return;
        }
        
        String id = (String)view.getEqTable().getValueAt(selectedRow, 0);
        String currentEqTypeID = (String)view.getEqTable().getValueAt(selectedRow, 1);
        String currentName = (String)view.getEqTable().getValueAt(selectedRow, 2);
        
        JTextField eqTypeIDField = new JTextField(currentEqTypeID);
        JTextField nameField = new JTextField(currentName);
        
        Object[] message = {
            "Equipment Type ID:", eqTypeIDField,  
            "Name:", nameField
        };
        
        int option = JOptionPane.showConfirmDialog(view, message, "Update Equipment", JOptionPane.OK_CANCEL_OPTION);
        if (option == JOptionPane.OK_OPTION){
            try{
                String newEqTypeID = eqTypeIDField.getText().trim();
                String newName = nameField.getText().trim();
                
                // Create EquipmentType object
                EquipmentType eqType = new EquipmentType();
                eqType.setEqTypeID(newEqTypeID);
                
                // Pass EquipmentType object instead of string
                
                Equipment updatedEq = new Equipment(id, eqType, newName);
                client.sendAction("Update Equipment");
                client.sendObject(updatedEq);
                
                // Correctly receive the boolean response first
                boolean isUpdated = client.receiveResponseBoolean();
                
                if (isUpdated) {
                    JOptionPane.showMessageDialog(view, "Equipment updated successfully!");
                    loadAllEquipment(); // Now we send "Select All" AFTER we confirmed the response
                } else {
                    JOptionPane.showMessageDialog(view, "Update failed. Please try again.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
            catch(Exception ex){
                JOptionPane.showMessageDialog(view, "Error: " + ex.getMessage());
            }
        }
        view.getEqTable().clearSelection(); // Deselect the row whether the user clicks ok or cancel
    }
    
    //delete method
    private void deleteEquipment(){
        int selectedRow = view.getEqTable().getSelectedRow();
        if (selectedRow == -1){
            JOptionPane.showMessageDialog(view, "Please select an equipment to delete.");
            return;
        }
        
        String id = (String)view.getEqTable().getValueAt(selectedRow, 0);
        int confirm = JOptionPane.showConfirmDialog(view, "Are you sure you want to delete this equipment?", "Delete Equipment", JOptionPane.YES_NO_OPTION);
        
        if (confirm == JOptionPane.YES_OPTION){
            try{
                client.sendAction("Delete Equipment");
                client.sendObject(id);
                
                // Correctly receive the boolean response first
                boolean isDeleted = client.receiveResponseBoolean();
                
                if (isDeleted) {
                    JOptionPane.showMessageDialog(view, "Equipment deleted successfully!");
                    loadAllEquipment(); // Now we send "Select All" AFTER we confirmed the response
                } else {
                    JOptionPane.showMessageDialog(view, "Deletion failed. Please try again.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
            catch(Exception ex){
                JOptionPane.showMessageDialog(view, "Error: " + ex.getMessage());
            }
        }
        view.getEqTable().clearSelection(); // Deselect the row whether the user clicks yes or no
    }
    
    //search method
    private void searchEquipment(){
        String searchId = view.getSearchField().getText();
        if (searchId.isEmpty()){
            JOptionPane.showMessageDialog(view, "Please enter an Equipment ID to search.");
            return;
        } 
        
        try {
            client.sendAction("Select Equipment");
            client.sendObject(searchId);
            Equipment eq = (Equipment)client.receiveResponseObject();
            
            if(eq != null){
                JOptionPane.showMessageDialog(view, "Equipment Found:\nID: " + eq.getEqID()+ 
                        "\nEquipment Type: " + eq.getEquipmentType().getEqTypeID()+
                        "\nName: " + eq.getName());
            } /*else {
                JOptionPane.showMessageDialog(view, "Equipment not found.");
            }*/
        }
        catch(Exception ex){
            JOptionPane.showMessageDialog(view, "Equipment not found.");
            //JOptionPane.showMessageDialog(view, "Error: " + ex.getMessage());
            System.err.println("Error while searching for price: " + ex.getMessage());
            ex.printStackTrace(); // This prints the full error details to the console
        }
    }
}
