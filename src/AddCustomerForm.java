
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;


public class AddCustomerForm  extends JPanel{

    private JTextField nameField, phoneField, emailField;
    private JTextArea addressArea;
    private JButton submitButton, clearButton;

    public AddCustomerForm(){
        JLabel titleLabel = new JLabel("Add Customer", JLabel.CENTER);
        titleLabel.setFont(new Font("Arial",Font.BOLD, 18));
        setLayout(new BorderLayout());


        JPanel formPanel = new JPanel(new GridLayout(6, 2, 10, 10));
        formPanel.add(new JLabel("Name:"));
        nameField = new JTextField();
        formPanel.add(nameField);


        formPanel.add(new JLabel("Phone:"));
        phoneField = new JTextField();
        formPanel.add(phoneField);

        formPanel.add( new JLabel("Email:"));
        emailField = new JTextField();
        formPanel.add(emailField);

        formPanel.add(new JLabel("Address:"));
        addressArea = new JTextArea(3, 20);
        formPanel.add(addressArea);



        JPanel buttonPanel = new JPanel();
        // Buttons
        JButton submitButton = new JButton("Submit");
         JButton clearButton = new JButton("Clear");

         buttonPanel.add(submitButton);
         buttonPanel.add(clearButton);

         add(titleLabel,BorderLayout.NORTH);
         add(formPanel,BorderLayout.CENTER);
         add(buttonPanel,BorderLayout.SOUTH);


        // Add event listener to submit button
        submitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                addCustomer();
            }
        });

        clearButton.addActionListener(e -> clearFields());

        setVisible(true);

    }
    private void addCustomer() {
        String name = nameField.getText().trim();
        String phone = phoneField.getText().trim();
        String email = emailField.getText().trim();
        String address = addressArea.getText().trim();

        if (name.isEmpty() || phone.isEmpty() || email.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Name, Phone, and Email are required!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try (Connection conn = DatabaseConnection.getConnection()){

            String sql ="INSERT INTO customers (name, phone, email, address) VALUES (?, ?, ?, ?)";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, name);
            stmt.setString(2, phone);
            stmt.setString(3, email);
            stmt.setString(4, address);
            stmt.executeUpdate();
            JOptionPane.showMessageDialog(this, "Customer added successfully!");
            clearFields();
        }catch(SQLException ex){

            JOptionPane.showMessageDialog(this, "Error saving data: " + ex.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
        }




    }

    private void clearFields() {
        nameField.setText("");
        phoneField.setText("");
        emailField.setText("");
        addressArea.setText("");
    }
}
