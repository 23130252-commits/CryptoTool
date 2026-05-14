package com.cryptotool.service.classic;

import com.cryptotool.service.classic.alphabet.Alphabet;
import com.cryptotool.service.classic.alphabet.AlphabetRepository;
import com.cryptotool.service.classic.alphabet.AlphabetType;

public class SubstitutionCipher {
    private Alphabet alphabet;

    public SubstitutionCipher(AlphabetType alphabetType) {
        this.alphabet = AlphabetRepository.getAlphabet(alphabetType);
    }

    /**
     * Mã hóa Substitution Cipher
     * key là chuỗi thay thế, phải có độ dài bằng kích thước bảng chữ cái
     */
    public String encrypt(String plaintext, String key) throws Exception {
        if (key == null || key.isEmpty()) {
            throw new Exception("Key không được để trống!");
        }
        if (key.length() != alphabet.getSize()) {
            throw new Exception("Key phải có độ dài " + alphabet.getSize() + " ký tự!");
        }

        // Kiểm tra key có chứa tất cả ký tự trong bảng chữ cái không
        for (int i = 0; i < alphabet.getSize(); i++) {
            char c = alphabet.getCharAt(i);
            if (key.indexOf(c) == -1) {
                throw new Exception("Key phải chứa tất cả ký tự trong bảng chữ cái!");
            }
        }

        StringBuilder result = new StringBuilder();
        for (char c : plaintext.toCharArray()) {
            if (alphabet.contains(c)) {
                int index = alphabet.getIndex(c);
                result.append(key.charAt(index));
            } else {
                result.append(c);
            }
        }
        return result.toString();
    }

    /**
     * Giải mã Substitution Cipher
     */
    public String decrypt(String ciphertext, String key) throws Exception {
        if (key == null || key.isEmpty()) {
            throw new Exception("Key không được để trống!");
        }
        if (key.length() != alphabet.getSize()) {
            throw new Exception("Key phải có độ dài " + alphabet.getSize() + " ký tự!");
        }

        // Tạo key đảo ngược (reverse key)
        String originalAlphabet = alphabet.getAlphabet();
        char[] reverseKey = new char[alphabet.getSize()];

        for (int i = 0; i < alphabet.getSize(); i++) {
            char c = key.charAt(i);
            int origIndex = originalAlphabet.indexOf(c);
            if (origIndex >= 0) {
                reverseKey[origIndex] = originalAlphabet.charAt(i);
            }
        }

        StringBuilder result = new StringBuilder();
        for (char c : ciphertext.toCharArray()) {
            int index = key.indexOf(c);
            if (index >= 0) {
                result.append(reverseKey[index]);
            } else {
                result.append(c);
            }
        }
        return result.toString();
    }

    /**
     * Tạo key ngẫu nhiên
     */
    public String generateRandomKey() {
        String original = alphabet.getAlphabet();
        char[] chars = original.toCharArray();
        
        // Fisher-Yates shuffle
        for (int i = chars.length - 1; i > 0; i--) {
            int j = (int) (Math.random() * (i + 1));
            char temp = chars[i];
            chars[i] = chars[j];
            chars[j] = temp;
        }
        
        return new String(chars);
    }
}
