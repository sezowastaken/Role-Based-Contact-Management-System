package util;

import java.security.MessageDigest;

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
        /* girilen plain text şifre hashlernir ve storedHash ile karşılaştırır,
        eğer eşlenirse true döner */
    }

    
    public static boolean verify(String plainPassword, String storedHashString){
        
        return hash(plainPassword).equalsIgnoreCase(storedHashString);
    }

}
