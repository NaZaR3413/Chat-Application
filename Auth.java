import java.util.*;
import java.sql.*;
import java.security.*;

public class Auth {
    // preworded sql statements
    private static final String INSERT_USER = "INSERT INTO Users (username, password_hash, salt) VALUES (?, ?, ?)";
    private static final String SELECT_USER = "SELECT id, password_hash, salt FROM Users WHERE username = ?";
    
    // register a new user
    public static Integer registerUser(String username, String password) {
        try (Connection conn = Database.connect()) { // Connection conn = Database.connect()
            // generate salt and hash user's password
            String salt = generateSalt();
            String hashedPassword = hashPassword(password, salt);

            // adding the user to our database
            PreparedStatement pstmt = conn.prepareStatement(INSERT_USER);
            // replace the ? values in the INSERT_USER 
            pstmt.setString(1, username);
            pstmt.setString(2, hashedPassword);
            pstmt.setString(3, salt);
            pstmt.executeUpdate();

            // grab user and return user's ID to the server
            pstmt = conn.prepareStatement(SELECT_USER);
            pstmt.setString(1, username);

            return pstmt.executeQuery().getInt("id");

        } catch(SQLException e) { // print the error and return false if unsuccessful
            System.out.println("ERROR in registeruser: " + e);
            e.printStackTrace();
            return -1;
        } catch(NoSuchAlgorithmException p) {
            System.out.println("ERROR in registeruser: " + p);
            p.printStackTrace();
            return -1;
        }
    } 
    
    // log in a user
    public static Integer loginUser(String username, String password) {
        try (Connection conn = Database.connect()) { // (Connection conn = Database.connect())
            // initialize the query to get user information, if it exists
            PreparedStatement pstmt = conn.prepareStatement(SELECT_USER);
            pstmt.setString(1, username);

            // attempt to retrieve a user from database
            ResultSet rs = pstmt.executeQuery();
            if(rs.next()) {
                // retrieve stored hash and salt
                String storedHash = rs.getString("password_hash");
                String salt = rs.getString("salt");

                // hash the password user is attempting to login with, and compare the hashed password 
                // with the stored hashed password to confirm if it is correct or not 
                String hashedPassword = hashPassword(password, salt);
                if(hashedPassword.equals(storedHash)) {
                    // return the user's id if successful
                    return rs.getInt("id");
                }
            }

        } catch(SQLException e) {
            System.out.println("ERROR in loginuser: " + e);
            e.printStackTrace();
            return -1;
        } catch(NoSuchAlgorithmException p) {
            System.out.println("ERROR in loginuser: " + p);
            p.printStackTrace();
            return -1;
        }
        return -1;
    }

    // method to hash a password with a given salt
    private static String hashPassword(String password, String salt) throws NoSuchAlgorithmException{
        MessageDigest md = MessageDigest.getInstance("SHA-256");
        md.update(Base64.getDecoder().decode(salt));
        byte[] hashedPassword = md.digest(password.getBytes());
        return Base64.getEncoder().encodeToString(hashedPassword);
    }

    // method to generate a random salt
    private static String generateSalt()  throws NoSuchAlgorithmException{
        SecureRandom sr = SecureRandom.getInstance("SHA1PRNG");
        byte[] salt = new byte[16];
        sr.nextBytes(salt);
        return Base64.getEncoder().encodeToString(salt);
    }
}
