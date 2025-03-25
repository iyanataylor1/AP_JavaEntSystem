package domain;

//import java.util.Date;
import java.time.LocalDate;

import java.io.Serializable;

/**
 *
 * @author iyana
 */
public class Report implements Serializable{
    private String reportID;
    private String reportType;
    private LocalDate dateGenerated;
    private String adminID;
    
    //does it need a def con?

    public Report(String reportID, String reportType, LocalDate dateGenerated, String adminID) {
        this.reportID = reportID;
        this.reportType = reportType;
        this.dateGenerated = dateGenerated;
        this.adminID = adminID;
    }
    
    public Report() {
        //body, if any
    }

    public String getReportID() {
        return reportID;
    }

    public void setReportID(String reportID) {
        this.reportID = reportID;
    }

    public String getReportType() {
        return reportType;
    }

    public void setReportType(String reportType) {
        this.reportType = reportType;
    }

    public LocalDate getDateGenerated() {
        return dateGenerated;
    }

    public void setDateGenerated(LocalDate dateGenerated) {
        this.dateGenerated = dateGenerated;
    }
    
    //Methods
        public void generateReportBooking() {
        // logic to generate a booking report
        System.out.println("Booking report generated for report ID: " + reportID);
    }

    public void generateRevenueReport() {
        // logic to generate a revenue report
        System.out.println("Revenue report generated for report ID: " + reportID);
    }

    public String getAdminID() {
        return adminID;
    }

    public void setAdminID(String adminID) {
        this.adminID = adminID;
    }
    
    //Methods

    @Override
    public String toString() {
        return "Report{" + "reportID=" + reportID + ", reportType=" + reportType + ", dateGenerated=" + dateGenerated + ", adminID=" + adminID + '}';
    }
    
    public Boolean isDataEntered() {
        if(this.reportID.equals("") || this.reportType.equals("")) {
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
