package domain;

//import java.util.Date;
import java.time.LocalDate;

import java.io.Serializable;

/**
 *
 * @author iyana
 */
public class Invoice implements Serializable{
    private String invoiceID;
    private String eventID;
    private String customerID;
    private double totalAmount;
    private LocalDate issueDate;
    private String invoiceStatus;

    public Invoice(String invoiceID, String eventID, String customerID, double totalAmount, LocalDate issueDate, String invoiceStatus) {
        this.invoiceID = invoiceID;
        this.eventID = eventID;
        this.customerID = customerID;
        this.totalAmount = totalAmount;
        this.issueDate = issueDate;
        this.invoiceStatus = invoiceStatus;
    }
    
    public Invoice() {
        //body, if any
    }

    public String getInvoiceID() {
        return invoiceID;
    }

    public void setInvoiceID(String invoiceID) {
        this.invoiceID = invoiceID;
    }

    public String getEventID() {
        return eventID;
    }

    public void setEventID(String eventID) {
        this.eventID = eventID;
    }

    public String getCustomerID() {
        return customerID;
    }

    public void setCustomerID(String customerID) {
        this.customerID = customerID;
    }

    public double getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(double totalAmount) {
        this.totalAmount = totalAmount;
    }

    public LocalDate getIssueDate() {
        return issueDate;
    }

    public void setIssueDate(LocalDate issueDate) {
        this.issueDate = issueDate;
    }

    public String getInvoiceStatus() {
        return invoiceStatus;
    }

    public void setInvoiceStatus(String invoiceStatus) {
        this.invoiceStatus = invoiceStatus;
    }
    
    //Methods
    

    @Override
    public String toString() {
        return "Invoice{" + "invoiceID=" + invoiceID + ", eventID=" + eventID + ", customerID=" + customerID + ", totalAmount=" + totalAmount + ", issueDate=" + issueDate + ", invoiceStatus=" + invoiceStatus + '}';
    }
    
    public Boolean isDataEntered() {
        if(this.invoiceID.equals("") || this.eventID.equals("") || this.customerID.equals("") || this.invoiceStatus.equals("")) {
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
