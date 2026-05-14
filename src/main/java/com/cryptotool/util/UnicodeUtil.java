package com.cryptotool.util;

public class UnicodeUtil {
    /**
     * Chuyển đổi chuỗi thành Unicode escape sequence
     */
    public static String toUnicodeEscape(String input) {
        StringBuilder result = new StringBuilder();
        for (char c : input.toCharArray()) {
            if (c < 128) {
                result.append(c);
            } else {
                result.append(String.format("\\u%04X", (int) c));
            }
        }
        return result.toString();
    }

    /**
     * Chuyển đổi Unicode escape sequence thành chuỗi
     */
    public static String fromUnicodeEscape(String input) {
        StringBuilder result = new StringBuilder();
        int i = 0;
        while (i < input.length()) {
            if (input.charAt(i) == '\\' && i + 5 < input.length() && input.charAt(i + 1) == 'u') {
                String hex = input.substring(i + 2, i + 6);
                try {
                    char c = (char) Integer.parseInt(hex, 16);
                    result.append(c);
                    i += 6;
                } catch (NumberFormatException e) {
                    result.append(input.charAt(i));
                    i++;
                }
            } else {
                result.append(input.charAt(i));
                i++;
            }
        }
        return result.toString();
    }

    /**
     * Lấy mã Unicode của ký tự
     */
    public static int getUnicodeValue(char c) {
        return (int) c;
    }

    /**
     * Tạo ký tự từ mã Unicode
     */
    public static char getCharFromUnicode(int code) {
        return (char) code;
    }

    /**
     * Kiểm tra ký tự có phải là chữ cái không (English A-Z, a-z)
     */
    public static boolean isEnglishLetter(char c) {
        return (c >= 'A' && c <= 'Z') || (c >= 'a' && c <= 'z');
    }

    /**
     * Chuyển ký tự thành chữ hoa (English)
     */
    public static char toUpperCaseEnglish(char c) {
        if (c >= 'a' && c <= 'z') {
            return (char) (c - 32);
        }
        return c;
    }

    /**
     * Chuyển ký tự thành chữ thường (English)
     */
    public static char toLowerCaseEnglish(char c) {
        if (c >= 'A' && c <= 'Z') {
            return (char) (c + 32);
        }
        return c;
    }

    /**
     * Lấy vị trí của chữ cái trong bảng chữ cái (A=0, B=1, ..., Z=25)
     */
    public static int getEnglishLetterIndex(char c) {
        c = toUpperCaseEnglish(c);
        if (c >= 'A' && c <= 'Z') {
            return c - 'A';
        }
        return -1;
    }

    /**
     * Lấy chữ cái từ index (0=A, 1=B, ..., 25=Z)
     */
    public static char getEnglishLetterFromIndex(int index) {
        if (index >= 0 && index < 26) {
            return (char) ('A' + index);
        }
        return '?';
    }

    /**
     * Kiểm tra xem chuỗi có phải toàn là chữ cái English không
     */
    public static boolean isEnglishAlphabet(String text) {
        if (text == null || text.isEmpty()) {
            return false;
        }
        for (char c : text.toCharArray()) {
            if (!isEnglishLetter(c)) {
                return false;
            }
        }
        return true;
    }

    /**
     * Chuyển chuỗi sang dạng Unicode numbers
     */
    public static String stringToUnicodeNumbers(String input) {
        StringBuilder result = new StringBuilder();
        for (char c : input.toCharArray()) {
            result.append((int) c).append(" ");
        }
        return result.toString().trim();
    }

    /**
     * Chuyển Unicode numbers thành chuỗi
     */
    public static String unicodeNumbersToString(String numbers) {
        StringBuilder result = new StringBuilder();
        String[] codes = numbers.split(" ");
        for (String code : codes) {
            try {
                result.append((char) Integer.parseInt(code));
            } catch (NumberFormatException e) {
                // Skip invalid numbers
            }
        }
        return result.toString();
    }

    /**
     * Kiểm tra ký tự có phải là số không
     */
    public static boolean isDigit(char c) {
        return c >= '0' && c <= '9';
    }

    /**
     * Kiểm tra ký tự có phải là ký tự đặc biệt không
     */
    public static boolean isSpecialCharacter(char c) {
        return !isEnglishLetter(c) && !isDigit(c) && !Character.isWhitespace(c);
    }

    /**
     * Lấy tên Unicode của ký tự (nếu có)
     */
    public static String getCharacterName(char c) {
        int code = (int) c;
        if (code >= 32 && code <= 126) {
            return "ASCII: " + c;
        }
        return "Unicode: U+" + String.format("%04X", code);
    }
}
