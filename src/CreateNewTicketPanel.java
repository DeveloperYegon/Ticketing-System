
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;


public class CreateNewTicketPanel extends JPanel {

    private JTextField customerNameField, contactField;
    private JComboBox<String> issueCategoryBox, priorityBox,statusBox;
    private JTextArea descriptionArea;


    public CreateNewTicketPanel(){
    setLayout(new BorderLayout());
    JLabel titleLabel = new JLabel("Create New Ticket", JLabel.CENTER);
    titleLabel.setFont(new Font("Arial", Font.BOLD, 18));


    JPanel formPanel = new JPanel(new GridLayout(6, 2, 10, 10));
        formPanel.add(new JLabel("Customer Name:"));
        customerNameField = new JTextField();
        formPanel.add(customerNameField);

        formPanel.add(new JLabel("Contact:"));
        contactField = new JTextField();
        formPanel.add(contactField);

        formPanel.add(new JLabel("Issue Category:"));
        issueCategoryBox = new JComboBox<>(new String[]{"Billing", "Technical", "Service", "Other"});
        formPanel.add(issueCategoryBox);

        formPanel.add(new JLabel("Priority Level:"));
        priorityBox = new JComboBox<>(new String[]{"Low", "Medium", "High", "Critical"});
        formPanel.add(priorityBox);

        formPanel.add(new JLabel("Description:"));
        descriptionArea = new JTextArea(3, 20);
        JScrollPane scrollPane = new JScrollPane(descriptionArea);
        formPanel.add(scrollPane);

        formPanel.add(new JLabel("Status"));
        statusBox = new JComboBox<>(new String[]{"Open", "Closed", "Pending Feedback", "In Progress"});
        formPanel.add(statusBox);




        JPanel buttonPanel = new JPanel();
        JButton submitButton = new JButton("Submit");
        JButton cancelButton = new JButton("Cancel");

        buttonPanel.add(submitButton);
        buttonPanel.add(cancelButton);

        add(titleLabel, BorderLayout.NORTH);
        add(formPanel, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);


    submitButton.addActionListener(new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
                handleSubmit();
        }
    });

    cancelButton.addActionListener(e->clearForm());
    }



    private void  handleSubmit(){
        String customerName = customerNameField.getText().trim();
        String contact = contactField.getText().trim();
        String issueCategory = (String) issueCategoryBox.getSelectedItem();
        String priority = (String) priorityBox.getSelectedItem();
        String status = (String) statusBox.getSelectedItem();
        String description = descriptionArea.getText().trim();

        if (customerName.isEmpty() || contact.isEmpty() || description.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please fill in all required fields.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try (Connection conn = DatabaseConnection.getConnection()) {
            String sql = "INSERT INTO tickets (customer_name, contact, issue_category,status, priority, description) VALUES (?,?, ?, ?, ?, ?)";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, customerName);
            stmt.setString(2, contact);
            stmt.setString(3, issueCategory);
            stmt.setString(4, priority);
            stmt.setString(5, description);
            stmt.setString(6, status);
            stmt.executeUpdate();

            JOptionPane.showMessageDialog(this, "Ticket Created Successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
            clearForm();
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error saving ticket to database!", "Database Error"  , JOptionPane.ERROR_MESSAGE);
        }

    }

    private void clearForm() {
        customerNameField.setText("");
        contactField.setText("");
        issueCategoryBox.setSelectedIndex(0);
        priorityBox.setSelectedIndex(0);
        statusBox.setSelectedIndex(0);
        descriptionArea.setText("");
    }

}
