package com.cryptotool.service.hash;

import java.security.MessageDigest;
import java.util.Base64;

public class HashService {
    /**
     * Tính hash MD5
     */
    public static String hashMD5(String input) throws Exception {
        MessageDigest md = MessageDigest.getInstance("MD5");
        byte[] hash = md.digest(input.getBytes());
        return bytesToHex(hash);
    }

    /**
     * Tính hash MD5 từ byte array
     */
    public static String hashMD5(byte[] input) throws Exception {
        MessageDigest md = MessageDigest.getInstance("MD5");
        byte[] hash = md.digest(input);
        return bytesToHex(hash);
    }

    /**
     * Tính hash SHA-1
     */
    public static String hashSHA1(String input) throws Exception {
        MessageDigest md = MessageDigest.getInstance("SHA-1");
        byte[] hash = md.digest(input.getBytes());
        return bytesToHex(hash);
    }

    /**
     * Tính hash SHA-1 từ byte array
     */
    public static String hashSHA1(byte[] input) throws Exception {
        MessageDigest md = MessageDigest.getInstance("SHA-1");
        byte[] hash = md.digest(input);
        return bytesToHex(hash);
    }

    /**
     * Tính hash SHA-256
     */
    public static String hashSHA256(String input) throws Exception {
        MessageDigest md = MessageDigest.getInstance("SHA-256");
        byte[] hash = md.digest(input.getBytes());
        return bytesToHex(hash);
    }

    /**
     * Tính hash SHA-256 từ byte array
     */
    public static String hashSHA256(byte[] input) throws Exception {
        MessageDigest md = MessageDigest.getInstance("SHA-256");
        byte[] hash = md.digest(input);
        return bytesToHex(hash);
    }

    /**
     * Tính hash SHA-384
     */
    public static String hashSHA384(String input) throws Exception {
        MessageDigest md = MessageDigest.getInstance("SHA-384");
        byte[] hash = md.digest(input.getBytes());
        return bytesToHex(hash);
    }

    /**
     * Tính hash SHA-384 từ byte array
     */
    public static String hashSHA384(byte[] input) throws Exception {
        MessageDigest md = MessageDigest.getInstance("SHA-384");
        byte[] hash = md.digest(input);
        return bytesToHex(hash);
    }

    /**
     * Tính hash SHA-512
     */
    public static String hashSHA512(String input) throws Exception {
        MessageDigest md = MessageDigest.getInstance("SHA-512");
        byte[] hash = md.digest(input.getBytes());
        return bytesToHex(hash);
    }

    /**
     * Tính hash SHA-512 từ byte array
     */
    public static String hashSHA512(byte[] input) throws Exception {
        MessageDigest md = MessageDigest.getInstance("SHA-512");
        byte[] hash = md.digest(input);
        return bytesToHex(hash);
    }

    /**
     * Chuyển mảng byte thành Hex string
     */
    public static String bytesToHex(byte[] bytes) {
        StringBuilder hex = new StringBuilder();
        for (byte b : bytes) {
            hex.append(String.format("%02x", b));
        }
        return hex.toString();
    }

    /**
     * Chuyển mảng byte thành Hex string (chữ hoa)
     */
    public static String bytesToHexUpperCase(byte[] bytes) {
        StringBuilder hex = new StringBuilder();
        for (byte b : bytes) {
            hex.append(String.format("%02X", b));
        }
        return hex.toString();
    }

    /**
     * Chuyển mảng byte thành Base64
     */
    public static String bytesToBase64(byte[] bytes) {
        return Base64.getEncoder().encodeToString(bytes);
    }

    /**
     * So sánh hai hash value
     */
    public static boolean compareHash(String hash1, String hash2) {
        return hash1.equalsIgnoreCase(hash2);
    }

    /**
     * Kiểm tra độ dài hash (để xác định kiểu hash)
     */
    public static String detectHashType(String hash) {
        int length = hash.length();
        switch (length) {
            case 32:
                return "MD5 (128-bit)";
            case 40:
                return "SHA-1 (160-bit)";
            case 56:
                return "SHA-224 (224-bit)";
            case 64:
                return "SHA-256 (256-bit)";
            case 96:
                return "SHA-384 (384-bit)";
            case 128:
                return "SHA-512 (512-bit)";
            default:
                return "Unknown";
        }
    }

    /**
     * Demo: Tính hash từ chuỗi
     */
    public static void main(String[] args) {
        try {
            String data = "Hello, World!";

            System.out.println("========== Hash Demonstration ==========");
            System.out.println("Input: " + data);
            System.out.println();

            String md5 = HashService.hashMD5(data);
            System.out.println("MD5: " + md5);
            System.out.println("Type: " + HashService.detectHashType(md5));
            System.out.println();

            String sha1 = HashService.hashSHA1(data);
            System.out.println("SHA-1: " + sha1);
            System.out.println("Type: " + HashService.detectHashType(sha1));
            System.out.println();

            String sha256 = HashService.hashSHA256(data);
            System.out.println("SHA-256: " + sha256);
            System.out.println("Type: " + HashService.detectHashType(sha256));
            System.out.println();

            String sha512 = HashService.hashSHA512(data);
            System.out.println("SHA-512: " + sha512);
            System.out.println("Type: " + HashService.detectHashType(sha512));
            System.out.println();

            // So sánh hash
            String md5_2 = HashService.hashMD5(data);
            System.out.println("MD5 comparison: " + HashService.compareHash(md5, md5_2));
            System.out.println();

            System.out.println("========== Hash Demo Completed ==========");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
