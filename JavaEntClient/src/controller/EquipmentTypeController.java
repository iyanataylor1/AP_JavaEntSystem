/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controller;

//import domain & view
import domain.EquipmentType;
import view.EquipmentTypeView;

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
public class EquipmentTypeController {
    private EquipmentTypeView view;
    private Client client;
   
    //constructor
    public EquipmentTypeController(EquipmentTypeView view, Client client){
        this.view = view;
        this.client = client;
        
        // Load all on startup
        loadAllEquipmentTypes();
        
        // Add event listeners
        view.getAddButton().addActionListener(e -> addEqType());
        view.getUpdateButton().addActionListener(e -> updateEqType());
        view.getDeleteButton().addActionListener(e -> deleteEqType());
        view.getSearchButton().addActionListener(e -> searchEqType());
    }
    
    //load all method - select all
    private void loadAllEquipmentTypes(){
        try{
            client.sendAction("Select All Equipment Types");
            List<EquipmentType> eqTypeList = (List<EquipmentType>)client.receiveResponseObject();
            
            String[][] data = new String[eqTypeList.size()][2];
            for (int i = 0; i < eqTypeList.size(); i++){
                EquipmentType eqType = eqTypeList.get(i);
                data[i][0] = eqType.getEqTypeID();
                data[i][1] = eqType.getName();
            }
            
            String[] columnNames = {"ID", "Name"};
            view.getEqTypeTable().setModel(new javax.swing.table.DefaultTableModel(data, columnNames));
        }
        catch(Exception ex){
            JOptionPane.showMessageDialog(view, "Error: " + ex.getMessage());
        }
    }
    
    //add method
    private void addEqType(){
        JTextField idField = new JTextField();
        JTextField nameField = new JTextField();
        
        Object[] message = {
            "Equipment Type ID:", idField,
            "Name:", nameField,
        };
        
        int option = JOptionPane.showConfirmDialog(view, message, "Add Equipment Type", JOptionPane.OK_CANCEL_OPTION);
        if (option == JOptionPane.OK_OPTION){
            try{
                String id = idField.getText().trim();
                String name = nameField.getText().trim();
                
                EquipmentType newEqType = new EquipmentType(id, name);
                client.sendAction("Insert Equipment Type");
                client.sendObject(newEqType);
                
                // Correctly receive the boolean response first
                boolean isAdded = client.receiveResponseBoolean();
                
                if (isAdded) {
                    JOptionPane.showMessageDialog(view, "Equipment Type added successfully!");
                    loadAllEquipmentTypes(); // Now we send "Select All" AFTER we confirmed the response
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
    private void updateEqType(){
        int selectedRow = view.getEqTypeTable().getSelectedRow();
        if (selectedRow == -1){
            JOptionPane.showMessageDialog(view, "Please select an equipment type to update.");
            return;
        }
        
        String id = (String)view.getEqTypeTable().getValueAt(selectedRow, 0);
        String currentName = (String)view.getEqTypeTable().getValueAt(selectedRow, 1);
        
        JTextField nameField = new JTextField(currentName);
        
        Object[] message = {
            "Name:", nameField,
        };
        
        int option = JOptionPane.showConfirmDialog(view, message, "Update Equipment Type", JOptionPane.OK_CANCEL_OPTION);
        if (option == JOptionPane.OK_OPTION){
            try{
                String newName = nameField.getText().trim();
                
                EquipmentType updatedEqType = new EquipmentType(id, newName);
                client.sendAction("Update Equipment Type");
                client.sendObject(updatedEqType);
                
                // Correctly receive the boolean response first
                boolean isUpdated = client.receiveResponseBoolean();
                
                if (isUpdated) {
                    JOptionPane.showMessageDialog(view, "Equipment Type updated successfully!");
                    loadAllEquipmentTypes(); // Now we send "Select All" AFTER we confirmed the response
                } else {
                    JOptionPane.showMessageDialog(view, "Update failed. Please try again.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
            catch(Exception ex){
                JOptionPane.showMessageDialog(view, "Error: " + ex.getMessage());
            }
        }
        view.getEqTypeTable().clearSelection(); // Deselect the row whether the user clicks ok or cancel
    }
    
    //delete method
    private void deleteEqType(){
        int selectedRow = view.getEqTypeTable().getSelectedRow();
        if (selectedRow == -1){
            JOptionPane.showMessageDialog(view, "Please select an equipment type to delete.");
            return;
        }
        
        String id = (String)view.getEqTypeTable().getValueAt(selectedRow, 0);
        int confirm = JOptionPane.showConfirmDialog(view, "Are you sure you want to delete this equipment type?", "Delete Equipment Type", JOptionPane.YES_NO_OPTION);
        
        if (confirm == JOptionPane.YES_OPTION){
            try{
                client.sendAction("Delete Equipment Type");
                client.sendObject(id);
                
                // Correctly receive the boolean response first
                boolean isDeleted = client.receiveResponseBoolean();
                
                if (isDeleted) {
                    JOptionPane.showMessageDialog(view, "Equipment Type deleted successfully!");
                    loadAllEquipmentTypes(); // Now we send "Select All" AFTER we confirmed the response
                } else {
                    JOptionPane.showMessageDialog(view, "Deletion failed. Please try again.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
            catch(Exception ex){
                JOptionPane.showMessageDialog(view, "Error: " + ex.getMessage());
            }
        }
        view.getEqTypeTable().clearSelection(); // Deselect the row whether the user clicks yes or no
    }
    
    //search method
    private void searchEqType(){
       String searchId = view.getSearchField().getText();
        if (searchId.isEmpty()){
            JOptionPane.showMessageDialog(view, "Please enter an Equipment Type ID to search.");
            return;
        } 
        
        try {
            client.sendAction("Select Equipment Type");
            client.sendObject(searchId);
            EquipmentType eqType = (EquipmentType)client.receiveResponseObject();
            
            if(eqType != null){
                JOptionPane.showMessageDialog(view, "Equipment Type Found:\nID: " + eqType.getEqTypeID()+ 
                        "\nName: " + eqType.getName());
            } /*else {
                JOptionPane.showMessageDialog(view, "Equipment Type not found.");
            }*/
        }
        catch(Exception ex){
            JOptionPane.showMessageDialog(view, "Equipment Type not found.");
            //JOptionPane.showMessageDialog(view, "Error: " + ex.getMessage());
            System.err.println("Error while searching for price: " + ex.getMessage());
            ex.printStackTrace(); // This prints the full error details to the console
        }
    }
}
