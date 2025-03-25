/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package view;

//import lightweights
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

//import heavyweights
import java.awt.*;
import java.awt.event.ActionListener;

import domain.Invoice;
import java.time.format.DateTimeFormatter;


/**
 *
 * @author iyana
 */
public class InvoiceView extends JInternalFrame{
    //attributes
    JTable invoiceTable;
    JButton searchButton, addButton, updateButton, deleteButton, downloadButton;
    JTextField searchField;

    //constructor
    public InvoiceView(){
        //window properties
	setTitle("Invoice Management");
        setSize(600, 400);
        setResizable(false);
        setMaximizable(true);
        setIconifiable(true);
        setVisible(true);
        setClosable(true);
        setDefaultCloseOperation(JInternalFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());
        
	// Create table
        invoiceTable = new JTable();
        JScrollPane tableScrollPane = new JScrollPane(invoiceTable);
        
	// Create buttons
        addButton = new JButton("Add");
        updateButton = new JButton("Update");
        downloadButton = new JButton("Download Invoice");
        deleteButton = new JButton("Delete");
        
	// Create Search Bar
        searchField = new JTextField(10);
        searchButton = new JButton("Search");
        
	// Panel for search bar
        JPanel searchPanel = new JPanel();
        searchPanel.add(new JLabel("Search ID:"));
        searchPanel.add(searchField);
        searchPanel.add(searchButton);
        
	// Panel for buttons
        JPanel buttonPanel = new JPanel();
        buttonPanel.add(addButton);
        buttonPanel.add(updateButton);
        buttonPanel.add(downloadButton);
        buttonPanel.add(deleteButton);
        
	// Add components to frame
        add(searchPanel, BorderLayout.NORTH);
        add(tableScrollPane, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);
    }

    //get methods - table, add, update, delete, search, search field
    public JTable getInvoiceTable() {
        return invoiceTable;
    }

    public JButton getAddButton() {
        return addButton;
    }

    public JButton getUpdateButton() {
        return updateButton;
    }

    public JButton getDeleteButton() {
        return deleteButton;
    }

    public JButton getSearchButton() {
        return searchButton;
    }

    public JTextField getSearchField() {
        return searchField;
    }
    
    public JButton getDownloadButton() {
        return downloadButton;
    }
    
    // Getter methods for Invoice data
    public String getInvoiceID(Invoice invoice){
        return invoice.getInvoiceID();
    }
    
    public String getEventID(Invoice invoice){
        return invoice.getEventID();
    }
    
    public String getCustomerID(Invoice invoice){
        return invoice.getCustomerID();
    }
    
    public String getTotalAmount(Invoice invoice){
        return String.valueOf(invoice.getTotalAmount()); // Convert double to String 
    }
    
    public String getIssueDate(Invoice invoice) {
        // Define the date format
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd"); 
            
        return invoice.getIssueDate().format(formatter); // Convert LocalDate to String
    }
    
    public String getInvoiceStatus(Invoice invoice){
        return invoice.getInvoiceStatus();
    }
}
