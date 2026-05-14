package com.cryptotool.service.classic.alphabet;

public class Alphabet {
    private AlphabetType type;
    private String chars;

    public Alphabet(AlphabetType type) {
        this.type = type;
        this.chars = type.getAlphabet();
    }

    /**
     * Lấy vị trí của ký tự trong bảng chữ cái
     */
    public int getIndex(char c) {
        return chars.indexOf(c);
    }

    /**
     * Lấy ký tự từ vị trí
     */
    public char getCharAt(int index) {
        if (index >= 0 && index < chars.length()) {
            return chars.charAt(index);
        }
        return '?';
    }

    /**
     * Kiểm tra ký tự có trong bảng chữ cái không
     */
    public boolean contains(char c) {
        return chars.indexOf(c) >= 0;
    }

    /**
     * Lấy kích thước bảng chữ cái
     */
    public int getSize() {
        return chars.length();
    }

    /**
     * Lấy chuỗi bảng chữ cái
     */
    public String getAlphabet() {
        return chars;
    }

    /**
     * Lấy loại bảng chữ cái
     */
    public AlphabetType getType() {
        return type;
    }

    /**
     * Kiểm tra xem ký tự có phải chữ cái không (loại trừ khoảng trắng, số, dấu câu)
     */
    public boolean isLetter(char c) {
        return contains(c);
    }
}
