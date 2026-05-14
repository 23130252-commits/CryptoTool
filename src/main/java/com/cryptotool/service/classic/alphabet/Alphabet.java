package com.cryptotool.service.classic.alphabet;

import java.text.Normalizer;
import java.util.HashMap;
import java.util.Map;

public class Alphabet {
    private final AlphabetType type;
    private final String displayName;
    private final String characters;
    private final Map<Character, Integer> indexMap;

    public Alphabet(AlphabetType type) {
        if (type == null) {
            throw new IllegalArgumentException("AlphabetType không được null.");
        }

        this.type = type;
        this.displayName = type.getDisplayName();
        this.characters = normalize(type.getAlphabet());
        this.indexMap = new HashMap<>();

        buildIndexMap();
    }

    public Alphabet(String displayName, String characters) {
        if (characters == null || characters.isEmpty()) {
            throw new IllegalArgumentException("Bảng chữ cái không được để trống.");
        }

        this.type = null;
        this.displayName = displayName == null ? "Custom Alphabet" : displayName;
        this.characters = normalize(characters);
        this.indexMap = new HashMap<>();

        buildIndexMap();
    }

    private void buildIndexMap() {
        for (int i = 0; i < characters.length(); i++) {
            char ch = characters.charAt(i);

            if (indexMap.containsKey(ch)) {
                throw new IllegalArgumentException(
                        "Bảng chữ cái " + displayName + " bị trùng ký tự: " + ch
                );
            }

            indexMap.put(ch, i);
        }
    }

    public AlphabetType getType() {
        return type;
    }

    public String getDisplayName() {
        return displayName;
    }

    public String getCharacters() {
        return characters;
    }

    public int size() {
        return characters.length();
    }

    public boolean contains(char ch) {
        ch = normalizeChar(ch);
        return indexMap.containsKey(ch);
    }

    public int indexOf(char ch) {
        ch = normalizeChar(ch);

        Integer index = indexMap.get(ch);

        if (index == null) {
            return -1;
        }

        return index;
    }

    public char charAt(int index) {
        if (characters.isEmpty()) {
            throw new IllegalStateException("Bảng chữ cái đang rỗng.");
        }

        int safeIndex = mod(index, size());
        return characters.charAt(safeIndex);
    }

    public String normalizeText(String input) {
        if (input == null) {
            return "";
        }

        return normalize(input);
    }

    public String filterOnlyAlphabetCharacters(String input) {
        input = normalizeText(input);

        StringBuilder result = new StringBuilder();

        for (char ch : input.toCharArray()) {
            if (contains(ch)) {
                result.append(ch);
            }
        }

        return result.toString();
    }

    public boolean isValidText(String input) {
        input = normalizeText(input);

        for (char ch : input.toCharArray()) {
            if (!contains(ch)) {
                return false;
            }
        }

        return true;
    }

    public int mod(int value) {
        return mod(value, size());
    }

    private int mod(int value, int modulo) {
        return ((value % modulo) + modulo) % modulo;
    }

    private char normalizeChar(char ch) {
        String normalized = normalize(String.valueOf(ch));

        if (normalized.length() == 1) {
            return normalized.charAt(0);
        }

        return ch;
    }

    private String normalize(String input) {
        return Normalizer.normalize(input, Normalizer.Form.NFC);
    }
}