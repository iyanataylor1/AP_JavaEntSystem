/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package dbtools;

//import domain
import domain.Customer;

//import arraylist
import java.util.ArrayList;

//Interface defining CRUD operations for the imported domain.

/**
 *
 * @author iyana
 */
public interface ICustomerSvc {
    
    
    // Select by any other parameter (e.g., email or phone number)
    public Customer selectCustomerParam(String customerID);
    
    // Select all
    public ArrayList<Customer> selectAllCustomer();
    
    // Update an existing record
    public int updateCustomer(Customer obj);
    
    
    
    // Insert a new using parameters (perhaps for a different method of insertion)
    public int insertCustomerParam(Customer obj);
    
    // Delete by their unique identifier
    public int deleteCustomer(String customerID);
}   
