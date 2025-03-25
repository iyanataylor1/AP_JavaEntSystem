/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dbtools;

//imports for SQL
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

//imports for logging
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 *
 * @author iyana
 */
public abstract class SQLProvider {
    //declarations
    protected Connection con = null;	//connection object to database
    protected Statement stat = null;	//use to execute sql statement
    protected ResultSet result = null;	//used to collect result data from database
    protected PreparedStatement prepStat = null;
    private static final String DRIVER = "com.mysql.cj.jdbc.Driver";
	    
    //logger
    private static Logger logger = LogManager.getLogger(SQLProvider.class);
            
    //constructor
    public SQLProvider() {
	try {
            logger.warn("Attempting to connect to database, errors may occur");
	    Class.forName(DRIVER).newInstance();	//specify driver class
	    String url = "jdbc:mysql://localhost:3306/javaent_db";	//specify database path / url
            con = DriverManager.getConnection(url, "root","root");	//instantiate connection object via driver manager
            logger.info("Connected to database");		
	}
	catch(SQLException ex){
            ex.printStackTrace();
            logger.error(ex.getMessage());
	}
	catch (ClassNotFoundException ex){
            ex.printStackTrace();
            logger.error(ex.getMessage());
	}
	catch(NullPointerException ex){
            ex.printStackTrace();
            logger.error(ex.getMessage());
	}
	catch(IllegalAccessException ex){
            ex.printStackTrace();
            logger.error(ex.getMessage());
	}
	catch(InstantiationException ex){
            ex.printStackTrace();
            logger.error(ex.getMessage());
	}    
    }
}
