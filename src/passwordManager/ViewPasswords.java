package passwordManager;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class ViewPasswords extends JFrame {
    private static final long serialVersionUID = -4238459099802609565L;

    public ViewPasswords(int userId) {
        setTitle("View Passwords");
        setSize(500, 300);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        String[] columnNames = {"Account", "Password", "Strength"};
        DefaultTableModel model = new DefaultTableModel(columnNames, 0);
        JTable table = new JTable(model);

        List<String[]> passwords = PasswordManager.fetchPasswords(userId);
        for (String[] password : passwords) {
            String accountName = password[0];
            String accountPassword = password[1];

            String strength = evaluatePasswordStrength(accountPassword);

            model.addRow(new Object[]{accountName, accountPassword, strength});
        }

        JScrollPane scrollPane = new JScrollPane(table);
        add(scrollPane, BorderLayout.CENTER);

        setVisible(true);
    }

    private String evaluatePasswordStrength(String password) {
        if (password.length() < 6) {
            return "Weak";
        } else if (password.length() < 10) {
            return "Medium";
        } else {
            return "Strong";
        }
    }
}
