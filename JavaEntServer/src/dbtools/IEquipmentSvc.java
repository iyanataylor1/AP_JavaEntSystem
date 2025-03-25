/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package dbtools;

//import domain
import domain.Equipment;

//import arraylist
import java.util.ArrayList;

//Interface defining CRUD operations for the imported domain.


/**
 *
 * @author iyana
 */
public interface IEquipmentSvc {
    
    
    // Select by any other parameter (e.g., email or phone number)
    public Equipment selectEquipmentParam(String eqID);
    
    // Select all
    public ArrayList<Equipment> selectAllEquipment();
    
    // Update an existing record
    public int updateEquipment(Equipment obj);
    
    
    
    // Insert a new using parameters (perhaps for a different method of insertion)
    public int insertEquipmentParam(Equipment obj);
    
    // Delete by their unique identifier
    public int deleteEquipment(String eqID);
}
