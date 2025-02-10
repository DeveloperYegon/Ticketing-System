
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


public class Dashboard extends JPanel {
    private JLabel lblLogo, lblOpenTickets, lblClosedTickets, lblPendingFeedback;


public Dashboard(){
    JLabel titleLabel = new JLabel("DashBoard", JLabel.CENTER);
    titleLabel.setFont(new Font("Arial",Font.BOLD, 18));
    setLayout(new BorderLayout());


    // Load the logo
    JPanel topPanel = new JPanel();
    ImageIcon logoIcon = new ImageIcon("src/images/logo.png"); // Adjust path if necessary
    Image scaledImage = logoIcon.getImage().getScaledInstance(120, 120, Image.SCALE_SMOOTH);
    lblLogo = new JLabel(new ImageIcon(scaledImage), JLabel.CENTER);

    topPanel.add(lblLogo);

    JPanel statsPanel = new JPanel(new GridLayout(3, 1));
    lblOpenTickets = new JLabel("Open Tickets: Loading...", JLabel.CENTER);
    lblClosedTickets = new JLabel("Closed Tickets: Loading...", JLabel.CENTER);
    lblPendingFeedback = new JLabel("Pending Feedback: Loading...", JLabel.CENTER);

    lblOpenTickets.setFont(new Font("Arial", Font.BOLD, 16));
    lblClosedTickets.setFont(new Font("Arial", Font.BOLD, 16));
    lblPendingFeedback.setFont(new Font("Arial", Font.BOLD, 16));

    statsPanel.add(lblOpenTickets);
    statsPanel.add(lblClosedTickets);
    statsPanel.add(lblPendingFeedback);

    add(topPanel, BorderLayout.NORTH);
    add(titleLabel,BorderLayout.CENTER);
    add(statsPanel, BorderLayout.SOUTH);

    loadTicketStatistics();

    setVisible(true);

}


    private void loadTicketStatistics() {
        try (Connection conn = DatabaseConnection.getConnection()) {
            lblOpenTickets.setText("Open Tickets: " + getCount(conn, "Open"));
            lblClosedTickets.setText("Closed Tickets: " + getCount(conn, "Closed"));
            lblPendingFeedback.setText("Pending Feedback: " + getCount(conn, "Pending Feedback"));
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Database Error: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }


    private int getCount(Connection conn, String status) throws SQLException {
        String query = "SELECT COUNT(*) FROM tickets WHERE status = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setString(1, status);
            ResultSet rs = pstmt.executeQuery();
            return rs.next() ? rs.getInt(1) : 0;
        }
    }
}

