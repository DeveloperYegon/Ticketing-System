
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class CustomerList extends JPanel {

    private JTable customerTable;
    private DefaultTableModel tableModel;
    private JButton btnEdit, btnDelete;

    public  CustomerList(){
        JLabel titleLabel = new JLabel("Registered Users",JLabel.CENTER);
        titleLabel.setFont(new Font ("Arial" ,Font.BOLD,18));
        setLayout(new BorderLayout());


        // Create table model with columns
        tableModel = new DefaultTableModel();
        tableModel.addColumn("ID");
        tableModel.addColumn("Name");
        tableModel.addColumn("Phone");
        tableModel.addColumn("Email");
        tableModel.addColumn("Address");


        // Buttons for Edit & Delete
        JPanel buttonPanel = new JPanel();
        btnEdit = new JButton("Edit");
        btnDelete = new JButton("Delete");

        buttonPanel.add(btnEdit);
        buttonPanel.add(btnDelete);
        add(buttonPanel, BorderLayout.SOUTH);

        // Create JTable with model
        customerTable = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(customerTable);

        //add components to panel
        add(scrollPane, BorderLayout.CENTER);
        add(titleLabel, BorderLayout.NORTH);

        // Load customer data from database
        loadCustomers();


        // Button Actions
        btnEdit.addActionListener(e -> editCustomer());
        btnDelete.addActionListener(e -> deleteCustomer());

        setVisible(true);

    }
//display data
    private void loadCustomers() {
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT id, name, phone, email, address FROM customers")) {

            // Clear existing rows
            tableModel.setRowCount(0);

            // Populate table with database records
            while (rs.next()) {
                tableModel.addRow(new Object[]{
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getString("phone"),
                        rs.getString("email"),
                        rs.getString("address")
                });
            }

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error loading customers: " + e.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
        }
    }


    // Edit Customer
    private void editCustomer() {
        int selectedRow = customerTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select a customer to edit.");
            return;
        }

        int id = (int) tableModel.getValueAt(selectedRow, 0);
        String name = (String) tableModel.getValueAt(selectedRow, 1);
        String phone = (String) tableModel.getValueAt(selectedRow, 2);
        String email = (String) tableModel.getValueAt(selectedRow, 3);
        String address = (String) tableModel.getValueAt(selectedRow, 4);

        JTextField nameField = new JTextField(name);
        JTextField phoneField = new JTextField(phone);
        JTextField emailField = new JTextField(email);
        JTextField addressField = new JTextField(address);

        JPanel panel = new JPanel(new GridLayout(5, 2));
        panel.add(new JLabel("Name:"));
        panel.add(nameField);
        panel.add(new JLabel("Phone:"));
        panel.add(phoneField);
        panel.add(new JLabel("Email:"));
        panel.add(emailField);
        panel.add(new JLabel("Address:"));
        panel.add(addressField);

        int result = JOptionPane.showConfirmDialog(this, panel, "Edit Customer", JOptionPane.OK_CANCEL_OPTION);
        if (result == JOptionPane.OK_OPTION) {
            try (Connection conn = DatabaseConnection.getConnection()){
                String sql ="UPDATE customers SET name=?, phone=?, email=?, address=? WHERE id=?";
                 PreparedStatement pstmt = conn.prepareStatement(sql);

                pstmt.setString(1, nameField.getText());
                pstmt.setString(2, phoneField.getText());
                pstmt.setString(3, emailField.getText());
                pstmt.setString(4, addressField.getText());
                pstmt.setInt(5, id);

                int rowsUpdated = pstmt.executeUpdate();
                if (rowsUpdated > 0) {
                    JOptionPane.showMessageDialog(this, "Customer updated successfully!");
                    loadCustomers();
                }

            } catch (SQLException e) {
                JOptionPane.showMessageDialog(this, "Error updating customer: " + e.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }


    // Delete Customer
    private void deleteCustomer() {
        int selectedRow = customerTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select a customer to delete.");
            return;
        }

        int id = (int) tableModel.getValueAt(selectedRow, 0);
        int confirm = JOptionPane.showConfirmDialog(this, "Are you sure you want to delete this customer?", "Confirm Delete", JOptionPane.YES_NO_OPTION);

        if (confirm == JOptionPane.YES_OPTION) {
            try (Connection conn = DatabaseConnection.getConnection()){
                String sql= "DELETE FROM customers WHERE id=?";
                 PreparedStatement pstmt = conn.prepareStatement(sql);
                pstmt.setInt(1, id);
                int rowsDeleted = pstmt.executeUpdate();

                if (rowsDeleted > 0) {
                    JOptionPane.showMessageDialog(this, "Customer deleted successfully!");
                    loadCustomers();
                }

            } catch (SQLException e) {
                JOptionPane.showMessageDialog(this, "Error deleting customer: " + e.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}
