package domain;

//import java.util.Date;
import java.time.LocalDate;

import java.io.Serializable;

/**
 *
 * @author iyana
 */
public class Booking implements Serializable{
    private String bookingID;
    private String eventID;
    private String equipmentID;
    private LocalDate deliveryDate;
    private LocalDate returnDate;
    
    //constructor
    // does it need a def con?
    public Booking(String bookingID, String eventID, String equipmentID, LocalDate deliveryDate, LocalDate returnDate) {
        this.bookingID = bookingID;
        this.eventID = eventID;
        this.equipmentID = equipmentID;
        this.deliveryDate = deliveryDate;
        this.returnDate = returnDate;
    }
    
    public Booking() {
       //body. if any
    }

    public String getBookingID() {
        return bookingID;
    }

    public void setBookingID(String bookingID) {
        this.bookingID = bookingID;
    }

    public String getEventID() {
        return eventID;
    }

    public void setEventID(String eventID) {
        this.eventID = eventID;
    }

    public String getEquipmentID() {
        return equipmentID;
    }

    public void setEquipmentID(String equipmentID) {
        this.equipmentID = equipmentID;
    }

    public LocalDate getDeliveryDate() {
        return deliveryDate;
    }

    public void setDeliveryDate(LocalDate deliveryDate) {
        this.deliveryDate = deliveryDate;
    }

    public LocalDate getReturnDate() {
        return returnDate;
    }

    public void setReturnDate(LocalDate returnDate) {
        this.returnDate = returnDate;
    }
    
    //Methods
    

    @Override
    public String toString() {
        return "Booking{" + "bookingID=" + bookingID + ", eventID=" + eventID + ", equipmentID=" + equipmentID + ", deliveryDate=" + deliveryDate + ", returnDate=" + returnDate + '}';
    }
    
    public Boolean isDataEntered() {
        if(this.bookingID.equals("") || this.eventID.equals("") || this.equipmentID.equals("") || this.deliveryDate == null || this.returnDate == null) {
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
