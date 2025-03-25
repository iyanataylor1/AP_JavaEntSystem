package networktools;

//import domains
import domain.*;

//imports for exception handling
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

//imports for network connection
import java.net.Socket;
import java.net.UnknownHostException;
import java.net.InetAddress;

import java.util.List;

/**
 *
 * @author iyana
 */

public class Client {
    //attributes
    private ObjectInputStream is;
    private ObjectOutputStream os;
    private Socket connection;
    
    //constructor
    public Client() {
        this.createConnection();
        this.getStreams();
    }
    
    //create connection method
    public void createConnection() {
        try {
            this.connection = new Socket(InetAddress.getLocalHost(), 59999);
            System.out.println("Client Connection Established with Server");
        }
        catch(UnknownHostException ex) {
            ex.printStackTrace();
        }
        catch(IOException ex) {
            ex.printStackTrace();
        }
    }
    
    //get streams method
    public void getStreams() {
        try {
            this.os = new ObjectOutputStream(connection.getOutputStream());
            this.is = new ObjectInputStream(connection.getInputStream());
            System.out.println("Client Streams Established with Server Streams");
        }
        catch(IOException ex) {
            ex.printStackTrace();;
        }
    }
    
    //close streams method
    public void closeStreams() {
        try {
            os.close();
            is.close();
            connection.close();
            System.out.println("Client ends connection with server");
        }
        catch(IOException ex) {
            ex.printStackTrace();
        }
    }
    
    //send a string action to the server (they're all defined in the server)
    public void sendAction(String action) {
        try {
            os.writeObject(action);
            os.flush();
            System.out.println("Client sends action to server: " + action);
        }
        catch(IOException ex) {
            ex.printStackTrace();
        }
    }
    
    //generic method to send any domain object (admin, customer etc)- works with insert, update and delete, select
    public void sendObject(Object obj){
        try{
            os.writeObject(obj);
            os.flush();
            System.out.println("Client sends object to server: " + obj.getClass().getSimpleName());
        }
        catch(IOException ex){
            ex.printStackTrace();
        }
    }
    
    //receive an integer response 
    public int receiveResponseInt(){
        try{
            Integer num = (Integer)is.readObject();
            System.out.println("Client receives integer from server: " + num);
            return num;
        }
        catch(IOException | ClassNotFoundException | ClassCastException ex){
            ex.printStackTrace();
        }
        return 0;
    }
    
    //receive a boolean response 
    public boolean receiveResponseBoolean(){
        try{
            boolean flag = (Boolean)is.readObject();
            System.out.println("Client receives boolean from server: " + flag);
            return flag;
        }
        catch(IOException | ClassNotFoundException | ClassCastException ex){
            ex.printStackTrace();
        }
        return false;
    }
    
    //receive a list of objects (admin, customers etc) 
    public <T> List<T> receiveResponseList(){
        try{
           List<T> objects = (List<T>)is.readObject();
           System.out.println("Client receives list from server: " + objects.getClass().getSimpleName());
           return objects;
        }
        catch(IOException | ClassNotFoundException | ClassCastException ex){
            ex.printStackTrace();
        }
        return null;
    }
    
    //receive a single object from the server 
    public <T> T receiveResponseObject(){
        try{
            T obj = (T)is.readObject();
            System.out.println("Client receives object from server: " + obj.getClass().getSimpleName());
            return obj;
        }
        catch(ClassCastException | ClassNotFoundException | IOException ex){
            ex.printStackTrace();
        }
        return null;
    }
}
