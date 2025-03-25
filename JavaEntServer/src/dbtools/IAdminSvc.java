/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package dbtools;

//import domain
import domain.Admin;

//import arraylist
import java.util.ArrayList;

//Interface defining CRUD operations for the imported domain.

/**
 *
 * @author iyana
 */
public interface IAdminSvc {
    
    
    // Select by any other parameter (e.g., email or phone number)
    public Admin selectAdminParam(String adminID);
    
    // Select all
    public ArrayList<Admin> selectAllAdmin();
    
    // Update an existing record
    public int updateAdmin(Admin obj);
    
    
    
    // Insert a new using parameters (perhaps for a different method of insertion)
    public int insertAdminParam(Admin obj);
    
    // Delete by their unique identifier
    public int deleteAdmin(String adminID); 
    
}
