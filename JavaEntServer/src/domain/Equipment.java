package domain;

import java.io.Serializable;

/**
 *
 * @author iyana
 */
public class Equipment implements Serializable{
    private String eqID;
    private EquipmentType equipmentType; // Relationship
    private String name;

    // Constructor
    //does it need a def con?
    public Equipment(String eqID, EquipmentType equipmentType, String name) {
        this.eqID = eqID;
        this.equipmentType = equipmentType;
        this.name = name;
    }
    
    public Equipment() {
           //body, if any
    }


    public String getEqID() {
        return eqID;
    }

    public void setEqID(String eqID) {
        this.eqID = eqID;
    }

    public EquipmentType getEquipmentType() {
        return equipmentType;
    }

    public void setEquipmentType(EquipmentType equipmentType) {
        this.equipmentType = equipmentType;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
    
    // Methods
    

    @Override
    public String toString() {
        return "Equipment{" + "eqID=" + eqID + ", equipmentType=" + equipmentType + ", name=" + name + '}';
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
