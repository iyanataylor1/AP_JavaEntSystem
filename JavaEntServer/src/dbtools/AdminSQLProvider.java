/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dbtools;

//import domain
import domain.Admin;

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
public class AdminSQLProvider extends SQLProvider implements IAdminSvc{
    
    //logger
    private static Logger logger = LogManager.getLogger(AdminSQLProvider.class);
     
    
    
    //select param
    public Admin selectAdminParam(String adminID) {
        Admin admin = null;
        
        try {
            logger.warn("Attempting to retrieve data from database, errors may occur");
            String query = "SELECT * FROM Admin WHERE adminID = ?";          //specify query to pull all records
            prepStat = con.prepareStatement(query);                     //create SQL statement
            prepStat.setString(1, adminID);
            result = prepStat.executeQuery();                           //execute the above query
            logger.info("Record Successfully Retrieved");
            
            while(result.next()) {
                admin = new Admin();
                admin.setAdminID(result.getString(1));
                admin.setUsername(result.getString(2));
                admin.setPassword(result.getString(3));
            }
        }
        catch(SQLException ex) {
            logger.error(ex.getMessage());
            JOptionPane.showMessageDialog(null, "Could not retrieve record(s)", "SQL Exception", JOptionPane.ERROR_MESSAGE);
        }
        
        return admin;
    }
    
    //select all
    public ArrayList<Admin> selectAllAdmin() {
        ArrayList<Admin> list = new ArrayList<Admin>();         //create array list to collect results
        
        try {
            logger.warn("Attempting to retrieve data from database, errors may occur");
            stat = con.createStatement(); 
            String query = "SELECT * FROM Admin";
            logger.info(query);
            result = stat.executeQuery(query);
            logger.info("Record Successfully Retrieved");
            
            while(result.next()) {
                Admin admin = new Admin();
                admin.setAdminID(result.getString(1));
                admin.setUsername(result.getString(2));
                admin.setPassword(result.getString(3));
                list.add(admin);
            }
        }
        catch(SQLException ex){
            logger.error(ex.getMessage());
            JOptionPane.showMessageDialog(null, "Could not retrieve record(s)", "SQL Exception", JOptionPane.ERROR_MESSAGE);
        }
        
        return list;
    }
    
    //update
    public int updateAdmin(Admin obj) {
        int n;
        
        try {
            logger.warn("Attempting to update data in database, errors may occur");
            stat = con.createStatement();
            String query =
                    "UPDATE Admin "
                    + "SET username = '" + obj.getUsername() + "', "
                    + "password = '" + obj.getPassword() + "' "
                    + "WHERE adminID = '" + obj.getAdminID() + "' ";
            logger.info(query);
            n = stat.executeUpdate(query);
            logger.info("Record Successfully Updated");
        }
        catch(SQLException ex) {
            logger.error(ex.getMessage());
            JOptionPane.showMessageDialog(null, "Could not update record(s)", "SQL Exception", JOptionPane.ERROR_MESSAGE);	
            n = 0;
        }
        
        return n;
    }
    
    
    
    //insert param
    public int insertAdminParam(Admin obj){
        int n;
        
        try{
            logger.warn("Attempting to insert data to database, errors may occur");
            String query = "INSERT INTO Admin (adminID, username, password) VALUES (?, ?, ?)";
            prepStat = con.prepareStatement(query);
            prepStat.setString(1, obj.getAdminID());
            prepStat.setString(2, obj.getUsername());
            prepStat.setString(3, obj.getPassword());
            
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
    public int deleteAdmin(String adminID){
        int n;
        
        try {
            logger.warn("Attempting to delete data from database, errors may occur");
            stat = con.createStatement();
            String query = 
                    "DELETE FROM Admin "
                    + "WHERE adminID = '" + adminID + "' ";
            
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
