//import network tool
import networktools.Client;

//import domains
import domain.Admin;

//import views
import view.ParentWindow;
import view.LoginView;

//import controllers
import controller.LoginController;

import networktools.Client;

//imports for exception handling
import java.io.IOException;


/**
 *
 * @author iyana
 */
public class Driver {
    public static void main(String[] args) {
        // Create the client instance and pass it to ParentWindow
        Client client = new Client();  
        
        //start login process
        LoginView loginView = new LoginView();
        new LoginController(loginView, client);
        loginView.setVisible(true);
    }
}
