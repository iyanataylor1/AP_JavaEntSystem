/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package view;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;

import networktools.Client;
import view.LoginView;
import controller.LoginController;

import domain.*;
import view.*;
import controller.*;

/**
 *
 * @author iyana
 */
public class ParentWindow extends JFrame implements ActionListener{
    private JMenuBar menuBar;
    private JMenu managementMenu;
    
    private JMenuItem itemAdmin;
    private JMenuItem itemCustomer;
    private JMenuItem itemEvent;
    private JMenuItem itemEqType;
    private JMenuItem itemEq;
    private JMenuItem itemBooking;
    private JMenuItem itemInvoice;
    private JMenuItem itemPrice;
    private JMenuItem itemReport;
    
    private JMenuItem itemLogout;
    private JDesktopPane mainWindow;
    private JPanel homePanel;
    private Client client;

    public ParentWindow(Client client) {
        this.client = client;
        setTitle("Java Entertainment Event Scheduling System");
        initializeComponents();
        addComponentsToWindow();
        registerListeners();
        setWindowProperties();
    }
    
    private void initializeComponents() {
        menuBar = new JMenuBar();
        managementMenu = new JMenu("Management");
        
        itemAdmin = new JMenuItem("Admin");
        itemCustomer = new JMenuItem("Customer");
        itemEvent = new JMenuItem("Event");
        itemEqType = new JMenuItem("Equipment Type");
        itemEq = new JMenuItem("Equipment");
        itemBooking = new JMenuItem("Booking");
        itemInvoice = new JMenuItem("Invoice");
        itemPrice = new JMenuItem("Price");
        itemReport = new JMenuItem("Report");
        
        itemLogout = new JMenuItem("Logout");
        mainWindow = new JDesktopPane();
        
        // Home panel for greeting and instructions
        homePanel = new JPanel();
        homePanel.setLayout(new BorderLayout());
        homePanel.add(new JLabel("Java Entertainment Event Scheduling System", SwingConstants.CENTER), BorderLayout.CENTER);
        homePanel.add(new JLabel("Use the menu to navigate through the system.", SwingConstants.CENTER), BorderLayout.SOUTH);
    }

    private void addComponentsToWindow() {
        // Set up the menu bar
        this.setJMenuBar(this.menuBar);
        menuBar.add(managementMenu);
        
        managementMenu.add(itemAdmin);
        managementMenu.add(itemCustomer);
        managementMenu.add(itemEvent);
        managementMenu.add(itemEqType);
        managementMenu.add(itemEq);
        managementMenu.add(itemBooking);
        managementMenu.add(itemInvoice);
        managementMenu.add(itemPrice);
        managementMenu.add(itemReport);
        
        managementMenu.add(itemLogout);
        
        // Add the home panel as the initial view
        mainWindow.add(homePanel);
        this.getContentPane().add(mainWindow);
    }

    private void registerListeners() {
        // Register menu item listeners
        this.itemAdmin.addActionListener(this);
        this.itemCustomer.addActionListener(this);
        this.itemEvent.addActionListener(this);
        this.itemEqType.addActionListener(this);
        this.itemEq.addActionListener(this);
        this.itemBooking.addActionListener(this);
        this.itemInvoice.addActionListener(this);
        this.itemPrice.addActionListener(this);
        this.itemReport.addActionListener(this);
        
        this.itemLogout.addActionListener(this);
    }
    
    private void setWindowProperties() {
        this.setSize(1024, 768);
        this.setResizable(true);
        this.setVisible(true);
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
    }

    @Override
    public void actionPerformed(ActionEvent ae) {
        // If "Admin" menu item is clicked, open the view
        if (ae.getSource().equals(this.itemAdmin)) {
            openAdminView();
        } 
        // If "customer" menu item is clicked, open the view
        else if (ae.getSource().equals(this.itemCustomer)){         
            openCustomerView();
        }
        // If "event" menu item is clicked, open the view
        else if (ae.getSource().equals(this.itemEvent)){         
            openEventView();
        }
        // If "equipment type" menu item is clicked, open the view
        else if (ae.getSource().equals(this.itemEqType)){         
            openEqTypeView();
        }
        // If "equipment" menu item is clicked, open the view
        else if (ae.getSource().equals(this.itemEq)){         
            openEqView();
        }
        // If "booking" menu item is clicked, open the view
        else if (ae.getSource().equals(this.itemBooking)){         
            openBookingView();
        }
        // If "invoice" menu item is clicked, open the view
        else if (ae.getSource().equals(this.itemInvoice)){         
            openInvoiceView();
        }
        // If "price" menu item is clicked, open the view
        else if (ae.getSource().equals(this.itemPrice)){         
            openPriceView();
        }
        // If "report" menu item is clicked, open the view
        else if (ae.getSource().equals(this.itemReport)){         
            openReportView();
        }
        // If "Logout" menu item is clicked, handle logout
        else if (ae.getSource().equals(this.itemLogout)) {
            JOptionPane.showMessageDialog(this, "Logging out...");
            //System.exit(0); // Exit the program
            
            //close the current window
            this.dispose();
            
            //show the login window again
            LoginView loginView = new LoginView();
            new LoginController(loginView, client);
            loginView.setVisible(true);
        }
    }
    
    private void openAdminView() {
        // Remove home panel and load View
        mainWindow.remove(homePanel);
        
        // Create View and its controller
        AdminView adminView = new AdminView();
        new AdminController(adminView, client);
        
        // Add View to the main window (JDesktopPane)
        mainWindow.add(adminView);
        adminView.setVisible(true);
    }
    
    private void openCustomerView(){
        // Remove home panel and load View
        mainWindow.remove(homePanel);
        
        // Create View and its controller
        CustomerView customerView = new CustomerView();
        new CustomerController(customerView, client);
        
        // Add View to the main window (JDesktopPane)
        mainWindow.add(customerView);
        customerView.setVisible(true);
    }
    
    private void openEventView(){
        // Remove home panel and load View
        mainWindow.remove(homePanel);
        
        // Create View and its controller
        EventView eventView = new EventView();
        new EventController(eventView, client);
        
        // Add View to the main window (JDesktopPane)
        mainWindow.add(eventView);
        eventView.setVisible(true);
    }
    
    private void openEqTypeView(){
        // Remove home panel and load View
        mainWindow.remove(homePanel);
        
        // Create View and its controller
        EquipmentTypeView eqTypeView = new EquipmentTypeView();
        new EquipmentTypeController(eqTypeView, client);
        
        // Add View to the main window (JDesktopPane)
        mainWindow.add(eqTypeView);
        eqTypeView.setVisible(true);
    }
    
    private void openEqView(){
        // Remove home panel and load View
        mainWindow.remove(homePanel);
        
        // Create View and its controller
        EquipmentView eqView = new EquipmentView();
        new EquipmentController(eqView, client);
        
        // Add EventView to the main window (JDesktopPane)
        mainWindow.add(eqView);
        eqView.setVisible(true);
    }
    
    private void openBookingView(){
        // Remove home panel and load View
        mainWindow.remove(homePanel);
        
        // Create View and its controller
        BookingView bookingView = new BookingView();
        new BookingController(bookingView, client);
        
        // Add EventView to the main window (JDesktopPane)
        mainWindow.add(bookingView);
        bookingView.setVisible(true);
    }
    
    private void openInvoiceView(){
        // Remove home panel and load View
        mainWindow.remove(homePanel);
        
        // Create View and its controller
        InvoiceView invoiceView = new InvoiceView();
        new InvoiceController(invoiceView, client);
        
        // Add EventView to the main window (JDesktopPane)
        mainWindow.add(invoiceView);
        invoiceView.setVisible(true);
    }
    private void openPriceView(){
        // Remove home panel and load View
        mainWindow.remove(homePanel);
        
        // Create View and its controller
        PriceView priceView = new PriceView();
        new PriceController(priceView, client);
        
        // Add EventView to the main window (JDesktopPane)
        mainWindow.add(priceView);
        priceView.setVisible(true);
    }
    
    private void openReportView(){
        // Remove home panel and load View
        mainWindow.remove(homePanel);
        
        // Create View and its controller
        ReportView reportView = new ReportView();
        new ReportController(reportView, client);
        
        // Add EventView to the main window (JDesktopPane)
        mainWindow.add(reportView);
        reportView.setVisible(true);
    }
}
