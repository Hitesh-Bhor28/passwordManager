package passwordManager;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

@SuppressWarnings("unused")
public class Main extends JFrame {
    private static final long serialVersionUID = 1L;
    private int currentUserId;

    public Main() {
        setTitle("Password Manager");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new CardLayout());
        setSize(500, 400);
        setLocationRelativeTo(null);

        JPanel loginPanel = new JPanel(new GridBagLayout());
        JPanel mainPanel = new JPanel(new BorderLayout());
        

        // Login Panel Components
        JTextField usernameField = new JTextField(15);
        JPasswordField passwordField = new JPasswordField(15);
        JButton loginButton = new JButton("Login");
        JButton registerButton = new JButton("Register");

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);

        loginPanel.add(new JLabel("Username:"), gbc);
        gbc.gridx = 1;
        loginPanel.add(usernameField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        loginPanel.add(new JLabel("Password:"), gbc);
        gbc.gridx = 1;
        loginPanel.add(passwordField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        loginPanel.add(loginButton, gbc);
        gbc.gridx = 1;
        loginPanel.add(registerButton, gbc);

        JPanel buttonPanel = new JPanel(new FlowLayout());
        JButton addPasswordButton = new JButton("Add Password");
        JButton viewPasswordsButton = new JButton("View Passwords");
        JButton strongPasswordButton = new JButton("Generate Strong Password");
        JButton logoutButton = new JButton("Logout");
        
        

        buttonPanel.add(addPasswordButton);
        buttonPanel.add(viewPasswordsButton);
        buttonPanel.add(strongPasswordButton);
        buttonPanel.add(logoutButton);

       
        
        String[] columnNames = {"Account", "Password", "Strength"};
        DefaultTableModel model = new DefaultTableModel(columnNames, 0);

        
        JTable table = new JTable(model);
        JScrollPane scrollPane = new JScrollPane(table);

        mainPanel.add(buttonPanel, BorderLayout.NORTH);
        mainPanel.add(scrollPane, BorderLayout.CENTER);

        add(loginPanel, "Login");
        add(mainPanel, "Main");

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
            String accountPassword = JOptionPane.showInputDialog(this, "Enter Password:");

            if (PasswordManager.addPassword(currentUserId, accountName, accountPassword)) {
                JOptionPane.showMessageDialog(this, "Password Added Successfully!");
            } else {
                JOptionPane.showMessageDialog(this, "Failed to Add Password!");
            }
        });

        viewPasswordsButton.addActionListener(e -> {
            model.setRowCount(0); // Clear table

            // Fetch and display passwords
            List<String[]> passwords = PasswordManager.fetchPasswords(currentUserId);
            for (String[] password : passwords) {
                // Evaluate password strength and add to the table
                String strength = evaluatePasswordStrength(password[1]);

                // Debug logging (optional)
                System.out.println("Account: " + password[0] + ", Password: " + password[1] + ", Strength: " + strength);

                // Add a row with account name, password, and strength
                model.addRow(new Object[]{password[0], password[1], strength});
            }

            // Ensure the table is updated properly
            table.revalidate();
            table.repaint();
        });


        
        
        
        
        strongPasswordButton.addActionListener(e -> {
            int selectedRow = table.getSelectedRow();
            if (selectedRow == -1) {
                JOptionPane.showMessageDialog(this, "Please select a password to update.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            String accountName = (String) model.getValueAt(selectedRow, 0); // Get account name
            String currentPassword = (String) model.getValueAt(selectedRow, 1); // Get current password

            // Evaluate current password strength
            String strength = evaluatePasswordStrength(currentPassword);
            if (!strength.equals("Weak")) {
                JOptionPane.showMessageDialog(this, "The selected password is not weak.", "Info", JOptionPane.INFORMATION_MESSAGE);
                return;
            }

            // Generate a strong password
            String strongPassword = generateStrongPassword();

            // Evaluate the strength of the newly generated strong password
            String newPasswordStrength = evaluatePasswordStrength(strongPassword); // This should return "Strong"

            // Update password in the database
            int confirm = JOptionPane.showConfirmDialog(this, 
                "Update to the strong password: " + strongPassword + "?", 
                "Confirm Update", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                if (PasswordManager.updatePassword(currentUserId, accountName, strongPassword)) {
                    // Update the table with the new password and strength
                    model.setValueAt(strongPassword, selectedRow, 1);  // Update the password column
                    model.setValueAt(newPasswordStrength, selectedRow, 2);  // Update the strength column with "Strong"
                    JOptionPane.showMessageDialog(this, "Password updated successfully!");
                } else {
                    JOptionPane.showMessageDialog(this, "Failed to update password!", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });



        logoutButton.addActionListener(e -> {
            currentUserId = -1;
            JOptionPane.showMessageDialog(this, "Logged out successfully!");
            ((CardLayout) getContentPane().getLayout()).show(getContentPane(), "Login");
        });
    }
    
    private String evaluatePasswordStrength(String password) {
        if (password == null || password.isEmpty()) {
            return "Weak";
        }
        int length = password.length();
        boolean hasUpperCase = password.chars().anyMatch(Character::isUpperCase);
        boolean hasLowerCase = password.chars().anyMatch(Character::isLowerCase);
        boolean hasDigit = password.chars().anyMatch(Character::isDigit);
        boolean hasSpecialChar = password.chars().anyMatch(ch -> "!@#$%^&*()_+[]{}|;:,.<>?".indexOf(ch) >= 0);

        if (length >= 10 && hasUpperCase && hasLowerCase && hasDigit && hasSpecialChar) {
            return "Strong";
        } else if (length >= 6 && ((hasUpperCase && hasLowerCase) || hasDigit)) {
            return "Medium";
        } else {
            return "Weak";
        }
    }
    
    private String generateStrongPassword() {
        String upperCaseLetters = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        String lowerCaseLetters = "abcdefghijklmnopqrstuvwxyz";
        String digits = "0123456789";
        String specialChars = "!@#$%^&*()_+[]{}|;:,.<>?";
        String allChars = upperCaseLetters + lowerCaseLetters + digits + specialChars;

        StringBuilder password = new StringBuilder();
        java.util.Random random = new java.util.Random();

        // Add at least one of each type
        password.append(upperCaseLetters.charAt(random.nextInt(upperCaseLetters.length())));
        password.append(lowerCaseLetters.charAt(random.nextInt(lowerCaseLetters.length())));
        password.append(digits.charAt(random.nextInt(digits.length())));
        password.append(specialChars.charAt(random.nextInt(specialChars.length())));

        // Fill the remaining length with random characters
        for (int i = 4; i < 12; i++) {  // Length of 12 characters
            password.append(allChars.charAt(random.nextInt(allChars.length())));
        }

        // Shuffle the password for randomness
        return new String(password);
    }


    

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new Main().setVisible(true));
    }
}
