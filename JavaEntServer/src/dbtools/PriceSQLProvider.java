/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dbtools;

//import domain
import domain.Price;

import java.time.LocalDate;
import java.sql.Date;

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
public class PriceSQLProvider extends SQLProvider implements IPriceSvc{
    //logger
    private static Logger logger = LogManager.getLogger(PriceSQLProvider.class);
    
    //select param
    public Price selectPriceParam(String priceID){
        Price price = null;
        
        try{
            logger.warn("Attempting to retrieve data from database, errors may occur");
            String query = "SELECT * FROM Price WHERE priceID = ?";          //specify query to pull all records
            prepStat = con.prepareStatement(query);                     //create SQL statement
            prepStat.setString(1, priceID);
            result = prepStat.executeQuery();                           //execute the above query
            logger.info("Record Successfully Retrieved");
            
            while(result.next()) {
                price = new Price();         
                price.setPriceID(result.getString(1));
                price.setEqID(result.getString(2));
                price.setRatePerHour(result.getFloat(3));
                price.setRateAtDate(result.getDate(4).toLocalDate());
            }
        }
        catch(SQLException ex){
            logger.error(ex.getMessage());
            JOptionPane.showMessageDialog(null, "Could not retrieve record(s)", "SQL Exception", JOptionPane.ERROR_MESSAGE);
        
        }
        return price;
    }
            
    //select all
    public ArrayList<Price> selectAllPrice(){
        ArrayList<Price> list = new ArrayList<Price>();
        
        try{
            logger.warn("Attempting to retrieve data from database, errors may occur");
            stat = con.createStatement(); 
            String query = "SELECT * FROM Price";
            logger.info(query);
            result = stat.executeQuery(query);
            logger.info("Record Successfully Retrieved");
            
            while(result.next()){
                Price price = new Price();         
                price.setPriceID(result.getString(1));
                price.setEqID(result.getString(2));
                price.setRatePerHour(result.getFloat(3));
                price.setRateAtDate(result.getDate(4).toLocalDate());
                list.add(price);
            }
        }
        catch(SQLException ex){
            logger.error(ex.getMessage());
            JOptionPane.showMessageDialog(null, "Could not retrieve record(s)", "SQL Exception", JOptionPane.ERROR_MESSAGE);
        }
        return list;
    }
    
    //update
    public int updatePrice(Price obj){
        int n;
        
        try{
            logger.warn("Attempting to update data in database, errors may occur");
            stat = con.createStatement();
            String query =
                    "UPDATE Price "
                    + "SET eqID = '" + obj.getEqID() + "', "
                    + "ratePerHour = '" + obj.getRatePerHour() + "', "
                    + "rateAtDate = '" + obj.getRateAtDate() + "' "
                    + "WHERE priceID = '" + obj.getPriceID() + "' ";
            
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
    public int insertPriceParam(Price obj){
        int n;
        
        try{
            logger.warn("Attempting to insert data to database, errors may occur");
            String query = "INSERT INTO Price (priceID, eqID, ratePerHour, rateAtDate) VALUES (?, ?, ?, ?)";
            prepStat = con.prepareStatement(query);
            prepStat.setString(1, obj.getPriceID());
            prepStat.setString(2, obj.getEqID());
            prepStat.setFloat(3, obj.getRatePerHour());
            prepStat.setDate(4, java.sql.Date.valueOf(obj.getRateAtDate()));
            
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
    public int deletePrice(String priceID){
        int n;
        
        try{
            logger.warn("Attempting to delete data from database, errors may occur");
            stat = con.createStatement();
            String query = 
                    "DELETE FROM Price "
                    + "WHERE priceID = '" + priceID + "' ";
            
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
    
    //Retrieve the most recent price for a given equipment (eqID) on or before the rental date -- needed for invoice generation
    public Price getLatestPrice(String eqID, LocalDate rentalDate) {
        Price latestPrice = null;
        String query = "SELECT * FROM Price WHERE eqID = ? AND rateAtDate <= ? ORDER BY rateAtDate DESC LIMIT 1";

        try {
            prepStat = con.prepareStatement(query);
            prepStat.setString(1, eqID);
            prepStat.setDate(2, Date.valueOf(rentalDate));
            result = prepStat.executeQuery();

            if (result.next()) {
                latestPrice = new Price();
                latestPrice.setPriceID(result.getString("priceID"));
                latestPrice.setEqID(result.getString("eqID"));
                latestPrice.setRatePerHour(result.getFloat("ratePerHour"));
                latestPrice.setRateAtDate(result.getDate("rateAtDate").toLocalDate());
            }         
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return latestPrice;
    }
}
