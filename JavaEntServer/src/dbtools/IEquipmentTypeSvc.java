/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package dbtools;

//import domain
import domain.EquipmentType;

//import arraylist
import java.util.ArrayList;

//Interface defining CRUD operations for the imported domain.


/**
 *
 * @author iyana
 */
public interface IEquipmentTypeSvc {
    
    
    // Select by any other parameter (e.g., email or phone number)
    public EquipmentType selectEqTypeParam(String eqTypeID);
    
    // Select all
    public ArrayList<EquipmentType> selectAllEqType();
    
    // Update an existing record
    public int updateEqType(EquipmentType obj);
    
    
    
    // Insert a new using parameters (perhaps for a different method of insertion)
    public int insertEqTypeParam(EquipmentType obj);
    
    // Delete by their unique identifier
    public int deleteEqType(String eqTypeID);
}
