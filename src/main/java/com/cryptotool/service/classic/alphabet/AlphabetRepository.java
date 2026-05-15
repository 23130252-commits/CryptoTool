package com.cryptotool.service.classic.alphabet;

public class AlphabetRepository {
    private AlphabetRepository() {
    }

    public static Alphabet getAlphabet(AlphabetType type) {
        if (type == null) {
            return new Alphabet(AlphabetType.ENGLISH);
        }

        return new Alphabet(type);
    }

    public static Alphabet getEnglishAlphabet() {
        return new Alphabet(AlphabetType.ENGLISH);
    }

    public static Alphabet getVietnameseAlphabet() {
        return new Alphabet(AlphabetType.VIETNAMESE);
    }

    public static Alphabet getMixedAlphabet() {
        return new Alphabet(AlphabetType.MIXED);
    }

    public static Alphabet getAlphabetByName(String name) {
        if (name == null || name.trim().isEmpty()) {
            return getEnglishAlphabet();
        }

        String normalizedName = name.trim().toLowerCase();

        switch (normalizedName) {
            case "english":
            case "en":
            case "eng":
                return getEnglishAlphabet();

            case "vietnamese":
            case "vi":
            case "vie":
            case "tieng viet":
            case "tiếng việt":
                return getVietnameseAlphabet();

            case "mixed":
            case "mix":
                return getMixedAlphabet();

            default:
                return getEnglishAlphabet();
        }
    }

    public static String getCharacters(AlphabetType type) {
        return getAlphabet(type).getCharacters();
    }

    public static String getEnglishCharacters() {
        return getEnglishAlphabet().getCharacters();
    }

    public static String getVietnameseCharacters() {
        return getVietnameseAlphabet().getCharacters();
    }

    public static String getMixedCharacters() {
        return getMixedAlphabet().getCharacters();
    }

    /**
     * Dùng riêng cho Hill Cipher.
     */
    public static Alphabet getEnglishUppercaseAlphabet() {
        return new Alphabet("English Uppercase A-Z", "ABCDEFGHIJKLMNOPQRSTUVWXYZ");
    }

    public static String getEnglishUppercaseCharacters() {
        return "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    }
}