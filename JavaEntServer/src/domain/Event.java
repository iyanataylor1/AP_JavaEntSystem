package domain;

import java.io.Serializable;

import java.util.ArrayList;
//import java.util.Date;
import java.util.List;

import java.time.LocalDate;

/**
 *
 * @author iyana
 */
public class Event implements Serializable{
    private String eventID;
    private String eventName;
    private String street;
    private String town;
    private String parish;
    private LocalDate startDate;
    private LocalDate endDate;
    private String customerID; // Foreign Key
    private List<Event> equipmentList; // Relationship

    // Constructor
    //does it need a def con?
    
    public Event (String eventID, String eventName, String street, String town, String parish, LocalDate startDate, LocalDate endDate, String customerID) {
        this.eventID = eventID;
        this.eventName = eventName;
        this.street = street;
        this.town = town;
        this.parish = parish;
        this.startDate = startDate;
        this.endDate = endDate;
        this.customerID = customerID;
        this.equipmentList = new ArrayList<>();
    }

    public Event() {
        this.equipmentList = new ArrayList<>(); // Ensure equipmentList is initialized
    }


    public String getEventID() {
        return eventID;
    }

    public void setEventID(String eventID) {
        this.eventID = eventID;
    }

    public String getEventName() {
        return eventName;
    }

    public void setEventName(String eventName) {
        this.eventName = eventName;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getTown() {
        return town;
    }

    public void setTown(String town) {
        this.town = town;
    }

    public String getParish() {
        return parish;
    }

    public void setParish(String parish) {
        this.parish = parish;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public String getCustomerID() {
        return customerID;
    }

    public void setCustomerID(String customerID) {
        this.customerID = customerID;
    }

    public List<Event> getEquipmentList() {
        return equipmentList;
    }

    public void setEquipmentList(List<Event> equipmentList) {
        this.equipmentList = equipmentList;
    }
    
    //Methods

    @Override
    public String toString() {
        return "Event{" + "eventID=" + eventID + ", eventName=" + eventName + ", street=" + street + ", town=" + town + ", parish=" + parish + ", startDate=" + startDate + ", endDate=" + endDate + ", customerID=" + customerID + ", equipmentList=" + equipmentList + '}';
    }
    
    public Boolean isDataEntered() {
        if(this.eventID.equals("") || this.eventName.equals("") || this.street.equals("") || this.town.equals("") || this.parish.equals("")) {
            return false;
        }
        return true;
    }
    
    /*
    public Boolean validate(){
    //validation based on ID
    }
    */
}
