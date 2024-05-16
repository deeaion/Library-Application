package server.service.util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Base64;

public class PasswordEncryption{

    public static String[] hashPassword(String password) throws NoSuchAlgorithmException {
        // Generate a seed
        SecureRandom random = new SecureRandom();
        byte[] seedBytes = new byte[16];
        random.nextBytes(seedBytes);
        String seed = Base64.getEncoder().encodeToString(seedBytes);
        System.out.println(seed);
        // Hash the password with the seed
        String hashedPassword = hashWithSeed(password, seed);
        System.out.println(hashedPassword);
        return new String[]{seed, hashedPassword};
    }

    public static boolean verifyPassword(String password, String hashedPassword, String seed) throws NoSuchAlgorithmException {
        // Hash the input password with the retrieved seed
        String hashedInputPassword = hashWithSeed(password, seed);
        // Verify the hashed input password against the stored hashed password
        return hashedInputPassword.equals(hashedPassword);
    }

    private static String hashWithSeed(String password, String seed) throws NoSuchAlgorithmException {
        MessageDigest md = MessageDigest.getInstance("SHA-256");
        md.update(seed.getBytes());
        byte[] hashedBytes = md.digest(password.getBytes());
        return Base64.getEncoder().encodeToString(hashedBytes);
    }}