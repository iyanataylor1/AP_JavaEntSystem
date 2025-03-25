/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dbtools;

//import domain
import domain.EquipmentType;

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
public class EquipmentTypeSQLProvider extends SQLProvider implements IEquipmentTypeSvc{
    //logger
    private static Logger logger = LogManager.getLogger(EquipmentTypeSQLProvider.class);
     
    
    
    //select param
    public EquipmentType selectEqTypeParam(String eqTypeID){
        EquipmentType eqType = null;
        
        try{
            logger.warn("Attempting to retrieve data from database, errors may occur");
            String query = "SELECT * FROM EquipmentType WHERE eqTypeID = ?";          //specify query to pull all records
            prepStat = con.prepareStatement(query);                     //create SQL statement
            prepStat.setString(1, eqTypeID);
            result = prepStat.executeQuery();                           //execute the above query
            logger.info("Record Successfully Retrieved");
            
            while(result.next()) {
                eqType = new EquipmentType();
                eqType.setEqTypeID(result.getString(1));
                eqType.setName(result.getString(2));
            }
        }
        catch(SQLException ex){
            logger.error(ex.getMessage());
            JOptionPane.showMessageDialog(null, "Could not retrieve record(s)", "SQL Exception", JOptionPane.ERROR_MESSAGE);
        }
        return eqType;
    }
    
    //select all
    public ArrayList<EquipmentType> selectAllEqType(){
        ArrayList<EquipmentType> list = new ArrayList<EquipmentType>(); 
        
        try{
            logger.warn("Attempting to retrieve data from database, errors may occur");
            stat = con.createStatement(); 
            String query = "SELECT * FROM EquipmentType";
            logger.info(query);
            result = stat.executeQuery(query);
            logger.info("Record Successfully Retrieved");
            
            while(result.next()) {
                EquipmentType eqType = new EquipmentType();
                eqType.setEqTypeID(result.getString(1));
                eqType.setName(result.getString(2));
                list.add(eqType);
            }
        }
        catch(SQLException ex){
            logger.error(ex.getMessage());
            JOptionPane.showMessageDialog(null, "Could not retrieve record(s)", "SQL Exception", JOptionPane.ERROR_MESSAGE);
        }
        
        return list;
    }
    
    //update
    public int updateEqType(EquipmentType obj){
        int n;
        
        try {
            logger.warn("Attempting to update data in database, errors may occur");
            stat = con.createStatement();
            String query =
                    "UPDATE EquipmentType "
                    + "SET name = '" + obj.getName() + "' "
                    + "WHERE eqTypeID = '" + obj.getEqTypeID() + "' ";
            
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
    public int insertEqTypeParam(EquipmentType obj){
        int n;
        
        try{
            logger.warn("Attempting to insert data to database, errors may occur");
            String query = "INSERT INTO EquipmentType (eqTypeID, name) VALUES (?, ?)";
            prepStat = con.prepareStatement(query);
            prepStat.setString(1, obj.getEqTypeID());
            prepStat.setString(2, obj.getName());
            
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
    public int deleteEqType(String eqTypeID){
        int n;
        
        try {
            logger.warn("Attempting to delete data from database, errors may occur");
            stat = con.createStatement();
            String query = 
                    "DELETE FROM EquipmentType "
                    + "WHERE eqTypeID = '" + eqTypeID + "' ";
            
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
