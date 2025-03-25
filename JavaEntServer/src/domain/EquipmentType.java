package domain;

import java.io.Serializable;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author iyana
 */
public class EquipmentType implements Serializable{ 
    private String eqTypeID;
    private String name;
    private List<Equipment> equipmentList; // Relationship

    // Constructor
    //does it need a def con?
    public EquipmentType(String eqTypeID, String name) {
        this.eqTypeID = eqTypeID;
        this.name = name;
        this.equipmentList = new ArrayList<>();
    }
    
    public EquipmentType() {
        this.equipmentList = new ArrayList<>(); // Ensure equipmentList is initialized
    }


    public String getEqTypeID() {
        return eqTypeID;
    }

    public void setEqTypeID(String eqTypeID) {
        this.eqTypeID = eqTypeID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Equipment> getEquipmentList() {
        return equipmentList;
    }

    public void setEquipmentList(List<Equipment> equipmentList) {
        this.equipmentList = equipmentList;
    }

    //Methods
    
    // Display all equipment in this type
    public void viewEquipmentList() {
        System.out.println("Equipment Type: " + name);
        for (Equipment eq : equipmentList) {
            System.out.println("- " + eq.getName());
        }
    }

    @Override
    public String toString() {
        return "EquipmentType{" + "eqTypeID=" + eqTypeID + ", name=" + name + ", equipmentList=" + equipmentList + '}';
    }
    
    /*
    public Boolean isDataEntered(){
    //use if statements to check if data is entered
    }
    */
    
    /*
    public Boolean validate(){
    //validation based on ID
    }
    */
}
