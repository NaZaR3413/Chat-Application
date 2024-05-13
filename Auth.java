import java.util.*;
import java.sql.*;
import java.security.*;

public class Auth {
    // preworded sql statements
    private static final String INSERT_USER = "INSERT INTO Users (username, password_hash, salt) VALUES (?, ?, ?)";
    private static final String SELECT_USER = "SELECT id, password_hash, salt FROM Users WHERE username = ?";
    
    // register a new user
    public static boolean registerUser(String username, String password) {
        try(Connection conn = Database.connect()) {
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

            // return true if successful
            return true;

        } catch(SQLException e) { // print the error and return false if unsuccessful
            System.out.println("ERROR in registeruser: " + e);
            e.printStackTrace();
            return false;
        } catch(NoSuchAlgorithmException p) {
            System.out.println("ERROR in registeruser: " + p);
            p.printStackTrace();
            return false;
        }
    } 
    
    // log in a user
    public static Integer loginUser(String username, String password) {
        return null;
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
