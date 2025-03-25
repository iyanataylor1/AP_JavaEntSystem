/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controller;

import view.LoginView;
import view.ParentWindow;  // Parent window after login
import java.sql.*;
import networktools.Client;

import javax.swing.JOptionPane;

/**
 *
 * @author iyana
 */
public class LoginController {
    private LoginView loginView;
    private Client client;
    
    public LoginController(LoginView loginView, Client client){
        this.loginView = loginView;
        this.client = client;

        // Add event listener for login button
        loginView.getLoginButton().addActionListener(e -> authenticateUser());
    }
    
    private void authenticateUser(){
        String username = loginView.getUsername();
        String password = loginView.getPassword();

        if (username.isEmpty() || password.isEmpty()) {
            loginView.showMessage("Please enter both username and password.");
            return;
        }
        
        // Send action to server
        client.sendAction("LOGIN");

        // Send username and password to the server
        client.sendObject(username);
        client.sendObject(password);

        // Receive login response
        boolean isAuthenticated = client.receiveResponseBoolean();

        if (isAuthenticated) {
            JOptionPane.showMessageDialog(loginView, "Login successful!");
            loginView.dispose(); // Close the login window
            new ParentWindow(client); // Open ParentWindow
        } else {
            JOptionPane.showMessageDialog(loginView, "Invalid username or password!", "Login Failed", JOptionPane.ERROR_MESSAGE);
        }
    }
}
