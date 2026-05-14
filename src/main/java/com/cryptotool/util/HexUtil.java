package com.cryptotool.util;

public class HexUtil {
    /**
     * Chuyển chuỗi thành Hex
     */
    public static String toHex(String input) {
        return toHex(input.getBytes());
    }

    /**
     * Chuyển byte array sang chuỗi HEX (chữ thường)
     */
    public static String toHex(byte[] bytes) {
        return bytesToHex(bytes);
    }

    /**
     * Chuyển byte array sang chuỗi HEX
     */
    public static String bytesToHex(byte[] bytes) {
        StringBuilder sb = new StringBuilder();
        for (byte b : bytes) {
            sb.append(String.format("%02x", b));
        }
        return sb.toString();
    }

    /**
     * Chuyển byte array sang chuỗi HEX (chữ hoa)
     */
    public static String bytesToHexUpperCase(byte[] bytes) {
        StringBuilder sb = new StringBuilder();
        for (byte b : bytes) {
            sb.append(String.format("%02X", b));
        }
        return sb.toString();
    }

    /**
     * Chuyển Hex thành chuỗi
     */
    public static String fromHex(String hex) {
        return new String(fromHexToBytes(hex));
    }

    /**
     * Chuyển chuỗi HEX sang byte array
     */
    public static byte[] hexToBytes(String hex) {
        return fromHexToBytes(hex);
    }

    /**
     * Chuyển Hex thành mảng byte
     */
    public static byte[] fromHexToBytes(String hex) {
        hex = hex.replaceAll(" ", "").replaceAll("\n", "").replaceAll("\r", "");
        int len = hex.length();
        byte[] data = new byte[len / 2];
        for (int i = 0; i < len; i += 2) {
            data[i / 2] = (byte) ((Character.digit(hex.charAt(i), 16) << 4)
                    + Character.digit(hex.charAt(i + 1), 16));
        }
        return data;
    }

    /**
     * Kiểm tra xem chuỗi có phải Hex hợp lệ không
     */
    public static boolean isValidHex(String hex) {
        if (hex == null || hex.isEmpty()) {
            return false;
        }
        hex = hex.replaceAll(" ", "");
        if (hex.length() % 2 != 0) {
            return false;
        }
        return hex.matches("[0-9A-Fa-f]+");
    }

    /**
     * Chuyển Hex thành chuỗi ASCII (nếu có thể)
     */
    public static String hexToAscii(String hex) {
        StringBuilder sb = new StringBuilder();
        byte[] bytes = hexToBytes(hex);
        for (byte b : bytes) {
            if (b >= 32 && b <= 126) {
                sb.append((char) b);
            } else {
                sb.append(".");
            }
        }
        return sb.toString();
    }

    /**
     * Hiển thị Hex với định dạng đẹp (16 bytes trên một dòng)
     */
    public static String hexDump(byte[] bytes) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < bytes.length; i++) {
            if (i % 16 == 0 && i > 0) {
                sb.append("\n");
            }
            sb.append(String.format("%02X ", bytes[i]));
        }
        return sb.toString();
    }

    /**
     * Lấy kích thước dữ liệu từ hex string
     */
    public static int getHexSize(String hex) {
        return hexToBytes(hex).length;
    }

    /**
     * So sánh hai hex string (case-insensitive)
     */
    public static boolean compareHex(String hex1, String hex2) {
        return hex1.equalsIgnoreCase(hex2);
    }
}
