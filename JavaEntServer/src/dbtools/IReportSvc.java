/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package dbtools;

//import domain
import domain.Report;

//import arraylist
import java.util.ArrayList;

//Interface defining CRUD operations for the imported domain.


/**
 *
 * @author iyana
 */
public interface IReportSvc {
    
    
    // Select by any other parameter (e.g., email or phone number)
    public Report selectReportParam(String reportID);
    
    // Select all
    public ArrayList<Report> selectAllReport();
    
    // Update an existing record
    public int updateReport(Report obj);
    
    
    // Insert a new using parameters (perhaps for a different method of insertion)
    public int insertReportParam(Report obj);
    
    // Delete by their unique identifier
    public int deleteReport(String reportID);
}
