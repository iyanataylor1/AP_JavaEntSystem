package networktools;

// imports for exception handling
import java.io.EOFException;
import java.io.IOException;

// imports for network connection
import java.net.ServerSocket;
import java.net.Socket;

//imports for logging
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

//import heavyweights for system tray icon
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import domain.*;
import dbtools.*;

/**
 *
 * @author iyana
 */
public class Server {
    //declare attributes
    private ServerSocket servSock;
    private Socket connection;
    
    private TrayIcon trayIcon;
    
    private Logger logger = LogManager.getLogger(Server.class);
    
    //constructor
    public Server() {
        /*System.out.println("Server starting...");

        // Temporary test
        String testEventID = "E001"; // Replace with a valid event ID from your database
        domain.Event testEvent = new EventSQLProvider().selectEventParam(testEventID); // Fetch event from DB
        
        if (testEvent != null) {
            testEvent.viewEventEquipment(); // Call the method and print equipment
        } else {
            System.out.println("Event not found.");
        }*/
        
        this.createConnection();
        this.waitForRequests();
    }
    
    //create connection method (remember try/catch)
    public void createConnection() {
        try {
            System.out.println("Attempting to setup Server Socket, Errors may Occur");                  //for debugging, i need to see it in the console
            logger.warn("Attempting to setup Server Socket, Errors may Occur");
            this.servSock = new ServerSocket(59999, 50);             //exp: backlog size; determines the maximum number of pending connection requests that the operating system can queue up
            
            setupTrayIcon(); // Add system tray icon
            
            System.out.println("Server socket established");
            logger.info("Server Socket Successfully Configured");
        }
        catch(IOException ex) {
            logger.error(ex.getMessage());
        }
    }
    
    private void setupTrayIcon() {
        if (!SystemTray.isSupported()) {
            System.out.println("System tray not supported.");
            return;
        }

        SystemTray tray = SystemTray.getSystemTray();
        Image iconImage = Toolkit.getDefaultToolkit().getImage(getClass().getClassLoader().getResource("main/resources/server.png"));
        
        PopupMenu menu = new PopupMenu();
        MenuItem exitItem = new MenuItem("Stop Server");

        exitItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                stopServer();
            }
        });

        menu.add(exitItem);
        trayIcon = new TrayIcon(iconImage, "Server Running", menu);
        trayIcon.setImageAutoSize(true);

        try {
            tray.add(trayIcon);
        } catch (AWTException e) {
            e.printStackTrace();
        }
    }

    private void stopServer() {
        try {
            if (servSock != null) {
                servSock.close();
                System.out.println("Server stopped.");
            }
            System.exit(0);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    //wait for requests (remeber try/catch) -- change this when i'm done
    public void waitForRequests() {
        try {
            while (true) {
                logger.info("Server waiting for connections...");
                connection = servSock.accept();
                logger.info("Client request accepted"  + connection.getInetAddress());
                new ServerEngine(connection);
            }
        } catch (EOFException ex) {
            logger.info("Client has terminated connection with the server");
        } catch (IOException ex) {
            logger.error(ex.getMessage());
        }
    }
}

