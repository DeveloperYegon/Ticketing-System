
import javax.swing.*;
import java.awt.event.*;

public class Main extends JFrame{

    public Main(){
        setTitle("Customer Service Ticketing System");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);


        // Initialize Menu Bar
        JMenuBar menuBar = new JMenuBar();

        // File Menu
        JMenu fileMenu = new JMenu("File");
        JMenuItem dashboardMenuItem = new JMenuItem("Dashboard");
        fileMenu.add(dashboardMenuItem);
        dashboardMenuItem.addActionListener( e->showDashboard());
        JMenuItem exitMenuItem = new JMenuItem("Exit");
        fileMenu.add(exitMenuItem);

        // Tickets Menu
        JMenu ticketsMenu = new JMenu("Tickets");
        JMenuItem createTicketMenuItem = new JMenuItem("Create New Ticket");
        ticketsMenu.add(createTicketMenuItem);
        createTicketMenuItem.addActionListener(e -> showCreateTicket());
        ticketsMenu.add(new JMenuItem("View Open Tickets"));
        ticketsMenu.add(new JMenuItem("Search Tickets"));

        // Customers Menu
        JMenu customersMenu = new JMenu("Customers");
        JMenuItem  addNewCustomer =new JMenuItem("Add New Customer");
        customersMenu.add(addNewCustomer);
        addNewCustomer.addActionListener(e->showAddCustomer());

        JMenuItem viewCustomers = new JMenuItem("View Customer List");
        customersMenu.add(viewCustomers);
        viewCustomers.addActionListener(e->showViewCustomer());

        // Reports Menu
        JMenu reportsMenu = new JMenu("Reports");
        reportsMenu.add(new JMenuItem("Generate Report"));

        // Help Menu
        JMenu helpMenu = new JMenu("Help");
        helpMenu.add(new JMenuItem("User Guide"));
        helpMenu.add(new JMenuItem("About"));

        // Settings Menu
        JMenu settingsMenu = new JMenu("Settings");

        // Add Menus to Menu Bar
        menuBar.add(fileMenu);
        menuBar.add(ticketsMenu);
        menuBar.add(customersMenu);
        menuBar.add(reportsMenu);
        menuBar.add(helpMenu);
        menuBar.add(settingsMenu);

        setJMenuBar(menuBar);

        // Action Listeners
        exitMenuItem.addActionListener(e -> System.exit(0));
        dashboardMenuItem.addActionListener(e -> showDashboard());

        // Default Panel
        showDashboard();

    }

    // Method to Show Dashboard Panel
    private void showDashboard() {
        getContentPane().removeAll();
        getContentPane().add(new Dashboard());
        revalidate();
        repaint();
    }


    private void showCreateTicket() {
        getContentPane().removeAll();
        getContentPane().add(new CreateNewTicketPanel());
        revalidate();
        repaint();
    }

    private void showAddCustomer(){
        getContentPane().removeAll();
        getContentPane().add(new AddCustomerForm());
        revalidate();
        repaint();
    }
    private void showViewCustomer(){
        getContentPane().removeAll();
        getContentPane().add(new CustomerList());
        revalidate();
        repaint();
    }


    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new Main().setVisible(true);
        });
    }
}
