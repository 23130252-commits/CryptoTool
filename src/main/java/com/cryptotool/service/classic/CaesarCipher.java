package com.cryptotool.service.classic;

import com.cryptotool.service.classic.alphabet.Alphabet;
import com.cryptotool.service.classic.alphabet.AlphabetRepository;
import com.cryptotool.service.classic.alphabet.AlphabetType;

public class CaesarCipher {
    private Alphabet alphabet;

    public CaesarCipher(AlphabetType alphabetType) {
        this.alphabet = AlphabetRepository.getAlphabet(alphabetType);
    }

    /**
     * Mã hóa Caesar Cipher
     */
    public String encrypt(String plaintext, int key) {
        StringBuilder result = new StringBuilder();
        int size = alphabet.getSize();
        key = ((key % size) + size) % size; // Normalize key

        for (char c : plaintext.toCharArray()) {
            if (alphabet.contains(c)) {
                int index = alphabet.getIndex(c);
                int newIndex = (index + key) % size;
                result.append(alphabet.getCharAt(newIndex));
            } else {
                // Giữ nguyên ký tự không trong bảng chữ cái
                result.append(c);
            }
        }
        return result.toString();
    }

    /**
     * Giải mã Caesar Cipher
     */
    public String decrypt(String ciphertext, int key) {
        return encrypt(ciphertext, -key);
    }
}
