package com.cryptotool.service.classic;

import com.cryptotool.service.classic.alphabet.Alphabet;
import com.cryptotool.service.classic.alphabet.AlphabetRepository;
import com.cryptotool.service.classic.alphabet.AlphabetType;

public class CaesarCipher {
    private final Alphabet alphabet;

    public CaesarCipher() {
        this(AlphabetType.ENGLISH);
    }

    public CaesarCipher(AlphabetType alphabetType) {
        this.alphabet = AlphabetRepository.getAlphabet(alphabetType);
    }

    public String encrypt(String plaintext, int shift) {
        if (plaintext == null) {
            throw new IllegalArgumentException("Văn bản cần mã hóa không được null.");
        }

        plaintext = alphabet.normalizeText(plaintext);

        StringBuilder result = new StringBuilder();

        for (char ch : plaintext.toCharArray()) {
            int index = alphabet.indexOf(ch);

            if (index == -1) {
                result.append(ch);
                continue;
            }

            int encryptedIndex = index + shift;
            result.append(alphabet.charAt(encryptedIndex));
        }

        return result.toString();
    }

    public String decrypt(String ciphertext, int shift) {
        return encrypt(ciphertext, -shift);
    }
}