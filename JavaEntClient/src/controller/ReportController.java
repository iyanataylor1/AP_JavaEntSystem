/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controller;

//import domain & view
import domain.Report;
import view.ReportView;

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
import java.util.ArrayList;

// Import for formatting
import java.time.format.DateTimeFormatter; 
import java.time.LocalDate;
import java.time.format.DateTimeParseException;

//imports for pdf generation & download
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfWriter;
import java.io.File;
import java.io.FileOutputStream;

import com.toedter.calendar.JDateChooser;
import java.util.Date;
import java.time.ZoneId;
import java.util.Iterator;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;

/**
 *
 * @author iyana
 */
public class ReportController {
    private ReportView view;
    private Client client;
    
    //constructor
    //constructor
    public ReportController(ReportView view, Client client){
        this.view = view;
        this.client = client;
        
        // Load all on startup
        loadAllReports();
        
        // Add event listeners
        view.getAddButton().addActionListener(e -> addReport());
        view.getUpdateButton().addActionListener(e -> updateReport());
        view.getDeleteButton().addActionListener(e -> deleteReport());
        view.getSearchButton().addActionListener(e -> searchReport());
        //view.getDownloadButton().addActionListener(e -> onDownloadButtonClick());
        
        //1111111111111111
        view.getDownloadButton().addActionListener(e -> downloadReport());
        view.getFilterButton().addActionListener(e -> {
            try {
                // Get selected dates
                Date startDate = view.getStartDatePicker().getDate();
                Date endDate = view.getEndDatePicker().getDate();

                if (startDate == null || endDate == null) {
                    JOptionPane.showMessageDialog(view, "Please select both start and end dates.", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                LocalDate startLocalDate = startDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
                LocalDate endLocalDate = endDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();

                if (startLocalDate.isAfter(endLocalDate)) {
                    JOptionPane.showMessageDialog(view, "Start date cannot be after end date.", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                DefaultTableModel model = (DefaultTableModel) view.getReportTable().getModel();
                TableRowSorter<DefaultTableModel> sorter = new TableRowSorter<>(model);
                view.getReportTable().setRowSorter(sorter);

                sorter.setRowFilter(new RowFilter<DefaultTableModel, Integer>() {
                    @Override
                    public boolean include(Entry<? extends DefaultTableModel, ? extends Integer> entry) {
                        String dateString = entry.getStringValue(2); // Assuming "Date Generated" is column index 2
                        LocalDate reportDate = LocalDate.parse(dateString);
                        return (reportDate.isEqual(startLocalDate) || reportDate.isEqual(endLocalDate) || 
                                (reportDate.isAfter(startLocalDate) && reportDate.isBefore(endLocalDate)));
                    }
                });
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(view, "Error filtering reports: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        });
        //22222222222222222222222222
    }
    
    //load all method - select all
    private void loadAllReports(){
        try{
            client.sendAction("Select All Reports");
            List<Report> reportList = (List<Report>)client.receiveResponseObject();
            
            String[][] data = new String[reportList.size()][4];
            
            // Define the date format
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd"); 
        
            for (int i = 0; i < reportList.size(); i++){
                Report report = reportList.get(i);
                data[i][0] = report.getReportID();
                data[i][1] = report.getReportType();
                //data[i][2] = report.getDateGenerated();
                data[i][2] = report.getDateGenerated().format(formatter); // Convert LocalDate to String
                data[i][3] = report.getAdminID();
            }
            
            String[] columnNames = {"ID", "Report Type", "Date Generated", "Admin ID"};
            view.getReportTable().setModel(new javax.swing.table.DefaultTableModel(data, columnNames));
        }
        catch(Exception ex){
            JOptionPane.showMessageDialog(view, "Error: " + ex.getMessage());
        }
    }
    
    //add method
    private void addReport(){
        JTextField idField = new JTextField();
        JTextField reportTypeField = new JTextField();
        JTextField dateGeneratedField = new JTextField();
        JTextField adminIDField = new JTextField();
        
        Object[] message = {
            "Report ID:", idField,
            "Report Type:", reportTypeField,
            "Date Generated (yyyy-MM-dd):", dateGeneratedField,
            "Admin ID:", adminIDField
        };
        
        int option = JOptionPane.showConfirmDialog(view, message, "Add Report", JOptionPane.OK_CANCEL_OPTION);
        if (option == JOptionPane.OK_OPTION){
            try{
                // Define the date format
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

                String id = idField.getText().trim();
                String reportType = reportTypeField.getText().trim();
                //String dateGenerated = dateGeneratedField.getText().trim();
                //Convert string to local date
                LocalDate dateGenerated = LocalDate.parse(dateGeneratedField.getText().trim(), formatter);
                String adminID = adminIDField.getText().trim();
                
                Report newReport = new Report(id, reportType, dateGenerated, adminID);
                client.sendAction("Insert Report");
                client.sendObject(newReport);
                
                // Correctly receive the boolean response first
                boolean isAdded = client.receiveResponseBoolean();
                
                if (isAdded) {
                    JOptionPane.showMessageDialog(view, "Report added successfully!");
                    loadAllReports(); // Now we send "Select All" AFTER we confirmed the response
                } else {
                    JOptionPane.showMessageDialog(view, "Addition failed. Please try again.", "Error", JOptionPane.ERROR_MESSAGE);
                }  
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
    private void updateReport(){
        int selectedRow = view.getReportTable().getSelectedRow();
        if (selectedRow == -1){
            JOptionPane.showMessageDialog(view, "Please select a report to update.");
            return;
        }
        
        String id = (String)view.getReportTable().getValueAt(selectedRow, 0);
        String currentReportType = (String)view.getReportTable().getValueAt(selectedRow, 1);
        String currentDateGenerated = (String)view.getReportTable().getValueAt(selectedRow, 2);
        String currentAdminID = (String)view.getReportTable().getValueAt(selectedRow, 3);
        
        JTextField reportTypeField = new JTextField(currentReportType);
        JTextField dateGeneratedField = new JTextField(currentDateGenerated);
        JTextField adminIDField = new JTextField(currentAdminID);
        
        Object[] message = {
            "Report Type:", reportTypeField,
            "Date Generated (yyyy-MM-dd):", dateGeneratedField,
            "Admin ID:", adminIDField
        };
        
        int option = JOptionPane.showConfirmDialog(view, message, "Update Report", JOptionPane.OK_CANCEL_OPTION);
        if (option == JOptionPane.OK_OPTION){
            try{
                // Define the date format
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

                String newReportType = reportTypeField.getText().trim();
                //String newDateGenerated = dateGeneratedField.getText().trim();
                //Convert string to local date
                LocalDate newDateGenerated = LocalDate.parse(dateGeneratedField.getText().trim(), formatter);
                String newAdminID = adminIDField.getText().trim();
                
                Report updatedReport = new Report(id, newReportType, newDateGenerated, newAdminID);
                client.sendAction("Update Report");
                client.sendObject(updatedReport);
                
                // Correctly receive the boolean response first
                boolean isUpdated = client.receiveResponseBoolean();
                
                if (isUpdated) {
                    JOptionPane.showMessageDialog(view, "Report updated successfully!");
                    loadAllReports(); // Now we send "Select All" AFTER we confirmed the response
                } else {
                    JOptionPane.showMessageDialog(view, "Update failed. Please try again.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
            catch(DateTimeParseException e){
                JOptionPane.showMessageDialog(view, "Invalid date format. Please enter in yyyy-MM-dd format.", "Error", JOptionPane.ERROR_MESSAGE);
            }
            catch(Exception ex){
                JOptionPane.showMessageDialog(view, "Error: " + ex.getMessage());
            }
        }
        view.getReportTable().clearSelection(); // Deselect the row whether the user clicks ok or cancel
    }
    
    //delete method
    private void deleteReport(){
        int selectedRow = view.getReportTable().getSelectedRow();
        if (selectedRow == -1){
            JOptionPane.showMessageDialog(view, "Please select a report to delete.");
            return;
        }
        
        String id = (String)view.getReportTable().getValueAt(selectedRow, 0);
        int confirm = JOptionPane.showConfirmDialog(view, "Are you sure you want to delete this report?", "Delete Report", JOptionPane.YES_NO_OPTION);
        
        if (confirm == JOptionPane.YES_OPTION){
            try{
                client.sendAction("Delete Report");
                client.sendObject(id);
                
                // Correctly receive the boolean response first
                boolean isDeleted = client.receiveResponseBoolean();
                
                if (isDeleted) {
                    JOptionPane.showMessageDialog(view, "Report deleted successfully!");
                    loadAllReports(); // Now we send "Select All" AFTER we confirmed the response
                } else {
                    JOptionPane.showMessageDialog(view, "Deletion failed. Please try again.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
            catch(Exception ex){
                JOptionPane.showMessageDialog(view, "Error: " + ex.getMessage());
            }
        }
        view.getReportTable().clearSelection(); // Deselect the row whether the user clicks yes or no
    }
    
    //search method
    private void searchReport(){
        String searchId = view.getSearchField().getText();
        if (searchId.isEmpty()){
            JOptionPane.showMessageDialog(view, "Please enter a Report ID to search.");
            return;
        }
        
        try{
            client.sendAction("Select Report");
            client.sendObject(searchId);
            Report report = (Report)client.receiveResponseObject();
            
            if(report != null){
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                
                JOptionPane.showMessageDialog(view, "Report Found:\nID: " + report.getReportID()+ 
                        "\nReport Type: " + report.getReportType()+ 
                        "\nDate Generated: " + report.getDateGenerated().format(formatter)+ 
                        "\nAdmin ID: " + report.getAdminID());
            } /*else {
                JOptionPane.showMessageDialog(view, "Report not found.");
            }*/

        }
        catch(Exception ex){
            JOptionPane.showMessageDialog(view, "Report not found.");
            //JOptionPane.showMessageDialog(view, "Error: " + ex.getMessage());
            System.err.println("Error while searching for price: " + ex.getMessage());
            ex.printStackTrace(); // This prints the full error details to the console
        }
    }
    /*
    // Download Report Method
    public void downloadReport() {
        int selectedRow = view.getReportTable().getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(null, "Please select a report to download.");
            return;
        }

        // get the data from the selected row in the table
        String reportID = view.getReportTable().getValueAt(selectedRow, 0).toString(); 
        String reportType = view.getReportTable().getValueAt(selectedRow, 1).toString();  
        String dateGenerated = view.getReportTable().getValueAt(selectedRow, 2).toString();  
        String adminID = view.getReportTable().getValueAt(selectedRow, 3).toString();  

        // Create the file name for the report PDF
        String fileName = "report_" + reportID + ".pdf";

        try {
            // Create a document object
            Document document = new Document();
            PdfWriter.getInstance(document, new FileOutputStream(fileName));

            // Open the document
            document.open();

            // Add content to the document (all the data fetched)
            document.add(new Paragraph("Java Entertainment Scheduling System"));
            document.add(new Paragraph("Report"));
            document.add(new Paragraph("\n--------------------------------"));
            document.add(new Paragraph("\nReport ID: " + reportID));
            document.add(new Paragraph("Report Type: " + reportType));
            document.add(new Paragraph("Date Generated: " + dateGenerated));
            document.add(new Paragraph("Generated by (Admin ID): " + adminID));
            document.add(new Paragraph("\n--------------------------------"));
            document.add(new Paragraph("\nDownloaded on: " + LocalDate.now().toString()));
            document.add(new Paragraph("Downloaded using iTextPDF 5.5.13"));

            // Close the document
            document.close();

            // Simulate the report being downloaded
            JOptionPane.showMessageDialog(null, "The report is saved as: " + new File(fileName).getAbsolutePath());
            
            // Clear the selection after downloading
            view.getReportTable().clearSelection();

        } 
        catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error generating the report: " + e.getMessage());
        }
    }
    
    // Event handler for download button
    private void onDownloadButtonClick() {
        int selectedRow = view.getReportTable().getSelectedRow();

        if (selectedRow == -1) {
            // No report selected
            JOptionPane.showMessageDialog(null, "Please select a report to download.");
        } else {
            // Get the selected report ID (assuming the report ID is in the first column)
            String reportID = (String) view.getReportTable().getValueAt(selectedRow, 0);
            downloadReport();
            
            // Clear the selection after downloading
            view.getReportTable().clearSelection();
            
            // Display success message
            JOptionPane.showMessageDialog(null, "Report " + reportID + " downloaded successfully.");
        }
    }*/
    
    //111111111111111111111111111111111
    public void downloadReport() {
        int selectedRow = view.getReportTable().getSelectedRow();
        DefaultTableModel model = (DefaultTableModel) view.getReportTable().getModel();

        List<String> reportsToDownload = new ArrayList<>();

        if (selectedRow != -1) {
            // Single report download
            String reportID = model.getValueAt(selectedRow, 0).toString();
            reportsToDownload.add(reportID);
        } else {
            // Download all filtered reports
            for (int i = 0; i < view.getReportTable().getRowCount(); i++) {
                reportsToDownload.add(model.getValueAt(i, 0).toString());
            }
        }

        if (reportsToDownload.isEmpty()) {
            JOptionPane.showMessageDialog(view, "No reports available for download.", "Info", JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        try {
            // Create a PDF file
            String fileName = reportsToDownload.size() == 1 ? "report_" + reportsToDownload.get(0) + ".pdf" : "all_reports.pdf";
            Document document = new Document();
            PdfWriter.getInstance(document, new FileOutputStream(fileName));

            document.open();
            document.add(new Paragraph("Java Entertainment Scheduling System"));
            document.add(new Paragraph("Generated Reports"));
            
            // Get data inside the loop
            for (String reportID : reportsToDownload) {
                int rowIndex = -1;
                for (int i = 0; i < model.getRowCount(); i++) {
                    if (model.getValueAt(i, 0).toString().equals(reportID)) {
                        rowIndex = i;
                        break;
                    }
                }

                if (rowIndex != -1) {
                    String reportType = model.getValueAt(rowIndex, 1).toString();
                    String dateGenerated = model.getValueAt(rowIndex, 2).toString();
                    String adminID = model.getValueAt(rowIndex, 3).toString();

                    document.add(new Paragraph("\n--------------------------------"));
                    document.add(new Paragraph("\nReport ID: " + reportID));
                    document.add(new Paragraph("Report Type: " + reportType));
                    document.add(new Paragraph("Date Generated: " + dateGenerated));
                    document.add(new Paragraph("Generated by (Admin ID): " + adminID));
                }
            }

            document.add(new Paragraph("\n--------------------------------"));
            document.add(new Paragraph("\nDownloaded on: " + LocalDate.now()));
            document.close();

            JOptionPane.showMessageDialog(view, "Report(s) saved as: " + new File(fileName).getAbsolutePath());
            
            // Clear the date pickers after filtering & downloading
            view.getStartDatePicker().setCalendar(null);
            view.getEndDatePicker().setCalendar(null);
                
            // Clear the selection after downloading
            view.getReportTable().clearSelection();
            
            loadAllReports(); // Now we send "Select All" AFTER 
        } catch (Exception e) {
            JOptionPane.showMessageDialog(view, "Error generating the report: " + e.getMessage());
        }
    }
    //22222222222222222222222222222222
}
