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
    private static String hashPassword(String password, String salt) {
        return null;
    }

    // method to generate a random salt
    private static String generateSalt()  {
        return null;
    }
}
