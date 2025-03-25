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
public class AdminView extends JInternalFrame{
    //attributes
    JTable adminTable;
    JButton searchButton, addButton, updateButton, deleteButton;
    JTextField searchField;

    //constructor
    public AdminView() {
        //window properties
        setTitle("Admin Management");
        setSize(600, 400);
        setResizable(false);
        setMaximizable(true);
        setIconifiable(true);
        setVisible(true);
        setClosable(true);
        setDefaultCloseOperation(JInternalFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());

        // Create table
        adminTable = new JTable();
        JScrollPane tableScrollPane = new JScrollPane(adminTable);

        // Create buttons
        addButton = new JButton("Add");
        updateButton = new JButton("Update");
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
        buttonPanel.add(deleteButton);

        // Add components to frame
        add(searchPanel, BorderLayout.NORTH);
        add(tableScrollPane, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);
    }

    //get methods - table, add, update, delete, search, search field
    public JTable getAdminTable() {
        return adminTable;
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
}
