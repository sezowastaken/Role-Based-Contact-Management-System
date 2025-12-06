package util;

import java.security.MessageDigest;

/**
 * Utility class for hashing and verifying passwords using SHA-256.
 */
public class HashUtil {

    public static String hash(String password){

        try {

            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[]  encodedhash = md.digest(password.getBytes());
        
            StringBuilder sb = new StringBuilder();
        
            for(byte b : encodedhash){

                sb.append(String.format("%02x", b));
            }
            return sb.toString();
        } 

        catch (Exception e){
            throw new RuntimeException("An error occurred while hashing the password!",e);
        }
        /* Hashes the given plain text password and compares it with the stored hash,
        returns true if they match */
    }

    /**
     * Verifies if the plain password matches the stored hash.
     * @param plainPassword The plain text password to verify.
     * @param storedHashString The stored hashed password.
     * @return true if the passwords match, false otherwise.
     */
    public static boolean verify(String plainPassword, String storedHashString){
        
        return hash(plainPassword).equalsIgnoreCase(storedHashString);
    }

}
