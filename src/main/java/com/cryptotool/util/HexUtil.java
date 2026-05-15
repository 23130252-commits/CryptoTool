package com.cryptotool.util;

public class HexUtil {
    private HexUtil() {
    }

    public static String toHex(byte[] bytes) {
        StringBuilder result = new StringBuilder();

        for (byte b : bytes) {
            result.append(String.format("%02x", b));
        }

        return result.toString();
    }
}