package com.cryptotool.util;

import java.util.Base64;

public class Base64Util {
    /**
     * Mã hóa byte array sang chuỗi Base64
     */
    public static String encode(byte[] data) {
        return Base64.getEncoder().encodeToString(data);
    }

    /**
     * Giải mã chuỗi Base64 sang byte array
     */
    public static byte[] decode(String base64String) {
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
        return new String(decode(base64String));
    }
}
