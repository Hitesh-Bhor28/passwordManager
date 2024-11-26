package passwordManager;

import java.sql.Connection;
import java.sql.SQLException;

public class DatabaseTest {
    public static void main(String[] args) {
        try (Connection conn = Database.connect()) { // Attempt to connect to the database
            if (conn != null) {
                System.out.println("Connected to the database!");
            }
        } catch (SQLException e) {
            // Print the stack trace if connection fails
            e.printStackTrace();
        }
    }
}

