/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controller;

//import domain & view
import domain.Invoice;
import view.InvoiceView;

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

// Import for formatting
import java.time.format.DateTimeFormatter; 
import java.time.LocalDate;
import java.time.format.DateTimeParseException;

//imports for pdf generation & download
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfWriter;
import java.io.File;
import java.io.FileOutputStream;

/**
 *
 * @author iyana
 */
public class InvoiceController {
    private InvoiceView view;
    private Client client;
    
    //constructor
    public InvoiceController(InvoiceView view, Client client){
        this.view = view;
        this.client = client;
        
        // Load all on startup
        loadAllInvoices();
        
        // Add event listeners
        view.getAddButton().addActionListener(e -> addInvoice());
        view.getUpdateButton().addActionListener(e -> updateInvoice());
        view.getDeleteButton().addActionListener(e -> deleteInvoice());
        view.getSearchButton().addActionListener(e -> searchInvoice());
        view.getDownloadButton().addActionListener(e -> onDownloadButtonClick());
    }
    
    //load all method - select all
    private void loadAllInvoices(){
        try{
           client.sendAction("Select All Invoices");
            List<Invoice> invoiceList = (List<Invoice>)client.receiveResponseObject();
            
            String[][] data = new String[invoiceList.size()][6];
            
            // Define the date format
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            
            for (int i = 0; i < invoiceList.size(); i++){
                Invoice invoice = invoiceList.get(i);
                data[i][0] = invoice.getInvoiceID();
                data[i][1] = invoice.getEventID();
                data[i][2] = invoice.getCustomerID();
                //data[i][3] = invoice.getTotalAmount();
                data[i][3] = String.valueOf(invoice.getTotalAmount()); // Convert double to String        
                //data[i][4] = invoice.getIssueDate();
                data[i][4] = invoice.getIssueDate().format(formatter); //LocalDate to string
                data[i][5] = invoice.getInvoiceStatus();
            }
        
            String[] columnNames = {"ID", "Event ID", "Customer ID", "Total Amount ($)", "Issue Date", "Invoice Status"};
            view.getInvoiceTable().setModel(new javax.swing.table.DefaultTableModel(data, columnNames));
        }
        catch(Exception ex){
            JOptionPane.showMessageDialog(view, "Error: " + ex.getMessage());
        }
    }
    
    //add method
    private void addInvoice(){
        JTextField idField = new JTextField();
        JTextField eventIDField = new JTextField();
        JTextField customerIDField = new JTextField();
        JTextField totalAmountField = new JTextField();
        JTextField issueDateField = new JTextField();
        JTextField invoiceStatusField = new JTextField();
        
        Object[] message = {
            "Invoice ID:", idField,
            "Event ID:", eventIDField,
            "Customer ID:", customerIDField,
            "Total Amount ($):", totalAmountField,
            "Issue Date (yyyy-MM-dd):", issueDateField,
            "Invoice Status:", invoiceStatusField
        };
        
        int option = JOptionPane.showConfirmDialog(view, message, "Add Invoice", JOptionPane.OK_CANCEL_OPTION);
        if (option == JOptionPane.OK_OPTION){
            try{
                // Define the date format
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

                String id = idField.getText().trim();
                String eventID = eventIDField.getText().trim();
                String customerID = customerIDField.getText().trim();
                //String totalAmount = totalAmountField.getText().trim();
                double totalAmount = Double.parseDouble(totalAmountField.getText().trim()); // Convert String to double
                //String issueDate = issueDateField.getText().trim();
                LocalDate issueDate = LocalDate.parse(issueDateField.getText().trim(), formatter); // Convert String to LocalDate
                String invoiceStatus = invoiceStatusField.getText().trim();
                
                Invoice newInvoice = new Invoice(id, eventID, customerID, totalAmount, issueDate, invoiceStatus);
                client.sendAction("Insert Invoice");
                client.sendObject(newInvoice);
                
                // Correctly receive the boolean response first
                boolean isAdded = client.receiveResponseBoolean();
                
                if (isAdded) {
                    JOptionPane.showMessageDialog(view, "Invoice added successfully!");
                    loadAllInvoices(); // Now we send "Select All" AFTER we confirmed the response
                } else {
                    JOptionPane.showMessageDialog(view, "Addition failed. Please try again.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
            catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(view, "Invalid amount. Please enter a numeric value.", "Error", JOptionPane.ERROR_MESSAGE);
                return; // Exit the method if parsing fails
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
    private void updateInvoice(){
        int selectedRow = view.getInvoiceTable().getSelectedRow();
        if (selectedRow == -1){
            JOptionPane.showMessageDialog(view, "Please select an invoice to update.");
            return;
        }
        
        String id = (String)view.getInvoiceTable().getValueAt(selectedRow, 0);
        String currentEventID = (String)view.getInvoiceTable().getValueAt(selectedRow, 1);
        String currentCustomerID = (String)view.getInvoiceTable().getValueAt(selectedRow, 2);
        String currentTotalAmount = (String)view.getInvoiceTable().getValueAt(selectedRow, 3);
        String currentIssueDate = (String)view.getInvoiceTable().getValueAt(selectedRow, 4);
        String currentInvoiceStatus = (String)view.getInvoiceTable().getValueAt(selectedRow, 5);
        
        JTextField eventIDField = new JTextField(currentEventID);
        JTextField customerIDField = new JTextField(currentCustomerID);
        JTextField totalAmountField = new JTextField(currentTotalAmount);
        JTextField issueDateField = new JTextField(currentIssueDate);
        JTextField invoiceStatusField = new JTextField(currentInvoiceStatus);
    
        Object[] message = {
            "Event ID:", eventIDField,
            "Customer ID:", customerIDField,
            "Total Amount ($):", totalAmountField,
            "Issue Date (yyyy-MM-dd):", issueDateField,
            "Invoice Status:", invoiceStatusField
        };
        
        int option = JOptionPane.showConfirmDialog(view, message, "Update Invoice", JOptionPane.OK_CANCEL_OPTION);
        if (option == JOptionPane.OK_OPTION){
            try{
                // Define the date format
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

                String newEventID = eventIDField.getText().trim();
                String newCustomerID = customerIDField.getText().trim();
                //String newTotalAmount = totalAmountField.getText().trim();
                double newTotalAmount = Double.parseDouble(totalAmountField.getText().trim()); // Convert String to double
                //String newIssueDate = issueDateField.getText().trim();
                LocalDate newIssueDate = LocalDate.parse(issueDateField.getText().trim(), formatter); // Convert String to LocalDate
                String newInvoiceStatus = invoiceStatusField.getText().trim();
                
                Invoice updatedInvoice = new Invoice(id, newEventID, newCustomerID, newTotalAmount, newIssueDate, newInvoiceStatus);
                client.sendAction("Update Invoice");
                client.sendObject(updatedInvoice);
                
                // Correctly receive the boolean response first
                boolean isUpdated = client.receiveResponseBoolean();
                
                if (isUpdated) {
                    JOptionPane.showMessageDialog(view, "Invoice updated successfully!");
                    loadAllInvoices(); // Now we send "Select All" AFTER we confirmed the response
                } else {
                    JOptionPane.showMessageDialog(view, "Update failed. Please try again.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
            catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(view, "Invalid amount. Please enter a numeric value.", "Error", JOptionPane.ERROR_MESSAGE);
                return; // Exit the method if parsing fails
            }
            catch(DateTimeParseException e){
                JOptionPane.showMessageDialog(view, "Invalid date format. Please enter in yyyy-MM-dd format.", "Error", JOptionPane.ERROR_MESSAGE);
            }
            catch(Exception ex){
                JOptionPane.showMessageDialog(view, "Error: " + ex.getMessage());
            }
        }
        view.getInvoiceTable().clearSelection(); // Deselect the row whether the user clicks ok or cancel
    }
    
    //delete method
    private void deleteInvoice(){
        int selectedRow = view.getInvoiceTable().getSelectedRow();
        if (selectedRow == -1){
            JOptionPane.showMessageDialog(view, "Please select an invoice to delete.");
            return;
        }
        
        String id = (String)view.getInvoiceTable().getValueAt(selectedRow, 0);
        int confirm = JOptionPane.showConfirmDialog(view, "Are you sure you want to delete this invoice?", "Delete Invoice", JOptionPane.YES_NO_OPTION);
        
        if (confirm == JOptionPane.YES_OPTION){
            try{
                client.sendAction("Delete Invoice");
                client.sendObject(id);
                
                // Correctly receive the boolean response first
                boolean isDeleted = client.receiveResponseBoolean();
                
                if (isDeleted) {
                    JOptionPane.showMessageDialog(view, "Invoice deleted successfully!");
                    loadAllInvoices(); // Now we send "Select All" AFTER we confirmed the response
                } else {
                    JOptionPane.showMessageDialog(view, "Deletion failed. Please try again.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
            catch(Exception ex){
                JOptionPane.showMessageDialog(view, "Error: " + ex.getMessage());
            }
        }
        view.getInvoiceTable().clearSelection(); // Deselect the row whether the user clicks yes or no
    }
    
    //search method
    private void searchInvoice(){
        String searchId = view.getSearchField().getText();
        if (searchId.isEmpty()){
            JOptionPane.showMessageDialog(view, "Please enter an Invoice ID to search.");
            return;
        }
        
        try{
            client.sendAction("Select Invoice");
            client.sendObject(searchId);
            Invoice invoice = (Invoice)client.receiveResponseObject();
            
            if(invoice != null){
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                
                JOptionPane.showMessageDialog(view, "Invoice Found:\nID: " + invoice.getInvoiceID()+ 
                        "\nEvent ID: " + invoice.getEventID()+ 
                        "\nCustomer ID: " + invoice.getCustomerID()+ 
                        "\nTotal Amount ($): " + invoice.getTotalAmount()+ 
                        "\nIssue Date: " + invoice.getIssueDate().format(formatter)+ 
                        "\nInvoice Status: " + invoice.getInvoiceStatus());
            } /*else {
                JOptionPane.showMessageDialog(view, "Invoice not found.");
            }*/
        }
        catch(Exception ex){
            JOptionPane.showMessageDialog(view, "Invoice not found.");
            //JOptionPane.showMessageDialog(view, "Error: " + ex.getMessage());
            System.err.println("Error while searching for price: " + ex.getMessage());
            ex.printStackTrace(); // This prints the full error details to the console
        }
    }
    
    //Download invoice method
    public void downloadInvoice(){
        int selectedRow = view.getInvoiceTable().getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(null, "Please select a report to download.");
            return;
        }
        
        // get the data from the selected row in the table (id, event id, cust id,total am, issue date, status
        String invoiceID = view.getInvoiceTable().getValueAt(selectedRow, 0).toString();
        String eventID = view.getInvoiceTable().getValueAt(selectedRow, 1).toString();
        String customerID = view.getInvoiceTable().getValueAt(selectedRow, 2).toString();
        String totalAmount = view.getInvoiceTable().getValueAt(selectedRow, 3).toString();
        String issueDate = view.getInvoiceTable().getValueAt(selectedRow, 4).toString();
        String status = view.getInvoiceTable().getValueAt(selectedRow, 5).toString();
        
        // Create the file name for the report PDF
        String fileName = "invoice_" + invoiceID + ".pdf";
        
        try{
            // Create a document object
            Document document = new Document();
            PdfWriter.getInstance(document, new FileOutputStream(fileName));
            
            // Open the document
            document.open();
            
            // Add content to the document (all the data fetched)
            document.add(new Paragraph("Java Entertainment Scheduling System"));
            document.add(new Paragraph("Invoice"));
            document.add(new Paragraph("\n--------------------------------"));
            document.add(new Paragraph("\nInvoice ID: " + invoiceID));
            document.add(new Paragraph("Event ID: " + eventID));
            document.add(new Paragraph("Customer ID: " + customerID));
            document.add(new Paragraph("Total Amount: " + totalAmount));
            document.add(new Paragraph("Issue Date: " + issueDate));
            document.add(new Paragraph("Invoice Status: " + status));
            document.add(new Paragraph("\n--------------------------------"));
            document.add(new Paragraph("\nDownloaded on: " + LocalDate.now().toString()));
            document.add(new Paragraph("Downloaded using iTextPDF 5.5.13"));
            
            // Close the document
            document.close();

            // Simulate the invoice being downloaded
            JOptionPane.showMessageDialog(null, "The invoice is saved as: " + new File(fileName).getAbsolutePath());
            
            // Clear the selection after downloading
            view.getInvoiceTable().clearSelection();
        }
        catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error generating the invoice: " + e.getMessage());
        }
    }
    
    // Event handler for download button
    private void onDownloadButtonClick() {
        int selectedRow = view.getInvoiceTable().getSelectedRow();

        if (selectedRow == -1) {
            // No invoice selected
            JOptionPane.showMessageDialog(null, "Please select an invoice to download.");
        } else {
            // Get the selected invoice ID (assuming the invoice ID is in the first column)
            String invoiceID = (String) view.getInvoiceTable().getValueAt(selectedRow, 0);
            downloadInvoice();
            
            // Clear the selection after downloading
            view.getInvoiceTable().clearSelection();
            
            // Display success message
            JOptionPane.showMessageDialog(null, "Invoice " + invoiceID + " downloaded successfully.");
        }
    }
}
