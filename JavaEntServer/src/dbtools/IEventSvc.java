/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package dbtools;

//import domain
import domain.Event;

//import arraylist
import java.util.ArrayList;

//Interface defining CRUD operations for the imported domain.

/**
 *
 * @author iyana
 */
public interface IEventSvc {
    
    
    // Select by any other parameter (e.g., email or phone number)
    public Event selectEventParam(String eventID);
    
    // Select all
    public ArrayList<Event> selectAllEvent();
    
    // Update an existing record
    public int updateEvent(Event obj);
    
    
    
    // Insert a new using parameters (perhaps for a different method of insertion)
    public int insertEventParam(Event obj);
    
    // Delete by their unique identifier
    public int deleteEvent(String eventID);
}
