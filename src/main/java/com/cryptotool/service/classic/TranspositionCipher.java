package com.cryptotool.service.classic;

import com.cryptotool.service.classic.alphabet.Alphabet;
import com.cryptotool.service.classic.alphabet.AlphabetRepository;
import com.cryptotool.service.classic.alphabet.AlphabetType;

public class TranspositionCipher {
    private Alphabet alphabet;

    public TranspositionCipher(AlphabetType alphabetType) {
        this.alphabet = AlphabetRepository.getAlphabet(alphabetType);
    }

    /**
     * Mã hóa Rail Fence Cipher
     */
    public String encryptRailFence(String plaintext, int numRails) throws Exception {
        if (numRails < 2) {
            throw new Exception("Số đường phải >= 2!");
        }

        if (plaintext.isEmpty()) {
            return plaintext;
        }

        // Tạo các hàng
        StringBuilder[] rails = new StringBuilder[numRails];
        for (int i = 0; i < numRails; i++) {
            rails[i] = new StringBuilder();
        }

        int rail = 0;
        int direction = 1; // 1 xuống, -1 lên

        for (char c : plaintext.toCharArray()) {
            rails[rail].append(c);

            // Thay đổi hướng ở các đầu
            if (rail == 0) {
                direction = 1;
            } else if (rail == numRails - 1) {
                direction = -1;
            }

            rail += direction;
        }

        // Gộp các hàng
        StringBuilder result = new StringBuilder();
        for (StringBuilder r : rails) {
            result.append(r);
        }

        return result.toString();
    }

    /**
     * Giải mã Rail Fence Cipher
     */
    public String decryptRailFence(String ciphertext, int numRails) throws Exception {
        if (numRails < 2) {
            throw new Exception("Số đường phải >= 2!");
        }

        if (ciphertext.isEmpty()) {
            return ciphertext;
        }

        int len = ciphertext.length();
        StringBuilder[] rails = new StringBuilder[numRails];
        for (int i = 0; i < numRails; i++) {
            rails[i] = new StringBuilder();
        }

        // Tính độ dài mỗi hàng
        int[] railLengths = new int[numRails];
        int rail = 0;
        int direction = 1;

        for (int i = 0; i < len; i++) {
            railLengths[rail]++;

            if (rail == 0) {
                direction = 1;
            } else if (rail == numRails - 1) {
                direction = -1;
            }

            rail += direction;
        }

        // Chia ciphertext thành các hàng
        int index = 0;
        for (int i = 0; i < numRails; i++) {
            for (int j = 0; j < railLengths[i]; j++) {
                rails[i].append(ciphertext.charAt(index++));
            }
        }

        // Tái tạo plaintext
        StringBuilder result = new StringBuilder();
        int[] railIndices = new int[numRails];
        rail = 0;
        direction = 1;

        for (int i = 0; i < len; i++) {
            result.append(rails[rail].charAt(railIndices[rail]++));

            if (rail == 0) {
                direction = 1;
            } else if (rail == numRails - 1) {
                direction = -1;
            }

            rail += direction;
        }

        return result.toString();
    }

    /**
     * Mã hóa Row-Column Transposition Cipher
     */
    public String encryptRowColumn(String plaintext, int[] keySequence) throws Exception {
        if (keySequence == null || keySequence.length == 0) {
            throw new Exception("Key sequence không được để trống!");
        }

        int cols = keySequence.length;
        int rows = (plaintext.length() + cols - 1) / cols;

        // Tạo ma trận
        char[][] matrix = new char[rows][cols];
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                int index = i * cols + j;
                matrix[i][j] = index < plaintext.length() ? plaintext.charAt(index) : ' ';
            }
        }

        // Đọc theo thứ tự key
        StringBuilder result = new StringBuilder();
        for (int keyPos = 1; keyPos <= keySequence.length; keyPos++) {
            for (int i = 0; i < keySequence.length; i++) {
                if (keySequence[i] == keyPos) {
                    for (int j = 0; j < rows; j++) {
                        result.append(matrix[j][i]);
                    }
                    break;
                }
            }
        }

        return result.toString();
    }

    /**
     * Giải mã Row-Column Transposition Cipher
     */
    public String decryptRowColumn(String ciphertext, int[] keySequence) throws Exception {
        if (keySequence == null || keySequence.length == 0) {
            throw new Exception("Key sequence không được để trống!");
        }

        int cols = keySequence.length;
        int rows = (ciphertext.length() + cols - 1) / cols;

        // Tạo ma trận
        char[][] matrix = new char[rows][cols];
        int index = 0;

        // Điền ma trận theo thứ tự key
        for (int keyPos = 1; keyPos <= keySequence.length; keyPos++) {
            for (int i = 0; i < keySequence.length; i++) {
                if (keySequence[i] == keyPos) {
                    for (int j = 0; j < rows; j++) {
                        matrix[j][i] = ciphertext.charAt(index++);
                    }
                    break;
                }
            }
        }

        // Đọc lần lượt từ trái sang phải, trên xuống dưới
        StringBuilder result = new StringBuilder();
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                result.append(matrix[i][j]);
            }
        }

        return result.toString();
    }
}
