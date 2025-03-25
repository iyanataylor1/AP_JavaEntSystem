/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dbtools;

//import domain
import domain.Report;

import java.time.LocalDate;

//import sql exception
import java.sql.SQLException;

//import arraylist
import java.util.ArrayList;
import java.util.List;

//import swing for joptionpane
import javax.swing.JOptionPane;

//import for logging
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


/**
 *
 * @author iyana
 */
public class ReportSQLProvider extends SQLProvider implements IReportSvc{
    //logger
    private static Logger logger = LogManager.getLogger(ReportSQLProvider.class);
     
    
    //select param
    public Report selectReportParam(String reportID){
        Report report = null;
        
        try{
            logger.warn("Attempting to retrieve data from database, errors may occur");
            String query = "SELECT * FROM Report WHERE reportID = ?";          //specify query to pull all records
            prepStat = con.prepareStatement(query);                     //create SQL statement
            prepStat.setString(1, reportID);
            result = prepStat.executeQuery();                           //execute the above query
            logger.info("Record Successfully Retrieved");
            
            while(result.next()) {
                report = new Report();         
                report.setReportID(result.getString(1));
                report.setReportType(result.getString(2));
                report.setDateGenerated(result.getDate(3).toLocalDate()); 
                report.setAdminID(result.getString(4));
            }
        }
        catch(SQLException ex){
            logger.error(ex.getMessage());
            JOptionPane.showMessageDialog(null, "Could not retrieve record(s)", "SQL Exception", JOptionPane.ERROR_MESSAGE);
        
        }
        return report;
    }
    
    //select all
    public ArrayList<Report> selectAllReport(){
        ArrayList<Report> list = new ArrayList<Report>();
        
        try{
            logger.warn("Attempting to retrieve data from database, errors may occur");
            stat = con.createStatement(); 
            String query = "SELECT * FROM Report";
            logger.info(query);
            result = stat.executeQuery(query);
            logger.info("Record Successfully Retrieved");
            
            while(result.next()){
                Report report = new Report();         
                report.setReportID(result.getString(1));
                report.setReportType(result.getString(2));
                report.setDateGenerated(result.getDate(3).toLocalDate()); 
                report.setAdminID(result.getString(4));
                list.add(report);
            }
        }
        catch(SQLException ex){
            logger.error(ex.getMessage());
            JOptionPane.showMessageDialog(null, "Could not retrieve record(s)", "SQL Exception", JOptionPane.ERROR_MESSAGE);
        }
        return list;
    }
    
    //update
    public int updateReport(Report obj){
        int n;
        
        try{
            logger.warn("Attempting to update data in database, errors may occur");
            stat = con.createStatement();
            String query =
                    "UPDATE Report "
                    + "SET reportType = '" + obj.getReportType() + "', "
                    + "dateGenerated = '" + obj.getDateGenerated() + "', "
                    + "adminID = '" + obj.getAdminID() + "' "
                    + "WHERE reportID = '" + obj.getReportID() + "' ";
            
            logger.info(query);
            n = stat.executeUpdate(query);
            logger.info("Record Successfully Updated");
        }
        catch(SQLException ex){
            logger.error(ex.getMessage());
            JOptionPane.showMessageDialog(null, "Could not update record(s)", "SQL Exception", JOptionPane.ERROR_MESSAGE);	
            n = 0;
        }
        return n;
    }
    
    //insert param
    public int insertReportParam(Report obj){
        int n;
        
        try{
            logger.warn("Attempting to insert data to database, errors may occur");
            String query = "INSERT INTO Report (reportID, reportType, dateGenerated, adminID) VALUES (?, ?, ?, ?)";
            prepStat = con.prepareStatement(query);
            prepStat.setString(1, obj.getReportID());
            prepStat.setString(2, obj.getReportType());
            prepStat.setDate(3, java.sql.Date.valueOf(obj.getDateGenerated()));
            prepStat.setString(4, obj.getAdminID());
            
            n = prepStat.executeUpdate();
            logger.info("Record Successfully Inserted");
        }
        catch(SQLException ex){
            n = 0;
            logger.error(ex.getMessage());
            JOptionPane.showMessageDialog(null, "Could not insert record(s)", "SQL Exception", JOptionPane.ERROR_MESSAGE);
        }
        
        return n;
    }
    
    //delete
    public int deleteReport(String reportID){
        int n;
        
        try{
            logger.warn("Attempting to delete data from database, errors may occur");
            stat = con.createStatement();
            String query = 
                    "DELETE FROM Report "
                    + "WHERE reportID = '" + reportID + "' ";
            
            n = stat.executeUpdate(query);	   
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
