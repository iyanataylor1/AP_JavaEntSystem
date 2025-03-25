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

/**
 *
 * @author iyana
 */
public class EventView extends JInternalFrame{
    //attributes
    JTable eventTable;
    JButton searchButton, addButton, updateButton, deleteButton, genInvoiceButton;
    JTextField searchField;

    //constructor
    public EventView(){
        //window properties
        setTitle("Event Management");
        setSize(600, 400);
        setResizable(false);
        setMaximizable(true);
        setIconifiable(true);
        setVisible(true);
        setClosable(true);
        setDefaultCloseOperation(JInternalFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());
        
        // Create table
        eventTable = new JTable();
        JScrollPane tableScrollPane = new JScrollPane(eventTable);

        // Create buttons
        addButton = new JButton("Add");
        updateButton = new JButton("Update");
        genInvoiceButton = new JButton("Generate Invoice");
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
        buttonPanel.add(genInvoiceButton);
        buttonPanel.add(deleteButton);
        
        // Add components to frame
        add(searchPanel, BorderLayout.NORTH);
        add(tableScrollPane, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);
    }
    
    //get methods - table, add, update, delete, search, search field, logout
    public JTable getEventTable() {
        return eventTable;
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
    
    public JButton getGenInvoiceButton(){
        return genInvoiceButton;
    }
}
