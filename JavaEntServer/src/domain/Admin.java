package domain;

import java.util.ArrayList;
import java.util.List;

import java.io.Serializable;

/**
 *
 * @author iyana
 */
public class Admin implements Serializable{

    private String adminID;
    private String username;
    private String password;
    private List<Customer> managedCustomers;

    // Constructor
    //does it need a def con?
    public Admin() {
        this.adminID = "";
        this.username = "";
        this.password = "";
    }
    
    public Admin(String adminID, String username, String password) {
        this.adminID = adminID;
        this.username = username;
        this.password = password;
        this.managedCustomers = new ArrayList<>();
    }

    public String getAdminID() {
        return adminID;
    }

    public void setAdminID(String adminID) {
        this.adminID = adminID;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public List<Customer> getManagedCustomers() {
        return managedCustomers;
    }

    public void setManagedCustomers(List<Customer> managedCustomers) {
        this.managedCustomers = managedCustomers;
    }
    
    // Methods

    @Override
    public String toString() {
        return "Admin{" + "adminID=" + adminID + ", username=" + username + ", password=" + password + '}';
    }

    public Boolean isDataEntered() {
        if(this.adminID.equals("") || this.username.equals("") || this.password.equals("")) {
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
