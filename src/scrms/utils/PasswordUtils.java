package scrms.utils;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Password hashing and verification utilities.
 */
public final class PasswordUtils {

    private PasswordUtils() {
    }

    /**
     * Hashes the provided password with SHA-256.
     *
     * @param password raw password
     * @return hexadecimal digest
     */
    public static String hash(String password) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hashed = digest.digest(password.getBytes(StandardCharsets.UTF_8));
            return bytesToHex(hashed);
        } catch (NoSuchAlgorithmException e) {
            throw new IllegalStateException("SHA-256 is not available", e);
        }
    }

    /**
     * Compares a raw password with a stored hash.
     *
     * @param password raw password
     * @param hash     stored hash
     * @return true when the password matches the hash
     */
    public static boolean matches(String password, String hash) {
        return hash(password).equals(hash);
    }

    private static String bytesToHex(byte[] data) {
        StringBuilder builder = new StringBuilder();
        for (byte b : data) {
            builder.append(String.format("%02x", b));
        }
        return builder.toString();
    }
}
