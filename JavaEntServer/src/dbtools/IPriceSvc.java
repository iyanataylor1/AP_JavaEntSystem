/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package dbtools;

//import domain
import domain.Price;

//import arraylist
import java.util.ArrayList;

//Interface defining CRUD operations for the imported domain.


/**
 *
 * @author iyana
 */
public interface IPriceSvc {
    
    
    // Select by any other parameter (e.g., email or phone number)
    public Price selectPriceParam(String priceID);
    
    // Select all
    public ArrayList<Price> selectAllPrice();
    
    // Update an existing record
    public int updatePrice(Price obj);
    
    
    
    // Insert a new using parameters (perhaps for a different method of insertion)
    public int insertPriceParam(Price obj);
    
    // Delete by their unique identifier
    public int deletePrice(String priceID);
}
