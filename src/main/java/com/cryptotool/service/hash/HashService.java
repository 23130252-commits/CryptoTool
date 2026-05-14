package com.cryptotool.service.hash;

import com.cryptotool.util.HexUtil;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;

public class HashService {
    private HashService() {
        // Utility class, không cần tạo object.
    }

    public static String hashMD5(String input) throws Exception {
        return hashText(input, "MD5");
    }

    public static String hashMD5(byte[] input) throws Exception {
        return hashBytes(input, "MD5");
    }

    public static String hashSHA1(String input) throws Exception {
        return hashText(input, "SHA-1");
    }

    public static String hashSHA1(byte[] input) throws Exception {
        return hashBytes(input, "SHA-1");
    }

    public static String hashSHA256(String input) throws Exception {
        return hashText(input, "SHA-256");
    }

    public static String hashSHA256(byte[] input) throws Exception {
        return hashBytes(input, "SHA-256");
    }

    public static String hashText(String input, String algorithm) throws Exception {
        if (input == null) {
            throw new IllegalArgumentException("Dữ liệu văn bản không được null.");
        }

        byte[] inputBytes = input.getBytes(StandardCharsets.UTF_8);
        return hashBytes(inputBytes, algorithm);
    }

    public static String hashBytes(byte[] input, String algorithm) throws Exception {
        if (input == null) {
            throw new IllegalArgumentException("Dữ liệu byte không được null.");
        }

        validateAlgorithm(algorithm);

        MessageDigest messageDigest = MessageDigest.getInstance(algorithm);
        byte[] hashBytes = messageDigest.digest(input);

        return HexUtil.toHex(hashBytes);
    }

    public static boolean compareHash(String hash1, String hash2) {
        if (hash1 == null || hash2 == null) {
            return false;
        }

        return hash1.trim().equalsIgnoreCase(hash2.trim());
    }

    public static String detectHashType(String hash) {
        if (hash == null) {
            return "Unknown";
        }

        String normalizedHash = hash.trim();

        switch (normalizedHash.length()) {
            case 32:
                return "MD5";
            case 40:
                return "SHA-1";
            case 64:
                return "SHA-256";
            default:
                return "Unknown";
        }
    }

    private static void validateAlgorithm(String algorithm) {
        if (!isSupportedAlgorithm(algorithm)) {
            throw new IllegalArgumentException("Thuật toán băm không được hỗ trợ: " + algorithm);
        }
    }

    public static boolean isSupportedAlgorithm(String algorithm) {
        return "MD5".equals(algorithm)
                || "SHA-1".equals(algorithm)
                || "SHA-256".equals(algorithm);
    }
}