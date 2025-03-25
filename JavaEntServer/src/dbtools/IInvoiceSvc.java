/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package dbtools;

//import domain
import domain.Invoice;

//import arraylist
import java.util.ArrayList;

//Interface defining CRUD operations for the imported domain.


/**
 *
 * @author iyana
 */
public interface IInvoiceSvc {
    
    
    // Select by any other parameter (e.g., email or phone number
    public Invoice selectInvoiceParam(String invoiceID);
    
    // Select all
    public ArrayList<Invoice> selectAllInvoice();
    
    // Update an existing record
    public int updateInvoice(Invoice obj);
    
    
    
    // Insert a new using parameters (perhaps for a different method of insertion
    public int insertInvoiceParam(Invoice obj);
    
    // Delete by their unique identifier
    public int deleteInvoice(String invoiceID);
}
