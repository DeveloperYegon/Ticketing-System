
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.*;
public class TicketsMade extends JPanel{
    private JTable ticketTable;
    private DefaultTableModel tableModel;
    private JButton btnEdit, btnDelete;
public TicketsMade(){
    JLabel titleLabel = new JLabel("Ticket List", JLabel.CENTER);
    titleLabel.setFont(new Font("Arial", Font.BOLD, 18));
    setLayout(new BorderLayout());
    // Table model with columns
    tableModel = new DefaultTableModel();
    tableModel.addColumn("ID");
    tableModel.addColumn("Customer Name");
    tableModel.addColumn("Issue");
    tableModel.addColumn("Priority");
    tableModel.addColumn("Status");
    tableModel.addColumn("Created At");

    // Buttons for Edit & Delete
    JPanel buttonPanel = new JPanel();
    btnEdit = new JButton("Edit");
    btnDelete = new JButton("Delete");

    buttonPanel.add(btnEdit);
    buttonPanel.add(btnDelete);
    add(buttonPanel, BorderLayout.SOUTH);

    // Create JTable with model
    ticketTable = new JTable(tableModel);
    JScrollPane scrollPane = new JScrollPane(ticketTable);

    // Add components to panel
    add(scrollPane, BorderLayout.CENTER);
    add(titleLabel, BorderLayout.NORTH);

    // Load ticket data from database
    loadTickets();

    // Button Actions
    btnEdit.addActionListener(e -> editTicket());
    btnDelete.addActionListener(e -> deleteTicket());

    setVisible(true);
}


    // Load tickets from the database
    private void loadTickets() {
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT id, customer_name, issue_category, priority, status, created_at " +
                     "FROM tickets ")) {

            // Clear existing rows
            tableModel.setRowCount(0);

            // Populate table with database records
            while (rs.next()) {
                tableModel.addRow(new Object[]{
                        rs.getInt("id"),
                        rs.getString("customer_name"),
                        rs.getString("issue_category"),
                        rs.getString("priority"),
                        rs.getString("status"),
                        rs.getString("created_at")
                });
            }

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error loading tickets: " + e.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    // Edit Ticket
    private void editTicket() {
        int selectedRow = ticketTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select a ticket to edit.");
            return;
        }

        int id = (int) tableModel.getValueAt(selectedRow, 0);
        String issueCategory = (String) tableModel.getValueAt(selectedRow, 2);
        String priority = (String) tableModel.getValueAt(selectedRow, 3);
        String status = (String) tableModel.getValueAt(selectedRow, 4);

        JTextField issueField = new JTextField(issueCategory);
        String[] priorityOptions = {"Low", "Medium", "High"};
        JComboBox<String> priorityBox = new JComboBox<>(priorityOptions);
        priorityBox.setSelectedItem(priority);
        String[] statusOptions = {"Open", "In Progress", "Closed"};
        JComboBox<String> statusBox = new JComboBox<>(statusOptions);
        statusBox.setSelectedItem(status);

        JPanel panel = new JPanel(new GridLayout(4, 2));
        panel.add(new JLabel("Issue Category:"));
        panel.add(issueField);
        panel.add(new JLabel("Priority:"));
        panel.add(priorityBox);
        panel.add(new JLabel("Status:"));
        panel.add(statusBox);

        int result = JOptionPane.showConfirmDialog(this, panel, "Edit Ticket", JOptionPane.OK_CANCEL_OPTION);
        if (result == JOptionPane.OK_OPTION) {
            try (Connection conn = DatabaseConnection.getConnection()) {
                String sql = "UPDATE tickets SET issue_category=?, priority=?, status=? WHERE id=?";
                PreparedStatement pstmt = conn.prepareStatement(sql);

                pstmt.setString(1, issueField.getText());
                pstmt.setString(2, (String) priorityBox.getSelectedItem());
                pstmt.setString(3, (String) statusBox.getSelectedItem());
                pstmt.setInt(4, id);

                int rowsUpdated = pstmt.executeUpdate();
                if (rowsUpdated > 0) {
                    JOptionPane.showMessageDialog(this, "Ticket updated successfully!");
                    loadTickets();
                }

            } catch (SQLException e) {
                JOptionPane.showMessageDialog(this, "Error updating ticket: " + e.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    // Delete Ticket
    private void deleteTicket() {
        int selectedRow = ticketTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select a ticket to delete.");
            return;
        }

        int id = (int) tableModel.getValueAt(selectedRow, 0);
        int confirm = JOptionPane.showConfirmDialog(this, "Are you sure you want to delete this ticket?", "Confirm Delete", JOptionPane.YES_NO_OPTION);

        if (confirm == JOptionPane.YES_OPTION) {
            try (Connection conn = DatabaseConnection.getConnection()) {
                String sql = "DELETE FROM tickets WHERE id=?";
                PreparedStatement pstmt = conn.prepareStatement(sql);
                pstmt.setInt(1, id);
                int rowsDeleted = pstmt.executeUpdate();

                if (rowsDeleted > 0) {
                    JOptionPane.showMessageDialog(this, "Ticket deleted successfully!");
                    loadTickets();
                }

            } catch (SQLException e) {
                JOptionPane.showMessageDialog(this, "Error deleting ticket: " + e.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

}
