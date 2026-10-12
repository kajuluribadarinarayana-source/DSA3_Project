package algorithms;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;

public class Hashing {

    public static String hash(String text) {

        try {

            MessageDigest md =
                    MessageDigest.getInstance("SHA-256");

            byte[] hash =
                    md.digest(text.getBytes(StandardCharsets.UTF_8));

            StringBuilder result = new StringBuilder();

            for (byte b : hash) {
                result.append(String.format("%02x", b));
            }

            return result.toString();

        } catch (Exception e) {
            return "";
        }
    }

    public static boolean areEqual(String text1, String text2) {
        return hash(text1).equals(hash(text2));
    }
}