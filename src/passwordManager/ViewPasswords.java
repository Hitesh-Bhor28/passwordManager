package passwordManager;
 
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class ViewPasswords extends JFrame {
    /**
	 * 
	 */
	private static final long serialVersionUID = -4238459099802609565L;

	public ViewPasswords(int userId) {
        setTitle("View Passwords");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        String[] columnNames = {"Account", "Password"};
        DefaultTableModel model = new DefaultTableModel(columnNames, 0);
        JTable table = new JTable(model);

        List<String[]> passwords = PasswordManager.fetchPasswords(userId);
        for (String[] password : passwords) {
            model.addRow(password);
        }

        JScrollPane scrollPane = new JScrollPane(table);
        add(scrollPane, BorderLayout.CENTER);
    }
}
