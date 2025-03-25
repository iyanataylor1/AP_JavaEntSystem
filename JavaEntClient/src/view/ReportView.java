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

import domain.Report;
import java.time.format.DateTimeFormatter;

import com.toedter.calendar.JDateChooser;

/**
 *
 * @author iyana
 */
public class ReportView extends JInternalFrame{
    //attributes
    JTable reportTable;
    JButton searchButton, addButton, updateButton, deleteButton, downloadButton; 
    JTextField searchField;
    
    //1111111111111111
    private JDateChooser startDatePicker;
    private JDateChooser endDatePicker;
    private JButton filterButton;
    //2222222222222222
    
    //constructor
    public ReportView(){
    	//window properties
        setTitle("Report Management");
        setSize(600, 400);
        setResizable(false);
        setMaximizable(true);
        setIconifiable(true);
        setVisible(true);
        setClosable(true);
        setDefaultCloseOperation(JInternalFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());
	
	// Create table
        reportTable = new JTable();
        JScrollPane tableScrollPane = new JScrollPane(reportTable);

	// Create buttons
        addButton = new JButton("Add");
        updateButton = new JButton("Update");
        filterButton = new JButton("Filter Reports");
        downloadButton = new JButton("Download Reports");
        deleteButton = new JButton("Delete");

	// Create Search Bar
        searchField = new JTextField(10);
        searchButton = new JButton("Search");
        
        // create date filters
        startDatePicker = new JDateChooser();
        endDatePicker = new JDateChooser();

	// Panel for search bar
        JPanel searchPanel = new JPanel();
        searchPanel.add(new JLabel("Search ID:"));
        searchPanel.add(searchField);
        searchPanel.add(searchButton);
        searchPanel.add(new JLabel("Start Date:"));
        searchPanel.add(startDatePicker);
        searchPanel.add(new JLabel("End Date:"));
        searchPanel.add(endDatePicker);

	// Panel for buttons
        JPanel buttonPanel = new JPanel();
        buttonPanel.add(addButton);
        buttonPanel.add(updateButton);
        buttonPanel.add(filterButton);
        buttonPanel.add(downloadButton);
        buttonPanel.add(deleteButton);

	// Add components to frame
        add(searchPanel, BorderLayout.NORTH);
        add(tableScrollPane, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);
    }

    //get methods - table, add, update, delete, search, search field
    public JTable getReportTable() {
        return reportTable;
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
    
    // Getter methods for Report data
    public String getReportID(Report report){
        return report.getReportID();
    }
    
    public String getReportType(Report report) {
        return report.getReportType();  
    }

    public String getDateGenerated(Report report) {
        // Define the date format
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd"); 
            
        return report.getDateGenerated().format(formatter); // Convert LocalDate to String
    }

    public String getAdminID(Report report) {
        return report.getAdminID();  
    }

    public JDateChooser getStartDatePicker() {
        return startDatePicker;
    }

    public JDateChooser getEndDatePicker() {
        return endDatePicker;
    }

    public JButton getFilterButton() {
        return filterButton;
    }
}
