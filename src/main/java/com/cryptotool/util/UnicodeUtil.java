package com.cryptotool.util;

public class UnicodeUtil {
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
        for (char c : text.toCharArray()) {
            if (!isEnglishLetter(c)) {
                return false;
            }
        }
        return true;
    }
}
