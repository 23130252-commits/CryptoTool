package com.cryptotool.service.classic;

import com.cryptotool.service.classic.alphabet.Alphabet;
import com.cryptotool.service.classic.alphabet.AlphabetRepository;
import com.cryptotool.service.classic.alphabet.AlphabetType;

public class VigenereCipher {
    private final Alphabet alphabet;

    public VigenereCipher() {
        this(AlphabetType.ENGLISH);
    }

    public VigenereCipher(AlphabetType alphabetType) {
        this.alphabet = AlphabetRepository.getAlphabet(alphabetType);
    }

    public String encrypt(String plaintext, String key) {
        validateKey(key);

        plaintext = alphabet.normalizeText(plaintext);
        key = alphabet.normalizeText(key);

        StringBuilder result = new StringBuilder();
        int keyIndex = 0;

        for (char plainChar : plaintext.toCharArray()) {
            int plainPosition = alphabet.indexOf(plainChar);

            if (plainPosition == -1) {
                result.append(plainChar);
                continue;
            }

            char keyChar = getNextKeyChar(key, keyIndex);
            int keyPosition = alphabet.indexOf(keyChar);

            int encryptedPosition = plainPosition + keyPosition;
            result.append(alphabet.charAt(encryptedPosition));

            keyIndex++;
        }

        return result.toString();
    }

    public String decrypt(String ciphertext, String key) {
        validateKey(key);

        ciphertext = alphabet.normalizeText(ciphertext);
        key = alphabet.normalizeText(key);

        StringBuilder result = new StringBuilder();
        int keyIndex = 0;

        for (char cipherChar : ciphertext.toCharArray()) {
            int cipherPosition = alphabet.indexOf(cipherChar);

            if (cipherPosition == -1) {
                result.append(cipherChar);
                continue;
            }

            char keyChar = getNextKeyChar(key, keyIndex);
            int keyPosition = alphabet.indexOf(keyChar);

            int decryptedPosition = cipherPosition - keyPosition;
            result.append(alphabet.charAt(decryptedPosition));

            keyIndex++;
        }

        return result.toString();
    }

    private char getNextKeyChar(String key, int keyIndex) {
        return key.charAt(keyIndex % key.length());
    }

    private void validateKey(String key) {
        if (key == null || key.trim().isEmpty()) {
            throw new IllegalArgumentException("Khóa Vigenere không được để trống.");
        }

        String normalizedKey = alphabet.normalizeText(key.trim());

        for (char ch : normalizedKey.toCharArray()) {
            if (!alphabet.contains(ch)) {
                throw new IllegalArgumentException(
                        "Khóa Vigenere chứa ký tự không nằm trong bảng chữ cái: " + ch
                );
            }
        }
    }
}