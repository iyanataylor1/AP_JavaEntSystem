/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controller;

//import domain & view
import domain.Admin;
import view.AdminView;

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
public class AdminController {
    private AdminView view;
    private Client client;

    //constructor
    public AdminController(AdminView view, Client client) {
        this.view = view;
        this.client = client;

        // Load all on startup
        loadAllAdmins();

        // Add event listeners
        view.getAddButton().addActionListener(e -> addAdmin());
        view.getUpdateButton().addActionListener(e -> updateAdmin());
        view.getDeleteButton().addActionListener(e -> deleteAdmin());
        view.getSearchButton().addActionListener(e -> searchAdmin());
    }

    //load all method - select all
    private void loadAllAdmins() {
        try {
            client.sendAction("Select All Admins");
            List<Admin> adminList = (List<Admin>) client.receiveResponseObject();

            String[][] data = new String[adminList.size()][3];
            for (int i = 0; i < adminList.size(); i++) {
                Admin admin = adminList.get(i);
                data[i][0] = admin.getAdminID();
                data[i][1] = admin.getUsername();
                data[i][2] = admin.getPassword();
            }

            String[] columnNames = {"ID", "Username", "Password"};
            view.getAdminTable().setModel(new javax.swing.table.DefaultTableModel(data, columnNames));

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(view, "Error: " + ex.getMessage());
        }
    }

    //add method
    private void addAdmin() {
        JTextField idField = new JTextField();
        JTextField usernameField = new JTextField();
        JPasswordField passwordField = new JPasswordField();
        
        Object[] message = {
            "Admin ID:", idField,
            "Username:", usernameField,
            "Password:", passwordField
        };

        int option = JOptionPane.showConfirmDialog(view, message, "Add Admin", JOptionPane.OK_CANCEL_OPTION);
        if (option == JOptionPane.OK_OPTION) {
            try {
                String id = idField.getText().trim();
                String username = usernameField.getText().trim();
                String password = new String(passwordField.getPassword()).trim();

                Admin newAdmin = new Admin(id, username, password);
                client.sendAction("Insert Admin");
                client.sendObject(newAdmin);

                // Correctly receive the boolean response first
                boolean isAdded = client.receiveResponseBoolean();
                
                if (isAdded) {
                    JOptionPane.showMessageDialog(view, "Admin added successfully!");
                    loadAllAdmins(); // Now we send "Select All" AFTER we confirmed the response
                } else {
                    JOptionPane.showMessageDialog(view, "Addition failed. Please try again.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(view, "Error: " + ex.getMessage());
            }
        }
    }

    //update method
    private void updateAdmin() {
        int selectedRow = view.getAdminTable().getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(view, "Please select an admin to update.");
            return;
        }

        String id = (String) view.getAdminTable().getValueAt(selectedRow, 0);
        String currentUsername = (String) view.getAdminTable().getValueAt(selectedRow, 1);
        String currentPassword = (String) view.getAdminTable().getValueAt(selectedRow, 2);

        JTextField usernameField = new JTextField(currentUsername);
        JPasswordField passwordField = new JPasswordField(currentPassword);
        
        Object[] message = {
            "Username:", usernameField,
            "Password:", passwordField
        };

        int option = JOptionPane.showConfirmDialog(view, message, "Update Admin", JOptionPane.OK_CANCEL_OPTION);
        if (option == JOptionPane.OK_OPTION) {
            try {
                String newUsername = usernameField.getText().trim();
                String newPassword = new String(passwordField.getPassword()).trim();

                Admin updatedAdmin = new Admin(id, newUsername, newPassword);
                client.sendAction("Update Admin");
                client.sendObject(updatedAdmin);

                // Correctly receive the boolean response first
                boolean isUpdated = client.receiveResponseBoolean();
                
                if (isUpdated) {
                    JOptionPane.showMessageDialog(view, "Admin updated successfully!");
                    loadAllAdmins(); // Now we send "Select All" AFTER we confirmed the response
                } else {
                    JOptionPane.showMessageDialog(view, "Update failed. Please try again.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(view, "Error: " + ex.getMessage());
            }
        }
        
        view.getAdminTable().clearSelection(); // Deselect the row whether the user clicks ok or cancel
    }

    //delete method
    private void deleteAdmin() {
        int selectedRow = view.getAdminTable().getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(view, "Please select an admin to delete.");
            return;
        }

        String id = (String) view.getAdminTable().getValueAt(selectedRow, 0);
        int confirm = JOptionPane.showConfirmDialog(view, "Are you sure you want to delete this admin?", "Delete Admin", JOptionPane.YES_NO_OPTION);
        
        if (confirm == JOptionPane.YES_OPTION) {
            try {
                client.sendAction("Delete Admin");
                client.sendObject(id);

                // Correctly receive the boolean response first
                boolean isDeleted = client.receiveResponseBoolean();
                
                if (isDeleted) {
                    JOptionPane.showMessageDialog(view, "Admin deleted successfully!");
                    loadAllAdmins(); // Now we send "Select All" AFTER we confirmed the response
                } else {
                    JOptionPane.showMessageDialog(view, "Deletion failed. Please try again.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(view, "Error: " + ex.getMessage());
            }
        }
        
        view.getAdminTable().clearSelection(); // Deselect the row whether the user clicks yes or no
    }

    //search method
    private void searchAdmin() {
        String searchId = view.getSearchField().getText();
        if (searchId.isEmpty()) {
            JOptionPane.showMessageDialog(view, "Please enter an Admin ID to search.");
            return;
        }

        try {
            client.sendAction("Select Admin");
            client.sendObject(searchId);
            Admin admin = (Admin) client.receiveResponseObject();

            if (admin != null) {
                JOptionPane.showMessageDialog(view, "Admin Found:\nID: " + admin.getAdminID()+ 
                        "\nUsername: " + admin.getUsername()+
                        "\nPassword: " +admin.getPassword());
            } /*else {
                JOptionPane.showMessageDialog(view, "Admin not found.");
            }*/
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(view, "Admin not found.");
            //JOptionPane.showMessageDialog(view, "Error: " + ex.getMessage());
            System.err.println("Error while searching for price: " + ex.getMessage());
            ex.printStackTrace(); // This prints the full error details to the console
        }
    }
}
