/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dbtools;

//import domain
import domain.Customer;

//import sql exception
import java.sql.SQLException;

//import arraylist
import java.util.ArrayList;

//import swing for joptionpane
import javax.swing.JOptionPane;

//import for logging
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 *
 * @author iyana
 */
public class CustomerSQLProvider extends SQLProvider implements ICustomerSvc{
    //logger
    private static Logger logger = LogManager.getLogger(CustomerSQLProvider.class);
    
    
    
    //select param
    public Customer selectCustomerParam(String customerID){
        Customer customer = null;
        
        try{
            logger.warn("Attempting to retrieve data from database, errors may occur");
            String query = "SELECT * FROM Customer WHERE customerID = ?";   //specify query to pull all records
            prepStat = con.prepareStatement(query);                 //create sql statement
            prepStat.setString(1, customerID);
            result = prepStat.executeQuery();
            logger.info("Record Successfully Retrieved");
            
            while(result.next()){
                customer = new Customer();
                customer.setCustomerID(result.getString(1));
                customer.setFirstName(result.getString(2));
                customer.setLastName(result.getString(3));
                customer.setCompanyName(result.getString(4));
                customer.setPhone(result.getString(5));
                customer.setEmail(result.getString(6));
                customer.setAdminID(result.getString(7));
            }
        }
        catch(SQLException ex){
            logger.error(ex.getMessage());
            JOptionPane.showMessageDialog(null, "Could not retreive record(s)", "SQL Exception", JOptionPane.ERROR_MESSAGE);	
        }
        
        return customer;
    }
    
    //select all
    public ArrayList<Customer> selectAllCustomer(){
        ArrayList<Customer> list = new ArrayList<Customer>(); //create array list to collect results
    
        try{
            logger.warn("Attempting to retrieve data from database, errors may occur");
            stat = con.createStatement();
            String query = "SELECT * FROM Customer";
            logger.info(query);
            result = stat.executeQuery(query);
            logger.info("Record Successfully Retrieved");
            
            while(result.next()){
                Customer customer = new Customer();
                customer.setCustomerID(result.getString(1));
                customer.setFirstName(result.getString(2));
                customer.setLastName(result.getString(3));
                customer.setCompanyName(result.getString(4));
                customer.setPhone(result.getString(5));
                customer.setEmail(result.getString(6));
                customer.setAdminID(result.getString(7));
                list.add(customer);
            }
        }
        catch(SQLException ex){
            logger.error(ex.getMessage());
                        JOptionPane.showMessageDialog(null, "Could not retreive record(s)", "SQL Exception", JOptionPane.ERROR_MESSAGE);	
        }
        
        return list;
    }
    
    //update
    public int updateCustomer(Customer obj){
        int n;
        
        try{
            logger.warn("Attempting to update data in database, errors may occur");
            stat = con.createStatement();
            String query = 
                    "UPDATE Customer "
                    + "SET firstName = '" + obj.getFirstName() + "', "
                    + "lastName = '" + obj.getLastName() + "', "
                    + "companyName = '" + obj.getCompanyName() + "', "
                    + "phone = '" + obj.getPhone() + "', "
                    + "email = '" + obj.getEmail() + "', "
                    + "adminID = '" + obj.getAdminID() + "' "
                    + "WHERE customerID = '" + obj.getCustomerID() + "' ";
            
            logger.info(query);
            n = stat.executeUpdate(query);
            logger.info("Record Successfully Updated");
        }
        catch(SQLException ex){
            logger.error(ex.getMessage());
            JOptionPane.showMessageDialog(null, "Could not update record", "SQL Exception", JOptionPane.ERROR_MESSAGE);	
            n = 0;
        }
        
        return n;
    }
    
    
    
    //insert param
    public int insertCustomerParam(Customer obj){
        int n;
        
        try{
            logger.warn("Attempting to insert data to database, errors may occur");
            String query = "INSERT INTO Customer (customerID, firstName, lastName, companyName, phone, email, adminID) VALUES(?,?,?,?,?,?,?)";
            prepStat = con.prepareStatement(query);	//create SQL statement    	 		
            prepStat.setString(1, obj.getCustomerID());
            prepStat.setString(2, obj.getFirstName());
            prepStat.setString(3, obj.getLastName());  	 		
            prepStat.setString(4, obj.getCompanyName());
            prepStat.setString(5, obj.getPhone());
            prepStat.setString(6, obj.getEmail());
            prepStat.setString(7, obj.getAdminID());
            
            n = prepStat.executeUpdate();
            logger.info("Record Successfully Inserted");
        }
        catch(SQLException ex){
            n = 0;
            logger.error(ex.getMessage());
            JOptionPane.showMessageDialog(null, "Could not insert record", "SQL Exception", JOptionPane.ERROR_MESSAGE);	
        }
        return n;
    }
    
    //delete
    public int deleteCustomer(String customerID){
        int n;
        
        try{
            logger.warn("Attempting to delete data from database, errors may occur");
            stat = con.createStatement();
            String query = 
                    "DELETE FROM Customer "
                    + "WHERE customerID = '" + customerID + "' ";
            
            n = stat.executeUpdate(query);	//execute query   
            logger.info("Record Successfully Deleted");
        }
        catch(SQLException ex){
            n = 0;
            logger.error(ex.getMessage());
            JOptionPane.showMessageDialog(null, "Could not delete record(s)", "SQL Exception", JOptionPane.ERROR_MESSAGE);
        }
        return n;
    }
}
