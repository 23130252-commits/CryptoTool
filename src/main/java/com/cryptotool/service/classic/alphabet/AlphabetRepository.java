package com.cryptotool.service.classic.alphabet;

public class AlphabetRepository {
    /**
     * Lấy bảng chữ cái theo loại
     */
    public static Alphabet getAlphabet(AlphabetType type) {
        return new Alphabet(type);
    }

    /**
     * Lấy bảng chữ cái English (A-Z)
     */
    public static Alphabet getEnglishAlphabet() {
        return new Alphabet(AlphabetType.ENGLISH);
    }

    /**
     * Lấy bảng chữ cái Vietnamese
     */
    public static Alphabet getVietnameseAlphabet() {
        return new Alphabet(AlphabetType.VIETNAMESE);
    }

    /**
     * Lấy bảng chữ cái Mixed
     */
    public static Alphabet getMixedAlphabet() {
        return new Alphabet(AlphabetType.MIXED);
    }
}
