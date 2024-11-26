package passwordManager;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class Main extends JFrame {
    /**
	 * 
	 */
	private static final long serialVersionUID = 3738708204717539251L;
	private int currentUserId;

    public Main() {
        setTitle("Password Manager");
        setSize(500, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new CardLayout());

        // Panels
        JPanel loginPanel = new JPanel(new GridLayout(3, 2, 10, 10));
        JPanel mainPanel = new JPanel(new BorderLayout());

        // Login Components
        JTextField usernameField = new JTextField();
        JPasswordField passwordField = new JPasswordField();
        JButton loginButton = new JButton("Login");
        JButton registerButton = new JButton("Register");
        loginPanel.add(new JLabel("Username:"));
        loginPanel.add(usernameField);
        loginPanel.add(new JLabel("Password:"));
        loginPanel.add(passwordField);
        loginPanel.add(loginButton);
        loginPanel.add(registerButton);

        // Main Panel Components
        JPanel buttonPanel = new JPanel(new FlowLayout());
        JButton addPasswordButton = new JButton("Add Password");
        JButton viewPasswordsButton = new JButton("View Passwords");
        JButton logoutButton = new JButton("Logout");
        buttonPanel.add(addPasswordButton);
        buttonPanel.add(viewPasswordsButton);
        buttonPanel.add(logoutButton);

        // Table for displaying passwords
        String[] columnNames = {"Account", "Password"};
        DefaultTableModel model = new DefaultTableModel(columnNames, 0);
        JTable table = new JTable(model);
        JScrollPane scrollPane = new JScrollPane(table);

        mainPanel.add(buttonPanel, BorderLayout.NORTH);
        mainPanel.add(scrollPane, BorderLayout.CENTER);

        // Add Panels
        add(loginPanel, "Login");
        add(mainPanel, "Main");

        // Action Listeners
        loginButton.addActionListener(e -> {
            String username = usernameField.getText();
            String password = new String(passwordField.getPassword());
            currentUserId = UserManager.login(username, password);
            if (currentUserId != -1) {
                JOptionPane.showMessageDialog(this, "Login Successful!");
                ((CardLayout) getContentPane().getLayout()).show(getContentPane(), "Main");
            } else {
                JOptionPane.showMessageDialog(this, "Invalid Credentials!");
            }
        });

        registerButton.addActionListener(e -> {
            String username = usernameField.getText().trim();
            String password = new String(passwordField.getPassword()).trim();

            if (username.isEmpty() || password.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Username or Password cannot be blank!", "Error", JOptionPane.ERROR_MESSAGE);
            } else {
                if (UserManager.register(username, password)) {
                    JOptionPane.showMessageDialog(this, "Registration Successful!");
                } else {
                    JOptionPane.showMessageDialog(this, "Registration Failed! Username might already exist.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });


        addPasswordButton.addActionListener(e -> {
            String accountName = JOptionPane.showInputDialog(this, "Enter Account Name:");
            String accountPassword = JOptionPane.showInputDialog(this, "Enter Account Password:");
            if (PasswordManager.addPassword(currentUserId, accountName, accountPassword)) {
                JOptionPane.showMessageDialog(this, "Password Added Successfully!");
            } else {
                JOptionPane.showMessageDialog(this, "Failed to Add Password!");
            }
        });

        viewPasswordsButton.addActionListener(e -> {
            // Clear existing rows in the table
            model.setRowCount(0);

            // Fetch and display passwords
            List<String[]> passwords = PasswordManager.fetchPasswords(currentUserId);
            for (String[] password : passwords) {
                model.addRow(password);
            }
        });
        
        logoutButton.addActionListener(e -> {
            // Logout action: clear the user ID and show login screen
            currentUserId = -1;
            JOptionPane.showMessageDialog(this, "Logged out successfully!");
            ((CardLayout) getContentPane().getLayout()).show(getContentPane(), "Login");
        });
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new Main().setVisible(true));
    }
}
