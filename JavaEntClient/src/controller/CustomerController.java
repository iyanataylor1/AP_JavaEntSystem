/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controller;

//import domain & view
import domain.Customer;
import view.CustomerView;

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
public class CustomerController {
    private CustomerView view;
    private Client client;
    
    //constructor
    public CustomerController(CustomerView view, Client client){
        this.view = view;
        this.client = client;
        
        // Load all on startup
        loadAllCustomers();
        
        // Add event listeners
        view.getAddButton().addActionListener(e -> addCustomer());
        view.getUpdateButton().addActionListener(e -> updateCustomer());
        view.getDeleteButton().addActionListener(e -> deleteCustomer());
        view.getSearchButton().addActionListener(e -> searchCustomer());
    }
    
    //load all method - select all
    private void loadAllCustomers(){
        try{
            client.sendAction("Select All Customers");
            List<Customer> customerList = (List<Customer>)client.receiveResponseObject();
            
            String[][] data = new String[customerList.size()][7];
            for (int i = 0; i < customerList.size(); i++){
                Customer customer = customerList.get(i);
                data[i][0] = customer.getCustomerID();
                data[i][1] = customer.getFirstName();
                data[i][2] = customer.getLastName();
                data[i][3] = customer.getCompanyName();
                data[i][4] = customer.getPhone();
                data[i][5] = customer.getEmail();
                data[i][6] = customer.getAdminID();
            }
            
            String[] columnNames = {"ID", "First Name", "Last Name", "Company Name", "Phone", "Email", "Admin ID"};
            view.getCustomerTable().setModel(new javax.swing.table.DefaultTableModel(data, columnNames));
        }
        catch(Exception ex){
            JOptionPane.showMessageDialog(view, "Error: " + ex.getMessage());
        }
    }
    
    //add method
    private void addCustomer(){
        JTextField idField = new JTextField();
        JTextField firstNameField = new JTextField();
        JTextField lastNameField = new JTextField();
        JTextField companyNameField = new JTextField();
        JTextField phoneField = new JTextField();
        JTextField emailField = new JTextField();
        JTextField adminIDField = new JTextField();
        
        Object[] message = {
            "Customer ID:", idField,
            "First Name:", firstNameField,
            "Last Name:", lastNameField,
            "Company Name:", companyNameField,
            "Phone:", phoneField,
            "Email:", emailField,
            "Admin ID:", adminIDField
        };
        
        int option = JOptionPane.showConfirmDialog(view, message, "Add Customer", JOptionPane.OK_CANCEL_OPTION);
        if (option == JOptionPane.OK_OPTION){
            try{
                String id = idField.getText().trim();
                String firstName = firstNameField.getText().trim();
                String lastName = lastNameField.getText();
                String companyName = companyNameField.getText().trim();
                String phone = phoneField.getText().trim();
                String email = emailField.getText().trim();
                String adminID = adminIDField.getText().trim();
                
                Customer newCustomer = new Customer(id, firstName, lastName, companyName, phone, email, adminID);
                client.sendAction("Insert Customer");
                client.sendObject(newCustomer);
                
                // Correctly receive the boolean response first
                boolean isAdded = client.receiveResponseBoolean();
                
                if (isAdded) {
                    JOptionPane.showMessageDialog(view, "Customer added successfully!");
                    loadAllCustomers(); // Now we send "Select All" AFTER we confirmed the response
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
    private void updateCustomer(){
        int selectedRow = view.getCustomerTable().getSelectedRow();
        if (selectedRow == -1){
            JOptionPane.showMessageDialog(view, "Please select a customer to update.");
            return;
        }
        
        String id = (String)view.getCustomerTable().getValueAt(selectedRow, 0);
        String currentFirstName = (String)view.getCustomerTable().getValueAt(selectedRow, 1);
        String currentLastName = (String)view.getCustomerTable().getValueAt(selectedRow, 2);
        String currentCompanyName = (String)view.getCustomerTable().getValueAt(selectedRow, 3);
        String currentPhone = (String)view.getCustomerTable().getValueAt(selectedRow, 4);
        String currentEmail = (String)view.getCustomerTable().getValueAt(selectedRow, 5);
        String currentAdminID = (String)view.getCustomerTable().getValueAt(selectedRow, 6);
        
        JTextField firstNameField = new JTextField(currentFirstName);
        JTextField lastNameField = new JTextField(currentLastName);
        JTextField companyNameField = new JTextField(currentCompanyName);
        JTextField phoneField = new JTextField(currentPhone);
        JTextField emailField = new JTextField(currentEmail);
        JTextField adminIDField = new JTextField(currentAdminID);
        
        Object[] message = {
            "First Name:", firstNameField,
            "Last Name:", lastNameField,
            "Company Name:", companyNameField,
            "Phone:", phoneField,
            "Email:", emailField,
            "Admin ID:", adminIDField
        };
        
        int option = JOptionPane.showConfirmDialog(view, message, "Update Customer", JOptionPane.OK_CANCEL_OPTION);
        if (option == JOptionPane.OK_OPTION){
            try{
                String newFirstName = firstNameField.getText().trim();
                String newLastName = lastNameField.getText().trim();
                String newCompanyName = companyNameField.getText().trim();
                String newPhone = phoneField.getText().trim();
                String newEmail = emailField.getText().trim();
                String newAdminID = adminIDField.getText().trim();
                
                Customer updatedCustomer = new Customer(id, newFirstName, newLastName, newCompanyName, newPhone, newEmail, newAdminID);
                client.sendAction("Update Customer");
                client.sendObject(updatedCustomer);
                
                // Correctly receive the boolean response first
                boolean isUpdated = client.receiveResponseBoolean();
                
                if (isUpdated) {
                    JOptionPane.showMessageDialog(view, "Customer updated successfully!");
                    loadAllCustomers(); // Now we send "Select All" AFTER we confirmed the response
                } else {
                    JOptionPane.showMessageDialog(view, "Update failed. Please try again.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
            catch(Exception ex){
                JOptionPane.showMessageDialog(view, "Error: " + ex.getMessage());
            }
        }
        
        view.getCustomerTable().clearSelection(); // Deselect the row whether the user clicks ok or cancel
    }
    
    //delete method
    private void deleteCustomer(){
        int selectedRow = view.getCustomerTable().getSelectedRow();
        if (selectedRow == -1){
            JOptionPane.showMessageDialog(view, "Please select a customer to delete.");
            return;
        }
        
        String id = (String)view.getCustomerTable().getValueAt(selectedRow, 0);
        int confirm = JOptionPane.showConfirmDialog(view, "Are you sure you want to delete this customer?", "Delete Customer", JOptionPane.YES_NO_OPTION);
        
        if (confirm == JOptionPane.YES_OPTION){
            try{
                client.sendAction("Delete Customer");
                client.sendObject(id);
                
                // Correctly receive the boolean response first
                boolean isDeleted = client.receiveResponseBoolean();
                
                if (isDeleted) {
                    JOptionPane.showMessageDialog(view, "Customer deleted successfully!");
                    loadAllCustomers(); // Now we send "Select All" AFTER we confirmed the response
                } else {
                    JOptionPane.showMessageDialog(view, "Deletion failed. Please try again.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
            catch(Exception ex){
                JOptionPane.showMessageDialog(view, "Error: " + ex.getMessage());
            }
        }
        
        view.getCustomerTable().clearSelection(); // Deselect the row whether the user clicks yes or no
    }
    
    //search method
    private void searchCustomer(){
        String searchId = view.getSearchField().getText();
        if (searchId.isEmpty()){
            JOptionPane.showMessageDialog(view, "Please enter a Customer ID to search.");
            return;
        }
        
        try {
            client.sendAction("Select Customer");
            client.sendObject(searchId);
            Customer customer = (Customer)client.receiveResponseObject();
            
            if(customer != null){
                JOptionPane.showMessageDialog(view, "Customer Found:\nID: " + customer.getCustomerID()+ 
                        "\nFirst Name: " + customer.getFirstName()+ 
                        "\nLast Name: " + customer.getLastName()+ 
                        "\nCompany Name: " + customer.getCompanyName()+ 
                        "\nPhone: " + customer.getPhone()+ 
                        "\nEmail: " + customer.getEmail()+
                        "\nAdmin ID: " +customer.getAdminID());
            }/* else {
                JOptionPane.showMessageDialog(view, "Customer not found.");
            }*/
        }
        catch(Exception ex){
            JOptionPane.showMessageDialog(view, "Customer not found.");
            //JOptionPane.showMessageDialog(view, "Error: " + ex.getMessage());
            System.err.println("Error while searching for price: " + ex.getMessage());
            ex.printStackTrace(); // This prints the full error details to the console
        }
    }
}
