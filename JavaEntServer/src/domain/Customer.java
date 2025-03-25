package domain;

import java.io.Serializable;

/**
 *
 * @author iyana
 */
public class Customer implements Serializable{
    private String customerID;
    private String firstName;
    private String lastName;
    private String companyName;
    private String phone;
    private String email;
    private String adminID; //foreign key

    // Constructor
    // does it need a def con?
    public Customer() {
        this.customerID = "";
        this.firstName = "";
        this.lastName = "";
        this.companyName = "";
        this.phone = "";
        this.email = "";
        this.adminID = "";
    }
    
    public Customer(String customerID, String firstName, String lastName, String companyName, String phone, String email, String adminID) {
        this.customerID = customerID;
        this.firstName = firstName;
        this.lastName = lastName;
        this.companyName = companyName;
        this.phone = phone;
        this.email = email;
        this.adminID = adminID;
    }

    public String getCustomerID() {
        return customerID;
    }

    public void setCustomerID(String customerID) {
        this.customerID = customerID;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAdminID() {
        return adminID;
    }

    public void setAdminID(String adminID) {
        this.adminID = adminID;
    }
    
    // Methods
    

    @Override
    public String toString() {
        return "Customer{" + "customerID=" + customerID + ", firstName=" + firstName + ", lastName=" + lastName + ", companyName=" + companyName + ", phone=" + phone + ", email=" + email + ", adminID=" + adminID + '}';
    }
    
    public Boolean isDataEntered() {
        if(this.customerID.equals("") || this.firstName.equals("") || this.lastName.equals("") || this.email.equals("") || this.adminID.equals("")) {
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
