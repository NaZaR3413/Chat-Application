import java.sql.*;

// java -cp "c:\Users\nilay\Downloads\mysql-connector-j-8.4.0\mysql-connector-j-8.4.0\mysql-connector-j-8.4.0.jar;./bin" Database.java

public class Database {
    // sql database url
    private static final String url = "jdbc:mysql://localhost:3306/chatapp";
    private static final String user = "root"; // mysql username
    private static final String password = ""; // mysql password

    /*static {
        try {
            System.out.println(System.getProperty("java.class.path"));

            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            System.out.println("ERROR: MySQL JDBC Driver not found.");
            e.printStackTrace();
        }
    }*/

    public static Connection connect() {
        //try to connect to the database. return nothing if unsuccessful
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            return DriverManager.getConnection(url, user, password);
        } catch (SQLException e) {
            System.out.println("ERROR: Database Connection Failed: " + e.getMessage());
            e.printStackTrace();
            return null;
        } catch (ClassNotFoundException e) {
            System.out.println("ERROR: MySQL JDBC Driver not found.");
            e.printStackTrace();
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
