package com.cryptotool.service.classic;

import com.cryptotool.service.classic.alphabet.Alphabet;
import com.cryptotool.service.classic.alphabet.AlphabetRepository;
import com.cryptotool.service.classic.alphabet.AlphabetType;

public class VigenereCipher {
    private Alphabet alphabet;

    public VigenereCipher(AlphabetType alphabetType) {
        this.alphabet = AlphabetRepository.getAlphabet(alphabetType);
    }

    /**
     * Mã hóa Vigenère Cipher
     */
    public String encrypt(String plaintext, String key) throws Exception {
        if (key == null || key.isEmpty()) {
            throw new Exception("Key không được để trống!");
        }

        // Kiểm tra key chỉ chứa ký tự trong bảng chữ cái
        for (char c : key.toCharArray()) {
            if (!alphabet.contains(c)) {
                throw new Exception("Key chỉ được chứa ký tự trong bảng chữ cái!");
            }
        }

        StringBuilder result = new StringBuilder();
        int keyIndex = 0;

        for (char c : plaintext.toCharArray()) {
            if (alphabet.contains(c)) {
                int plainIndex = alphabet.getIndex(c);
                int keyCharIndex = alphabet.getIndex(key.charAt(keyIndex % key.length()));
                int encryptedIndex = (plainIndex + keyCharIndex) % alphabet.getSize();
                result.append(alphabet.getCharAt(encryptedIndex));
                keyIndex++;
            } else {
                result.append(c);
            }
        }
        return result.toString();
    }

    /**
     * Giải mã Vigenère Cipher
     */
    public String decrypt(String ciphertext, String key) throws Exception {
        if (key == null || key.isEmpty()) {
            throw new Exception("Key không được để trống!");
        }

        // Kiểm tra key chỉ chứa ký tự trong bảng chữ cái
        for (char c : key.toCharArray()) {
            if (!alphabet.contains(c)) {
                throw new Exception("Key chỉ được chứa ký tự trong bảng chữ cái!");
            }
        }

        StringBuilder result = new StringBuilder();
        int keyIndex = 0;

        for (char c : ciphertext.toCharArray()) {
            if (alphabet.contains(c)) {
                int cipherIndex = alphabet.getIndex(c);
                int keyCharIndex = alphabet.getIndex(key.charAt(keyIndex % key.length()));
                int decryptedIndex = (cipherIndex - keyCharIndex + alphabet.getSize()) % alphabet.getSize();
                result.append(alphabet.getCharAt(decryptedIndex));
                keyIndex++;
            } else {
                result.append(c);
            }
        }
        return result.toString();
    }
}
