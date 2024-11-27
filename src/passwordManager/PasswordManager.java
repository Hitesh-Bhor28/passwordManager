package passwordManager;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class PasswordManager {
	
	public static boolean updatePassword(int userId, String accountName, String newPassword) {
	    String query = "UPDATE passwords SET account_password = ? WHERE user_id = ? AND account_name = ?";
	    try (Connection conn = Database.connect();
	         PreparedStatement stmt = conn.prepareStatement(query)) {
	        stmt.setString(1, newPassword);
	        stmt.setInt(2, userId);
	        stmt.setString(3, accountName);

	        int rowsUpdated = stmt.executeUpdate();
	        return rowsUpdated > 0;
	    } catch (SQLException e) {
	        e.printStackTrace();
	        return false;
	    }
	}


    public static boolean addPassword(int userId, String accountName, String accountPassword) {
        if (accountName == null || accountPassword == null) return false;
        String query = "INSERT INTO passwords (user_id, account_name, account_password) VALUES (?, ?, ?)";
        try (Connection conn = Database.connect();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, userId);
            stmt.setString(2, accountName);
            stmt.setString(3, accountPassword);
            stmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static List<String[]> fetchPasswords(int userId) {
        List<String[]> passwords = new ArrayList<>();
        String query = "SELECT account_name, account_password FROM passwords WHERE user_id = ?";
        try (Connection conn = Database.connect();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, userId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                passwords.add(new String[]{rs.getString("account_name"), rs.getString("account_password")});
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return passwords;
    }
}
