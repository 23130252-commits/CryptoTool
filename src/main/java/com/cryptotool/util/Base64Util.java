package com.cryptotool.util;

import java.util.Base64;

public class Base64Util {
    /**
     * Mã hóa chuỗi thành Base64
     */
    public static String encode(String input) {
        return encode(input.getBytes());
    }

    /**
     * Mã hóa byte array sang chuỗi Base64
     */
    public static String encode(byte[] data) {
        return Base64.getEncoder().encodeToString(data);
    }

    /**
     * Giải mã Base64 thành chuỗi
     */
    public static String decode(String input) {
        byte[] decoded = decodeToBytes(input);
        return new String(decoded);
    }

    /**
     * Giải mã chuỗi Base64 sang byte array
     */
    public static byte[] decodeToBytes(String base64String) {
        return Base64.getDecoder().decode(base64String);
    }

    /**
     * Mã hóa chuỗi sang Base64
     */
    public static String encodeString(String text) {
        return encode(text.getBytes());
    }

    /**
     * Giải mã Base64 sang chuỗi
     */
    public static String decodeString(String base64String) {
        return new String(decodeToBytes(base64String));
    }

    /**
     * Kiểm tra chuỗi có phải Base64 hợp lệ không
     */
    public static boolean isValidBase64(String input) {
        try {
            Base64.getDecoder().decode(input);
            return true;
        } catch (IllegalArgumentException e) {
            return false;
        }
    }

    /**
     * Mã hóa Base64 URL-safe
     */
    public static String encodeUrlSafe(String input) {
        return encodeUrlSafe(input.getBytes());
    }

    /**
     * Mã hóa Base64 URL-safe từ byte array
     */
    public static String encodeUrlSafe(byte[] input) {
        return Base64.getUrlEncoder().encodeToString(input);
    }

    /**
     * Giải mã Base64 URL-safe
     */
    public static String decodeUrlSafe(String input) {
        return new String(decodeUrlSafeToBytes(input));
    }

    /**
     * Giải mã Base64 URL-safe thành byte array
     */
    public static byte[] decodeUrlSafeToBytes(String input) {
        return Base64.getUrlDecoder().decode(input);
    }

    /**
     * Mã hóa MIME Base64 (với line breaks)
     */
    public static String encodeMime(byte[] input) {
        return Base64.getMimeEncoder().encodeToString(input);
    }

    /**
     * Giải mã MIME Base64
     */
    public static byte[] decodeMimeToBytes(String input) {
        return Base64.getMimeDecoder().decode(input);
    }

    /**
     * Lấy kích thước dữ liệu khi giải mã Base64
     */
    public static int getDecodedSize(String base64String) {
        int padding = 0;
        if (base64String.endsWith("==")) {
            padding = 2;
        } else if (base64String.endsWith("=")) {
            padding = 1;
        }
        return (base64String.length() * 3) / 4 - padding;
    }
}
