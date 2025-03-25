package domain;

//import java.util.Date;
import java.time.LocalDate;

import java.io.Serializable;

/**
 *
 * @author iyana
 */
public class Price implements Serializable{
    private String priceID;
    private String eqID;
    private float ratePerHour;
    private LocalDate rateAtDate;
    
    //does it need a def con?

    public Price(String priceID, String eqID, float ratePerHour, LocalDate rateAtDate) {
        this.priceID = priceID;
        this.eqID = eqID;
        this.ratePerHour = ratePerHour;
        this.rateAtDate = rateAtDate;
    }
    
    public Price() {
        //body, if any
    }

    public String getPriceID() {
        return priceID;
    }

    public void setPriceID(String priceID) {
        this.priceID = priceID;
    }

    public String getEqID() {
        return eqID;
    }

    public void setEqID(String eqID) {
        this.eqID = eqID;
    }

    public float getRatePerHour() {
        return ratePerHour;
    }

    public void setRatePerHour(float ratePerHour) {
        this.ratePerHour = ratePerHour;
    }

    public LocalDate getRateAtDate() {
        return rateAtDate;
    }

    public void setRateAtDate(LocalDate rateAtDate) {
        this.rateAtDate = rateAtDate;
    }
    
    //Methods
    

    @Override
    public String toString() {
        return "Price{" + "priceID=" + priceID + ", eqID=" + eqID + ", ratePerHour=" + ratePerHour + ", rateAtDate=" + rateAtDate + '}';
    }
    
    public Boolean isDataEntered() {
        if(this.priceID.equals("") || this.eqID.equals("") || this.rateAtDate == null) {
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
