import java.util.*;
import java.sql.*;
import java.security.*;

public class Auth {
    
    // register a new user
    public static boolean registerUser(String username, String password) {
        return true;
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
