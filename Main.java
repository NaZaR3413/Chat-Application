import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

// java -cp "c:\Users\nilay\Downloads\mysql-connector-j-8.4.0\mysql-connector-j-8.4.0\mysql-connector-j-8.4.0.jar;./bin" Main.java

public class Main {
    public static void main(String[] args) {

        try (Connection conn = Database.connect()) {
            if (conn != null) {
                System.out.println("Successfully connected to the database!");
                //deleteUser(conn, "exampleUser3");

                retrieveUsers(conn);

            } else {
                System.out.println("Failed to connect to the database.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // currently retrieves every user in the database
    private static void retrieveUsers(Connection conn) throws SQLException {
        String sql = "SELECT * FROM Users;";
        try (PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {
            while (rs.next()) {
                System.out.println("User ID: " + rs.getInt("id") + ", Username: " + rs.getString("username"));
            }
        }
    }

    // deletes a select user
    private static void deleteUser(Connection conn, String username) throws SQLException {
        String sql = "DELETE FROM Users WHERE username = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, username);
            int affectedRows = pstmt.executeUpdate();
            if (affectedRows > 0) {
                System.out.println("User deleted successfully!");
            } else {
                System.out.println("No user found with the specified username.");
            }
        }
    }

}