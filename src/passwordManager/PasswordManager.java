package passwordManager;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class PasswordManager {
    public static boolean addPassword(int userId, String accountName, String accountPassword) {
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
                String accountName = rs.getString("account_name");
                String accountPassword = rs.getString("account_password");
                passwords.add(new String[]{accountName, accountPassword});
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return passwords;
    }
}
