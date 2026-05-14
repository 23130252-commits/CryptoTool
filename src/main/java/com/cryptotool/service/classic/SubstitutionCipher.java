package com.cryptotool.service.classic;

import com.cryptotool.service.classic.alphabet.Alphabet;
import com.cryptotool.service.classic.alphabet.AlphabetRepository;
import com.cryptotool.service.classic.alphabet.AlphabetType;

import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class SubstitutionCipher {
    private final Alphabet alphabet;
    private final SecureRandom random;

    public SubstitutionCipher() {
        this(AlphabetType.ENGLISH);
    }

    public SubstitutionCipher(AlphabetType alphabetType) {
        this.alphabet = AlphabetRepository.getAlphabet(alphabetType);
        this.random = new SecureRandom();
    }

    public String encrypt(String plaintext, String key) {
        validateKey(key);

        if (plaintext == null) {
            throw new IllegalArgumentException("Văn bản cần mã hóa không được null.");
        }

        plaintext = alphabet.normalizeText(plaintext);
        key = alphabet.normalizeText(key);

        String originalAlphabet = alphabet.getCharacters();

        StringBuilder result = new StringBuilder();

        for (char ch : plaintext.toCharArray()) {
            int index = originalAlphabet.indexOf(ch);

            if (index == -1) {
                result.append(ch);
                continue;
            }

            result.append(key.charAt(index));
        }

        return result.toString();
    }

    public String decrypt(String ciphertext, String key) {
        validateKey(key);

        if (ciphertext == null) {
            throw new IllegalArgumentException("Văn bản cần giải mã không được null.");
        }

        ciphertext = alphabet.normalizeText(ciphertext);
        key = alphabet.normalizeText(key);

        String originalAlphabet = alphabet.getCharacters();

        StringBuilder result = new StringBuilder();

        for (char ch : ciphertext.toCharArray()) {
            int index = key.indexOf(ch);

            if (index == -1) {
                result.append(ch);
                continue;
            }

            result.append(originalAlphabet.charAt(index));
        }

        return result.toString();
    }

    public String generateRandomKey() {
        String characters = alphabet.getCharacters();

        List<Character> charList = new ArrayList<>();

        for (char ch : characters.toCharArray()) {
            charList.add(ch);
        }

        Collections.shuffle(charList, random);

        StringBuilder key = new StringBuilder();

        for (char ch : charList) {
            key.append(ch);
        }

        return key.toString();
    }

    private void validateKey(String key) {
        if (key == null || key.trim().isEmpty()) {
            throw new IllegalArgumentException("Key Substitution không được để trống.");
        }

        key = alphabet.normalizeText(key.trim());

        String originalAlphabet = alphabet.getCharacters();

        if (key.length() != originalAlphabet.length()) {
            throw new IllegalArgumentException(
                    "Key Substitution phải có độ dài bằng bảng chữ cái: "
                            + originalAlphabet.length()
                            + " ký tự. Hiện tại key có "
                            + key.length()
                            + " ký tự."
            );
        }

        for (char ch : key.toCharArray()) {
            if (!alphabet.contains(ch)) {
                throw new IllegalArgumentException(
                        "Key Substitution chứa ký tự không nằm trong bảng chữ cái: " + ch
                );
            }
        }

        for (int i = 0; i < key.length(); i++) {
            char current = key.charAt(i);

            for (int j = i + 1; j < key.length(); j++) {
                if (current == key.charAt(j)) {
                    throw new IllegalArgumentException(
                            "Key Substitution bị trùng ký tự: " + current
                    );
                }
            }
        }
    }
}