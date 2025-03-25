/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package networktools;

//import domains
import domain.*;

//imports from exceptions
import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

//imports for network
import java.net.Socket;

//import for array list
import java.util.ArrayList;

//imports for dbtools (interfaces and DAOs)
import dbtools.*;

//logging
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


/**
 *
 * @author iyana
 */
public class ServerEngine extends SQLProvider implements Runnable{
    private Socket connection;
    private ObjectInputStream is;       
    private ObjectOutputStream os; 
    
    private Logger logger = LogManager.getLogger(Server.class);
    
    public ServerEngine(Socket connection){
        this.connection = connection;
        new Thread(this).start();
    }
    
    //get streams method (remeber try/catch)
    public void getStreams() {
        try {
            logger.warn("Attempting to setup Server Streams to Client, Errors may Occur");
            os = new ObjectOutputStream(connection.getOutputStream()); 
            is = new ObjectInputStream(connection.getInputStream());
            //System.out.println("Server streams connected to client streams");
            logger.info("Server Streams Successfully Configured to Client");
        }
        catch(IOException ex) {
            logger.error(ex.getMessage());
        }
    }
    
    //close streams method (remember try/catch)
    public void closeStreams() {
        try {
            logger.warn("Attempting to Close Server Streams, Errors may Occur");
            os.close();
            is.close();
            connection.close();
            //System.out.println("Server streams closed");
            logger.info("Server Streams Successfully Closed");
        }
        catch(IOException ex) {
            logger.error(ex.getMessage());
        }
    }
    
    @Override
    public void run(){
        String action = "";
        try {
            this.getStreams();
            do {
                try {
                    logger.warn("Attempting to receive data from client");
                    action = (String)is.readObject();
                    logger.info("Action successfully received from client");

                    // Dispatch action to relevant method
                    handleAction(action);

                    os.flush();
                } catch (ClassNotFoundException | ClassCastException ex) {
                    logger.error(ex.getMessage());
                    os.writeObject(false);
                    ex.printStackTrace();
                }
            } while (!action.equals("Exit"));
            this.closeStreams();
            System.out.println("\n");
        }
        catch (EOFException ex) {
            logger.info("Client has terminated connection with the server");
        } catch (IOException ex) {
            logger.error(ex.getMessage());
        }
    }
    
    //handle requested actions (use switch-case)
    private void handleAction(String action){
        switch (action){
            case "LOGIN":
                handleLogin();
                break;
            //for admin
                //select all
            case "Select All Admins":
                selectAllAdmins();
                break;
                //select 
            case "Select Admin":
                selectAdmin();
                break;
                //insert
            case "Insert Admin":
                insertAdmin();
                break;    
                //update
            case "Update Admin":
                updateAdmin();
                break;
                //delete
            case "Delete Admin":
                deleteAdmin();
                break;

            //for customer
                //select all
            case "Select All Customers":
                selectAllCustomers();
                break;
                //select
            case "Select Customer":
                selectCustomer();
                break;
                //insert
            case "Insert Customer":
                insertCustomer();
                break;  
                //update
            case "Update Customer":
                updateCustomer();
                break;
                //delete
            case "Delete Customer":
                deleteCustomer();
                break;

            //for event
                //select all
            case "Select All Events":
                selectAllEvents();
                break;
                //select
            case "Select Event":
                selectEvent();
                break;
                //insert
            case "Insert Event":
                insertEvent();
                break;  
                //update
            case "Update Event":
                updateEvent();
                break;
                //delete
            case "Delete Event":
                deleteEvent();
                break;

            //for equipment type
                //select all
            case "Select All Equipment Types":
                selectAllEquipmentTypes();
                break;
                //select
            case "Select Equipment Type":
                selectEquipmentType();
                break;
                //insert
            case "Insert Equipment Type":
                insertEquipmentType();
                break;
                //update
            case "Update Equipment Type":
                updateEquipmentType();
                break;
                //delete
            case "Delete Equipment Type":
                deleteEquipmentType();
                break;

            //for equipment
                //select all
            case "Select All Equipment":
                selectAllEquipment();
                break;
                //select
            case "Select Equipment":
                selectEquipment();
                break;
                //insert
            case "Insert Equipment":
                insertEquipment();
                break;
                //update
            case "Update Equipment":
                updateEquipment();
                break;
                //delete
            case "Delete Equipment":
                deleteEquipment();
                break;

            //for booking
                //select all
            case "Select All Bookings":
                selectAllBookings();
                break;
                //select
            case "Select Booking":
                selectBooking();
                break;
                //insert
            case "Insert Booking":
                insertBooking();
                break;
                //update
            case "Update Booking":
                updateBooking();
                break;
                //delete
            case "Delete Booking":
                deleteBooking();
                break;

            //for invoice
                //select all
            case "Select All Invoices":
                selectAllInvoices();
                break;
                //select
            case "Select Invoice":
                selectInvoice();
                break;
                //insert
            case "Insert Invoice":
                insertInvoice();
                break;
                //update
            case "Update Invoice":
                updateInvoice();
                break;
                //delete
            case "Delete Invoice":
                deleteInvoice();
                break;
            case "Generate Invoice":  // New case for invoice generation
                generateInvoice();
                break;

            //for price
                //select all
            case "Select All Prices":
                selectAllPrices();
                break;
                //select
            case "Select Price":
                selectPrice();
                break;
                //insert
            case "Insert Price":
                insertPrice();
                break;
                //update
            case "Update Price":
                updatePrice();
                break;
                //delete
            case "Delete Price":
                deletePrice();
                break;

            //for report
                //select all
            case "Select All Reports":
                selectAllReports();
                break;
                //select
            case "Select Report":
                selectReport();
                break;
                //insert
            case "Insert Report":
                insertReport();
                break;
                //update
            case "Update Report":
                updateReport();
                break;
                //delete
            case "Delete Report":
                deleteReport();
                break;
            
            default:
                logger.warn("Action not recognized: " + action);
        }
    }
    
    //then the individual methods for each action in th switch-case
    
    //login
    private void handleLogin(){
        try{
            String username = (String)is.readObject();
            String password = (String)is.readObject();
            
            String query = "SELECT * FROM Admin WHERE username = ? AND password = ?";
            prepStat = con.prepareStatement(query);
            prepStat.setString(1, username);
            prepStat.setString(2, password);
            
            result = prepStat.executeQuery();
            boolean isAuthenticated = result.next(); //true if credentials match
            
            os.writeObject(isAuthenticated);
            os.flush();
        }
        catch(Exception e){
            e.printStackTrace();
        }
        finally {
            //close resources after query execution
            try{
                if (result != null) result.close();
                if (prepStat != null) prepStat.close();
            }
            catch (Exception ignored){}
        }
    }
    
    //for admin
        //select all
    private void selectAllAdmins(){
        try {
            IAdminSvc svc = new AdminSQLProvider();
            ArrayList<Admin> adminList = svc.selectAllAdmin();
            os.writeObject(adminList);
        }
        catch (IOException ex) {
            logger.error("I/O error occurred: " + ex.getMessage(), ex);
        } 
        catch (Exception ex) {
            // Catch any other unexpected exceptions
            logger.error("An unexpected error occurred: " + ex.getMessage(), ex);
        }
    }
        //select
    private void selectAdmin(){
        try{
            String adminID = (String)is.readObject();
            IAdminSvc svc = new AdminSQLProvider();
            Admin admin = svc.selectAdminParam(adminID);
            os.writeObject(admin);
        }
        catch (IOException ex) {
            logger.error("I/O error occurred: " + ex.getMessage(), ex);
        } 
        catch (Exception ex) {
            // Catch any other unexpected exceptions
            logger.error("An unexpected error occurred: " + ex.getMessage(), ex);
        }
    }
        //insert
    private void insertAdmin(){
        try{
            Admin obj = (Admin)is.readObject();
            IAdminSvc svc = new  AdminSQLProvider();
            int n = svc.insertAdminParam(obj);
            os.writeObject(n > 0);
        }
        catch (IOException ex) {
            logger.error("I/O error occurred: " + ex.getMessage(), ex);
        } 
        catch (Exception ex) {
            // Catch any other unexpected exceptions
            logger.error("An unexpected error occurred: " + ex.getMessage(), ex);
        }
    }
        //update
    private void updateAdmin(){
        try{
            Admin obj = (Admin)is.readObject();
            IAdminSvc svc = new AdminSQLProvider();
            int n = svc.updateAdmin(obj);
            os.writeObject(n > 0);
        }
        catch (IOException ex) {
            logger.error("I/O error occurred: " + ex.getMessage(), ex);
        } 
        catch (Exception ex) {
            // Catch any other unexpected exceptions
            logger.error("An unexpected error occurred: " + ex.getMessage(), ex);
        }
    }
        //delete
    private void deleteAdmin(){
        try{
            String adminID = (String)is.readObject();
            IAdminSvc svc = new AdminSQLProvider();
            int n = svc.deleteAdmin(adminID);
            os.writeObject(n > 0);
        }
        catch (IOException ex) {
            logger.error("I/O error occurred: " + ex.getMessage(), ex);
        } 
        catch (Exception ex) {
            // Catch any other unexpected exceptions
            logger.error("An unexpected error occurred: " + ex.getMessage(), ex);
        }
    }
    
    //for customer
        //select all
    private void selectAllCustomers(){
        try {
            ICustomerSvc svc = new CustomerSQLProvider();
            ArrayList<Customer> customerList = svc.selectAllCustomer();
            os.writeObject(customerList);
        }
        catch (IOException ex) {
            logger.error("I/O error occurred: " + ex.getMessage(), ex);
        } 
        catch (Exception ex) {
            // Catch any other unexpected exceptions
            logger.error("An unexpected error occurred: " + ex.getMessage(), ex);
        }
    }
        //select
    private void selectCustomer(){
        try{
            String customerID = (String)is.readObject();
            ICustomerSvc svc = new CustomerSQLProvider();
            Customer customer = svc.selectCustomerParam(customerID);
            os.writeObject(customer);
        }
        catch (IOException ex) {
            logger.error("I/O error occurred: " + ex.getMessage(), ex);
        } 
        catch (Exception ex) {
            // Catch any other unexpected exceptions
            logger.error("An unexpected error occurred: " + ex.getMessage(), ex);
        }
    }
        //insert
    private void insertCustomer(){
        try{
            Customer obj = (Customer)is.readObject();
            ICustomerSvc svc = new CustomerSQLProvider();
            int n = svc.insertCustomerParam(obj);
            os.writeObject(n > 0);
        }
        catch (IOException ex) {
            logger.error("I/O error occurred: " + ex.getMessage(), ex);
        } 
        catch (Exception ex) {
            // Catch any other unexpected exceptions
            logger.error("An unexpected error occurred: " + ex.getMessage(), ex);
        }
    }
        //update
    private void updateCustomer(){
        try{
            Customer obj = (Customer)is.readObject();
            ICustomerSvc svc = new CustomerSQLProvider();
            int n = svc.updateCustomer(obj);
            os.writeObject(n > 0);
        }
        catch (IOException ex) {
            logger.error("I/O error occurred: " + ex.getMessage(), ex);
        } 
        catch (Exception ex) {
            // Catch any other unexpected exceptions
            logger.error("An unexpected error occurred: " + ex.getMessage(), ex);
        }
    }
        //delete
    private void deleteCustomer(){
        try{
            String customerID = (String)is.readObject();
            ICustomerSvc svc = new CustomerSQLProvider();
            int n = svc.deleteCustomer(customerID);
            os.writeObject(n > 0);
        }
        catch (IOException ex) {
            logger.error("I/O error occurred: " + ex.getMessage(), ex);
        } 
        catch (Exception ex) {
            // Catch any other unexpected exceptions
            logger.error("An unexpected error occurred: " + ex.getMessage(), ex);
        }
    }

    //for event
        //select all
    private void selectAllEvents(){
        try {
            IEventSvc svc = new EventSQLProvider();
            ArrayList<Event> eventList = svc.selectAllEvent();
            os.writeObject(eventList);
        }
        catch (IOException ex) {
            logger.error("I/O error occurred: " + ex.getMessage(), ex);
        } 
        catch (Exception ex) {
            // Catch any other unexpected exceptions
            logger.error("An unexpected error occurred: " + ex.getMessage(), ex);
        }
    }
        //select
    private void selectEvent(){
        try{
            String eventID = (String)is.readObject();
            IEventSvc svc = new EventSQLProvider();
            Event event = svc.selectEventParam(eventID);
            os.writeObject(event);
        }
        catch (IOException ex) {
            logger.error("I/O error occurred: " + ex.getMessage(), ex);
        } 
        catch (Exception ex) {
            // Catch any other unexpected exceptions
            logger.error("An unexpected error occurred: " + ex.getMessage(), ex);
        }
    }
        //insert
    private void insertEvent(){
        try{
            Event obj = (Event)is.readObject();
            IEventSvc svc = new EventSQLProvider();
            int n = svc.insertEventParam(obj);
            os.writeObject(n > 0);
        }
        catch (IOException ex) {
            logger.error("I/O error occurred: " + ex.getMessage(), ex);
        } 
        catch (Exception ex) {
            // Catch any other unexpected exceptions
            logger.error("An unexpected error occurred: " + ex.getMessage(), ex);
        }
    }
        //update
    private void updateEvent(){
        try{
            Event obj = (Event)is.readObject();
            IEventSvc svc = new EventSQLProvider();
            int n = svc.updateEvent(obj);
            os.writeObject(n > 0);
        }
        catch (IOException ex) {
            logger.error("I/O error occurred: " + ex.getMessage(), ex);
        } 
        catch (Exception ex) {
            // Catch any other unexpected exceptions
            logger.error("An unexpected error occurred: " + ex.getMessage(), ex);
        }
    }
        //delete
    private void deleteEvent(){
        try{
            String eventID = (String)is.readObject();
            IEventSvc svc = new EventSQLProvider();
            int n = svc.deleteEvent(eventID);
            os.writeObject(n > 0);
        }
        catch (IOException ex) {
            logger.error("I/O error occurred: " + ex.getMessage(), ex);
        } 
        catch (Exception ex) {
            // Catch any other unexpected exceptions
            logger.error("An unexpected error occurred: " + ex.getMessage(), ex);
        }
    }
    
    //for equipment type
        //select all
    private void selectAllEquipmentTypes(){
        try {
            IEquipmentTypeSvc svc = new EquipmentTypeSQLProvider();
            ArrayList<EquipmentType> eqTypeList = svc.selectAllEqType();
            os.writeObject(eqTypeList);
        }
        catch (IOException ex) {
            logger.error("I/O error occurred: " + ex.getMessage(), ex);
        } 
        catch (Exception ex) {
            // Catch any other unexpected exceptions
            logger.error("An unexpected error occurred: " + ex.getMessage(), ex);
        }
    }
        //select
    private void selectEquipmentType(){
        try{
            String eqTypeID = (String)is.readObject();
            IEquipmentTypeSvc svc = new EquipmentTypeSQLProvider();
            EquipmentType eqType = svc.selectEqTypeParam(eqTypeID);
            os.writeObject(eqType);
        }
        catch (IOException ex) {
            logger.error("I/O error occurred: " + ex.getMessage(), ex);
        } 
        catch (Exception ex) {
            // Catch any other unexpected exceptions
            logger.error("An unexpected error occurred: " + ex.getMessage(), ex);
        }
    }
        //insert
    private void insertEquipmentType(){
        try{
            EquipmentType obj = (EquipmentType)is.readObject();
            IEquipmentTypeSvc svc = new EquipmentTypeSQLProvider();
            int n = svc.insertEqTypeParam(obj);
            os.writeObject(n > 0);
        }
        catch (IOException ex) {
            logger.error("I/O error occurred: " + ex.getMessage(), ex);
        } 
        catch (Exception ex) {
            // Catch any other unexpected exceptions
            logger.error("An unexpected error occurred: " + ex.getMessage(), ex);
        }
    }
        //update
    private void updateEquipmentType(){
        try{
            EquipmentType obj = (EquipmentType)is.readObject();
            IEquipmentTypeSvc svc = new EquipmentTypeSQLProvider();
            int n = svc.updateEqType(obj);
            os.writeObject(n > 0);
        }
        catch (IOException ex) {
            logger.error("I/O error occurred: " + ex.getMessage(), ex);
        } 
        catch (Exception ex) {
            // Catch any other unexpected exceptions
            logger.error("An unexpected error occurred: " + ex.getMessage(), ex);
        }
    }
        //delete
    private void deleteEquipmentType(){
        try{
            String eqTypeID = (String)is.readObject();
            IEquipmentTypeSvc svc = new EquipmentTypeSQLProvider();
            int n = svc.deleteEqType(eqTypeID);
            os.writeObject(n > 0);
        }
        catch (IOException ex) {
            logger.error("I/O error occurred: " + ex.getMessage(), ex);
        } 
        catch (Exception ex) {
            // Catch any other unexpected exceptions
            logger.error("An unexpected error occurred: " + ex.getMessage(), ex);
        }
    }

    //for equipment
        //select all
    private void selectAllEquipment(){
        try {
            IEquipmentSvc svc = new EquipmentSQLProvider();
            ArrayList<Equipment> eqList = svc.selectAllEquipment();
            os.writeObject(eqList);
        }
        catch (IOException ex) {
            logger.error("I/O error occurred: " + ex.getMessage(), ex);
        } 
        catch (Exception ex) {
            // Catch any other unexpected exceptions
            logger.error("An unexpected error occurred: " + ex.getMessage(), ex);
        }
    }
        //select
    private void selectEquipment(){
        try{
            String eqID = (String)is.readObject();
            IEquipmentSvc svc = new EquipmentSQLProvider();
            Equipment eq = svc.selectEquipmentParam(eqID);
            os.writeObject(eq);
        }
        catch (IOException ex) {
            logger.error("I/O error occurred: " + ex.getMessage(), ex);
        } 
        catch (Exception ex) {
            // Catch any other unexpected exceptions
            logger.error("An unexpected error occurred: " + ex.getMessage(), ex);
        }
    }
        //insert
    private void insertEquipment(){
        try{
            Equipment obj = (Equipment)is.readObject();
            IEquipmentSvc svc = new EquipmentSQLProvider();
            int n = svc.insertEquipmentParam(obj);
            os.writeObject(n > 0);
        }
        catch (IOException ex) {
            logger.error("I/O error occurred: " + ex.getMessage(), ex);
        } 
        catch (Exception ex) {
            // Catch any other unexpected exceptions
            logger.error("An unexpected error occurred: " + ex.getMessage(), ex);
        }
    }
        //update
    private void updateEquipment(){
        try{
            Equipment obj = (Equipment)is.readObject();
            IEquipmentSvc svc = new EquipmentSQLProvider();
            int n = svc.updateEquipment(obj);
            os.writeObject(n > 0);
        }
        catch (IOException ex) {
            logger.error("I/O error occurred: " + ex.getMessage(), ex);
        } 
        catch (Exception ex) {
            // Catch any other unexpected exceptions
            logger.error("An unexpected error occurred: " + ex.getMessage(), ex);
        }
    }
        //delete
    private void deleteEquipment(){
        try{
            String eqID = (String)is.readObject();
            IEquipmentSvc svc = new EquipmentSQLProvider();
            int n = svc.deleteEquipment(eqID);
            os.writeObject(n > 0);
        }
        catch (IOException ex) {
            logger.error("I/O error occurred: " + ex.getMessage(), ex);
        } 
        catch (Exception ex) {
            // Catch any other unexpected exceptions
            logger.error("An unexpected error occurred: " + ex.getMessage(), ex);
        }
    }


    //for booking
        //select all
    private void selectAllBookings(){
        try {
            IBookingSvc svc = new BookingSQLProvider();
            ArrayList<Booking> bookingList = svc.selectAllBooking();
            os.writeObject(bookingList);
        }
        catch (IOException ex) {
            logger.error("I/O error occurred: " + ex.getMessage(), ex);
        } 
        catch (Exception ex) {
            // Catch any other unexpected exceptions
            logger.error("An unexpected error occurred: " + ex.getMessage(), ex);
        }
    }
        //select
    private void selectBooking(){
        try{
            String bookingID = (String)is.readObject();
            IBookingSvc svc = new BookingSQLProvider();
            Booking booking = svc.selectBookingParam(bookingID);
            os.writeObject(booking);
        }
        catch (IOException ex) {
            logger.error("I/O error occurred: " + ex.getMessage(), ex);
        } 
        catch (Exception ex) {
            // Catch any other unexpected exceptions
            logger.error("An unexpected error occurred: " + ex.getMessage(), ex);
        }
    }
        //insert
    private void insertBooking(){
        try{
            Booking obj = (Booking)is.readObject();
            IBookingSvc svc = new BookingSQLProvider();
            int n = svc.insertBookingParam(obj);
            os.writeObject(n > 0);
        }
        catch (IOException ex) {
            logger.error("I/O error occurred: " + ex.getMessage(), ex);
        } 
        catch (Exception ex) {
            // Catch any other unexpected exceptions
            logger.error("An unexpected error occurred: " + ex.getMessage(), ex);
        }
    }
        //update
    private void updateBooking(){
        try{
            Booking obj = (Booking)is.readObject();
            IBookingSvc svc = new BookingSQLProvider();
            int n = svc.updateBooking(obj);
            os.writeObject(n > 0);
        }
        catch (IOException ex) {
            logger.error("I/O error occurred: " + ex.getMessage(), ex);
        } 
        catch (Exception ex) {
            // Catch any other unexpected exceptions
            logger.error("An unexpected error occurred: " + ex.getMessage(), ex);
        }
    }
        //delete
    private void deleteBooking(){
        try{
            String bookingID = (String)is.readObject();
            IBookingSvc svc = new BookingSQLProvider();
            int n = svc.deleteBooking(bookingID);
            os.writeObject(n > 0);
        }
        catch (IOException ex) {
            logger.error("I/O error occurred: " + ex.getMessage(), ex);
        } 
        catch (Exception ex) {
            // Catch any other unexpected exceptions
            logger.error("An unexpected error occurred: " + ex.getMessage(), ex);
        }
    }
    
    //for invoice
        //select all
    private void selectAllInvoices(){
        try {
            IInvoiceSvc svc = new InvoiceSQLProvider();
            ArrayList<Invoice> invoiceList = svc.selectAllInvoice();
            os.writeObject(invoiceList);
        }
        catch (IOException ex) {
            logger.error("I/O error occurred: " + ex.getMessage(), ex);
        } 
        catch (Exception ex) {
            // Catch any other unexpected exceptions
            logger.error("An unexpected error occurred: " + ex.getMessage(), ex);
        }
    }
        //select
    private void selectInvoice(){
        try{
            String invoiceID = (String)is.readObject();
            IInvoiceSvc svc = new InvoiceSQLProvider();
            Invoice invoice = svc.selectInvoiceParam(invoiceID);
            os.writeObject(invoice);
        }
        catch (IOException ex) {
            logger.error("I/O error occurred: " + ex.getMessage(), ex);
        } 
        catch (Exception ex) {
            // Catch any other unexpected exceptions
            logger.error("An unexpected error occurred: " + ex.getMessage(), ex);
        }
    }
        //insert
    private void insertInvoice(){
        try{
            Invoice obj = (Invoice)is.readObject();
            IInvoiceSvc svc = new InvoiceSQLProvider();
            int n = svc.insertInvoiceParam(obj);
            os.writeObject(n > 0);
        }
        catch (IOException ex) {
            logger.error("I/O error occurred: " + ex.getMessage(), ex);
        } 
        catch (Exception ex) {
            // Catch any other unexpected exceptions
            logger.error("An unexpected error occurred: " + ex.getMessage(), ex);
        }
    }
        //update
    private void updateInvoice(){
        try{
            Invoice obj = (Invoice)is.readObject();
            IInvoiceSvc svc = new InvoiceSQLProvider();
            int n = svc.updateInvoice(obj);
            os.writeObject(n > 0);
        }
        catch (IOException ex) {
            logger.error("I/O error occurred: " + ex.getMessage(), ex);
        } 
        catch (Exception ex) {
            // Catch any other unexpected exceptions
            logger.error("An unexpected error occurred: " + ex.getMessage(), ex);
        }
    }
        //delete
    private void deleteInvoice(){
        try{
            String invoiceID = (String)is.readObject();
            IInvoiceSvc svc = new InvoiceSQLProvider();
            int n = svc.deleteInvoice(invoiceID);
            os.writeObject(n > 0);
        }
        catch (IOException ex) {
            logger.error("I/O error occurred: " + ex.getMessage(), ex);
        } 
        catch (Exception ex) {
            // Catch any other unexpected exceptions
            logger.error("An unexpected error occurred: " + ex.getMessage(), ex);
        }
    }

        //generate invoice
    private void generateInvoice() {
        try {
            String eventID = (String) is.readObject(); // Receive eventID from client

            InvoiceSQLProvider invoiceProvider = new InvoiceSQLProvider();
            boolean success = invoiceProvider.generateInvoiceForEvent(eventID);

            os.writeObject(success); // Send success status back to client
            os.flush();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //for price
        //select all
    private void selectAllPrices(){
        try {
            IPriceSvc svc = new PriceSQLProvider();
            ArrayList<Price> priceList = svc.selectAllPrice();
            os.writeObject(priceList);
        }
        catch (IOException ex) {
            logger.error("I/O error occurred: " + ex.getMessage(), ex);
        } 
        catch (Exception ex) {
            // Catch any other unexpected exceptions
            logger.error("An unexpected error occurred: " + ex.getMessage(), ex);
        }
    }
        //select
    private void selectPrice(){
        try{
            String priceID = (String)is.readObject();
            IPriceSvc svc = new PriceSQLProvider();
            Price price = svc.selectPriceParam(priceID);
            os.writeObject(price);
        }
        catch (IOException ex) {
            logger.error("I/O error occurred: " + ex.getMessage(), ex);
        } 
        catch (Exception ex) {
            // Catch any other unexpected exceptions
            logger.error("An unexpected error occurred: " + ex.getMessage(), ex);
        }
    }
        //insert
    private void insertPrice(){
        try{
            Price obj = (Price)is.readObject();
            IPriceSvc svc = new PriceSQLProvider();
            int n = svc.insertPriceParam(obj);
            os.writeObject(n > 0);
        }
        catch (IOException ex) {
            logger.error("I/O error occurred: " + ex.getMessage(), ex);
        } 
        catch (Exception ex) {
            // Catch any other unexpected exceptions
            logger.error("An unexpected error occurred: " + ex.getMessage(), ex);
        }
    }
        //update
    private void updatePrice(){
        try{
            Price obj = (Price)is.readObject();
            IPriceSvc svc = new PriceSQLProvider();
            int n = svc.updatePrice(obj);
            os.writeObject(n > 0);
        }
        catch (IOException ex) {
            logger.error("I/O error occurred: " + ex.getMessage(), ex);
        } 
        catch (Exception ex) {
            // Catch any other unexpected exceptions
            logger.error("An unexpected error occurred: " + ex.getMessage(), ex);
        }
    }
        //delete
    private void deletePrice(){
        try{
            String priceID = (String)is.readObject();
            IPriceSvc svc = new PriceSQLProvider();
            int n = svc.deletePrice(priceID);
            os.writeObject(n > 0);
        }
        catch (IOException ex) {
            logger.error("I/O error occurred: " + ex.getMessage(), ex);
        } 
        catch (Exception ex) {
            // Catch any other unexpected exceptions
            logger.error("An unexpected error occurred: " + ex.getMessage(), ex);
        }
    }

    //for report
        //select all
    private void selectAllReports(){
        try {
            IReportSvc svc = new ReportSQLProvider();
            ArrayList<Report> reportList = svc.selectAllReport();
            os.writeObject(reportList);
        }
        catch (IOException ex) {
            logger.error("I/O error occurred: " + ex.getMessage(), ex);
        } 
        catch (Exception ex) {
            // Catch any other unexpected exceptions
            logger.error("An unexpected error occurred: " + ex.getMessage(), ex);
        }
    }
        //select
    private void selectReport(){
        try{
            String reportID = (String)is.readObject();
            IReportSvc svc = new ReportSQLProvider();
            Report report = svc.selectReportParam(reportID);
            os.writeObject(report);
        }
        catch (IOException ex) {
            logger.error("I/O error occurred: " + ex.getMessage(), ex);
        } 
        catch (Exception ex) {
            // Catch any other unexpected exceptions
            logger.error("An unexpected error occurred: " + ex.getMessage(), ex);
        }
    }
        //insert
    private void insertReport(){
        try{
            Report obj = (Report)is.readObject();
            IReportSvc svc = new ReportSQLProvider();
            int n = svc.insertReportParam(obj);
            os.writeObject(n > 0);
        }
        catch (IOException ex) {
            logger.error("I/O error occurred: " + ex.getMessage(), ex);
        } 
        catch (Exception ex) {
            // Catch any other unexpected exceptions
            logger.error("An unexpected error occurred: " + ex.getMessage(), ex);
        }
    }
        //update
    private void updateReport(){
        try{
            Report obj = (Report)is.readObject();
            IReportSvc svc = new ReportSQLProvider();
            int n = svc.updateReport(obj);
            os.writeObject(n > 0);
        }
        catch (IOException ex) {
            logger.error("I/O error occurred: " + ex.getMessage(), ex);
        } 
        catch (Exception ex) {
            // Catch any other unexpected exceptions
            logger.error("An unexpected error occurred: " + ex.getMessage(), ex);
        }
    }
        //delete
    private void deleteReport(){
        try{
            String reportID = (String)is.readObject();
            IReportSvc svc = new ReportSQLProvider();
            int n = svc.deleteReport(reportID);
            os.writeObject(n > 0);
        }
        catch (IOException ex) {
            logger.error("I/O error occurred: " + ex.getMessage(), ex);
        } 
        catch (Exception ex) {
            // Catch any other unexpected exceptions
            logger.error("An unexpected error occurred: " + ex.getMessage(), ex);
        }
    }
}
