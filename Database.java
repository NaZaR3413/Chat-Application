import java.sql.*;

public class Database {
    // sql database url
    private static final String url = "jdbc:mysql://localhost:3306/chatApp";
    private static final String user = "root"; // mysql username
    private static final String password = "nhp000r123"; // mysql password

    static {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            System.out.println("ERROR: MySQL JDBC Driver not found.");
            e.printStackTrace();
        }
    }

    public static Connection connect() {
        //try to connect to the database. return nothing if unsuccessful
        try {
            return DriverManager.getConnection(url, user, password);
        } catch (SQLException e) {
            System.out.println("ERROR: Database Connection Failed: " + e.getMessage());
            return null;
        }
    }

    public static void main(String[] args) {
        try (Connection conn = connect()) {
            if (conn != null) {
                System.out.println("Successfully connected to the database!");
            } else {
                System.out.println("Failed to connect to the database.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
}
