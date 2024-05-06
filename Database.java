import java.sql.*;

public class Database {
    // sql database url
    private static final String url = "";
    private static final String user = ""; // mysql username
    private static final String password = ""; // mysql password

    public static Connection connect() {
        //try to connect to the database. return nothing if unsuccessful
        try {
            return DriverManager.getConnection(url);
        } catch (SQLException e) {
            System.out.println("ERROR: Database Connection Failed: " + e.getMessage());
            return null;
        }
    }
    
}
